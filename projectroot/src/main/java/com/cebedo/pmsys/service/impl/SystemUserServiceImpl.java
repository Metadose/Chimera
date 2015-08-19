package com.cebedo.pmsys.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.controller.LoginLogoutController;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.service.SystemUserService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private static final String PATTERN_USERNAME = "^[a-z0-9_-]{4,32}$";
    private static final String PATTERN_PASSWORD = "^(?=.*\\d).{8,16}$";

    private final Pattern patternUsername = Pattern.compile(PATTERN_USERNAME);
    private final Pattern patternPassword = Pattern.compile(PATTERN_PASSWORD);
    private Matcher matcher;

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();

    private SystemUserDAO systemUserDAO;
    private StaffDAO staffDAO;
    private SystemConfigurationDAO systemConfigurationDAO;

    private static final String ROOT_USER_NAME = "root_098";
    private static final String ROOT_PASSWORD = "0p;/9ol.8ik,";

    public void setSystemConfigurationDAO(SystemConfigurationDAO systemConfigurationDAO) {
	this.systemConfigurationDAO = systemConfigurationDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    /**
     * Initialize the root account, if non-existent.
     */
    @Override
    @Transactional
    public void initRoot() {
	// Check if the root account exists.
	// If not, create it.
	try {
	    this.systemUserDAO.searchDatabase(ROOT_USER_NAME);
	} catch (Exception e) {
	    // Setup the root account.
	    SystemUser rootUser = new SystemUser();
	    rootUser.setUsername(ROOT_USER_NAME);
	    rootUser.setPassword(this.authHelper.encodePassword(ROOT_PASSWORD, rootUser));
	    rootUser.setSuperAdmin(true);

	    // Create the account.
	    this.systemUserDAO.create(rootUser);

	    // Log.
	    this.messageHelper.send(AuditAction.ACTION_CREATE, SystemUser.OBJECT_NAME, rootUser.getId(),
		    "Super Admin");

	    // Update the config value.
	    LoginLogoutController.appInit = true;
	    SystemConfiguration appInit = this.systemConfigurationDAO
		    .getByName(ConstantsSystem.CONFIG_ROOT_INIT);
	    appInit.setValue("1");
	    this.systemConfigurationDAO.merge(appInit);

	    // Log the results.
	    this.messageHelper.send(AuditAction.ACTION_UPDATE, SystemConfiguration.OBJECT_NAME,
		    appInit.getId());
	}
    }

    /**
     * Create a new system user.
     */
    @Override
    @Transactional
    public String create(SystemUser systemUser) {

	// Check if the user name is valid.
	this.matcher = this.patternUsername.matcher(systemUser.getUsername());
	if (!this.matcher.matches()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_USERNAME_INVALID_PATTERN);
	}

	// Check if the password is valid.
	this.matcher = this.patternPassword.matcher(systemUser.getPassword());
	if (!this.matcher.matches()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PASSWORD_INVALID_PATTERN);
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
		this.messageHelper.unauthorized(SystemUser.OBJECT_NAME, systemUser.getId());
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
	this.messageHelper.send(AuditAction.ACTION_CREATE, SystemUser.OBJECT_NAME, systemUser.getId());

	// Then link them together.
	systemUser.setStaff(staff);
	this.systemUserDAO.update(systemUser);

	// Return success.
	return AlertBoxGenerator.SUCCESS
		.generateCreate(SystemUser.OBJECT_NAME, systemUser.getUsername());
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
		this.messageHelper.unauthorized(SystemUser.OBJECT_NAME, obj.getId());
		return new SystemUser();
	    }

	    // Log.
	    this.messageHelper.send(AuditAction.ACTION_GET, SystemUser.OBJECT_NAME, obj.getId());
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
	    this.messageHelper.unauthorized(SystemUser.OBJECT_NAME, obj.getId());
	    return new SystemUser();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, SystemUser.OBJECT_NAME, obj.getId());

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
	    this.messageHelper.unauthorized(SystemUser.OBJECT_NAME, obj.getId());
	    return new SystemUser();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, SystemUser.OBJECT_NAME, obj.getId());

	// Return obj.
	return obj;
    }

    /**
     * Update a user.
     */
    @Override
    @Transactional
    public String update(SystemUser user) {

	// Check if the user name is valid.
	this.matcher = this.patternUsername.matcher(user.getUsername());
	if (!this.matcher.matches()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_USERNAME_INVALID_PATTERN);
	}

	// Check if the password is valid.
	this.matcher = this.patternPassword.matcher(user.getPassword());
	if (!this.matcher.matches()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PASSWORD_INVALID_PATTERN);
	}

	String encPassword = this.authHelper.encodePassword(user.getPassword(), user);
	user.setPassword(encPassword);

	// Security check.
	if (!this.authHelper.isActionAuthorized(user)) {
	    this.messageHelper.unauthorized(SystemUser.OBJECT_NAME, user.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, SystemUser.OBJECT_NAME, user.getId());

	// Do service.
	this.systemUserDAO.update(user);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpdate(SystemUser.OBJECT_NAME, user.getUsername());
    }

    /**
     * Update a user.
     */
    @Override
    @Transactional
    public String update(SystemUser user, boolean systemOverride) {

	// Check if the user name is valid.
	this.matcher = this.patternUsername.matcher(user.getUsername());
	if (!this.matcher.matches()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_USERNAME_INVALID_PATTERN);
	}

	// Check if the password is valid.
	this.matcher = this.patternPassword.matcher(user.getPassword());
	if (!this.matcher.matches()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PASSWORD_INVALID_PATTERN);
	}

	if (!systemOverride) {
	    // Security check.
	    if (!this.authHelper.isActionAuthorized(user)) {
		this.messageHelper.unauthorized(SystemUser.OBJECT_NAME, user.getId());
		return AlertBoxGenerator.ERROR;
	    }

	    // Log.
	    this.messageHelper.send(AuditAction.ACTION_UPDATE, SystemUser.OBJECT_NAME, user.getId());
	}

	// Do service.
	this.systemUserDAO.update(user);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpdate(SystemUser.OBJECT_NAME, user.getUsername());
    }

    /**
     * Delete a user given an id.
     */
    @Override
    @Transactional
    public String delete(long id) {
	SystemUser obj = this.systemUserDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(SystemUser.OBJECT_NAME, obj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, SystemUser.OBJECT_NAME, obj.getId());

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
	AuthenticationToken token = this.authHelper.getAuth();

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, SystemUser.OBJECT_NAME);

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
