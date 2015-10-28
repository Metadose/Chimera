package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.controller.LoginLogoutController;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.service.SystemUserService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.validator.SystemUserValidator;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private SystemUserDAO systemUserDAO;
    private StaffDAO staffDAO;
    private SystemConfigurationDAO systemConfigurationDAO;

    @Value("${webapp.accounts.root.username}")
    private String rootUsername;

    @Value("${webapp.accounts.root.password}")
    private String rootPassword;

    public void setSystemConfigurationDAO(SystemConfigurationDAO systemConfigurationDAO) {
	this.systemConfigurationDAO = systemConfigurationDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    @Autowired
    SystemUserValidator systemUserValidator;

    /**
     * Initialize the root account, if non-existent.
     */
    @Override
    @Transactional
    public void initRoot() {
	// Check if the root account exists.
	// If not, create it.
	try {
	    this.systemUserDAO.searchDatabase(this.rootUsername);
	} catch (Exception e) {
	    // Setup the root account.
	    SystemUser rootUser = new SystemUser();
	    rootUser.setUsername(this.rootUsername);
	    rootUser.setPassword(this.authHelper.encodePassword(this.rootPassword, rootUser));
	    rootUser.setSuperAdmin(true);

	    // Create the account.
	    this.systemUserDAO.create(rootUser);

	    // Update the config value.
	    LoginLogoutController.initWebApp = true;
	    SystemConfiguration appInit = this.systemConfigurationDAO
		    .getByName(ConstantsSystem.CONFIG_ROOT_INIT);
	    appInit.setValue("1");
	    this.systemConfigurationDAO.merge(appInit);
	}
    }

    /**
     * Create a new system user.
     */
    @Override
    @Transactional
    public String create(SystemUser systemUser, BindingResult result) {

	// Only company admins are allowed to create users.
	if (!this.authHelper.isCompanyAdmin()) {
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	this.systemUserValidator.validate(systemUser, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Encrpyt password.
	String encPassword = this.authHelper.encodePassword(systemUser.getPassword(), systemUser);
	systemUser.setPassword(encPassword);

	// Construct the staff of the user.
	Staff staff = new Staff();

	// Set the user company.
	// If it's already carrying a company ID,
	// use it.
	if (systemUser.getCompanyID() != null && systemUser.getCompany() == null) {
	    Company company = new Company(systemUser.getCompanyID());
	    systemUser.setCompany(company);
	    staff.setCompany(company);

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(systemUser)) {
		this.messageHelper.unauthorizedID(SystemUser.OBJECT_NAME, systemUser.getId());
		return AlertBoxGenerator.ERROR;
	    }
	} else {
	    // Else, get it somewhere else.
	    AuthenticationToken auth = this.authHelper.getAuth();
	    Company authCompany = auth.getCompany();
	    systemUser.setCompany(authCompany);
	    staff.setCompany(authCompany);
	}

	// Do service.
	// Create the user and staff.
	// Create the objects first.
	this.systemUserDAO.create(systemUser);
	this.staffDAO.create(staff);

	// Log and notify.
	this.messageHelper.auditableID(AuditAction.ACTION_CREATE, SystemUser.OBJECT_NAME,
		systemUser.getId(), Staff.OBJECT_NAME, staff.getId(), null, systemUser.getUsername());

	// Then link them together.
	systemUser.setStaff(staff);
	this.systemUserDAO.update(systemUser);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateCreate(SystemUser.OBJECT_NAME,
		systemUser.getUsername());
    }

    /**
     * Get an object by id.
     */
    @Override
    @Transactional
    public SystemUser getByID(long id, boolean override) {
	SystemUser obj = this.systemUserDAO.getByID(id);

	if (!override) {
	    // Security check.
	    if (!this.authHelper.isActionAuthorized(obj)) {
		this.messageHelper.unauthorizedID(SystemUser.OBJECT_NAME, obj.getId());
		return new SystemUser();
	    }

	    // Log.
	    this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, SystemUser.OBJECT_NAME,
		    obj.getId());
	}

	// Return obj.
	return obj;
    }

    /**
     * Get object by id, with security.
     */
    @Override
    @Transactional
    public SystemUser getWithSecurityByID(long id) {
	SystemUser obj = this.systemUserDAO.getWithSecurityByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedID(SystemUser.OBJECT_NAME, obj.getId());
	    return new SystemUser();
	}

	// Log.
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, SystemUser.OBJECT_NAME,
		obj.getId());

	// Return obj.
	return obj;
    }

    /**
     * Get obj by id.
     */
    @Override
    @Transactional
    public SystemUser getByID(long id) {
	SystemUser obj = getByID(id, false);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedID(SystemUser.OBJECT_NAME, obj.getId());
	    return new SystemUser();
	}

	// Log.
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, SystemUser.OBJECT_NAME,
		obj.getId());

	// Return obj.
	return obj;
    }

    /**
     * Update a user.
     */
    @Override
    @Transactional
    public String update(SystemUser user, BindingResult result) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(user)) {
	    this.messageHelper.unauthorizedID(SystemUser.OBJECT_NAME, user.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	this.systemUserValidator.validate(user, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	String encPassword = this.authHelper.encodePassword(user.getPassword(), user);
	user.setPassword(encPassword);

	// Log and notify.
	Staff staff = user.getStaff();
	if (staff == null) {
	    this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, SystemUser.OBJECT_NAME,
		    user.getId(), "", "", null, user.getUsername());
	} else {
	    this.messageHelper.auditableID(AuditAction.ACTION_UPDATE, SystemUser.OBJECT_NAME,
		    user.getId(), Staff.OBJECT_NAME, staff.getId(), null, user.getUsername());
	}

	// Do service.
	this.systemUserDAO.update(user);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpdate(SystemUser.OBJECT_NAME, user.getUsername());
    }

    /**
     * Update a user. Only used during authentication.
     */
    @Override
    @Transactional
    public String update(SystemUser user, boolean systemOverride) {
	// If this is not a system call,
	// go through the validation.
	if (systemOverride) {
	    // Do service.
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(SystemUser.OBJECT_NAME, user.getUsername());
	}
	return AlertBoxGenerator.ERROR;
    }

    /**
     * Delete a user given an id.
     */
    @Override
    @Transactional
    public String delete(long id) {
	// Only company admins are allowed to delete users.
	if (!this.authHelper.isCompanyAdmin()) {
	    return AlertBoxGenerator.ERROR;
	}

	SystemUser obj = this.systemUserDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedID(SystemUser.OBJECT_NAME, obj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log and notify.
	this.messageHelper.auditableKey(AuditAction.ACTION_DELETE, SystemUser.OBJECT_NAME, obj.getId(),
		"", "", null, obj.getUsername());

	// Do service.
	this.systemUserDAO.delete(id);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateDelete(SystemUser.OBJECT_NAME, obj.getUsername());
    }

    /**
     * List all system users.
     */
    @Override
    @Transactional
    public List<SystemUser> list() {
	// Only company admins are allowed to list users.
	if (!this.authHelper.isCompanyAdmin()) {
	    return new ArrayList<SystemUser>();
	}

	AuthenticationToken token = this.authHelper.getAuth();

	// Log.
	this.messageHelper.nonAuditableListNoAssoc(AuditAction.ACTION_LIST, SystemUser.OBJECT_NAME);

	if (token.isSuperAdmin()) {

	    // Return list.
	    return this.systemUserDAO.list(null);
	}

	Company co = token.getCompany();

	// Return list.
	return this.systemUserDAO.list(co.getId());
    }

    /**
     * Search the database given a name.
     */
    @Override
    @Transactional
    public SystemUser searchDatabase(String name) {
	return this.systemUserDAO.searchDatabase(name);
    }

}
