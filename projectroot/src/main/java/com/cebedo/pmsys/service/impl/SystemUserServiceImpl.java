package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.controller.LoginLogoutController;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
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

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private static Logger logger = Logger.getLogger(SystemUser.OBJECT_NAME);

    private SystemUserDAO systemUserDAO;
    private StaffDAO staffDAO;
    private SystemConfigurationDAO systemConfigurationDAO;

    private static final String ROOT_USER_NAME = "root";
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

	    // Update the config value.
	    LoginLogoutController.appInit = true;
	    SystemConfiguration appInit = this.systemConfigurationDAO
		    .getByName(SystemConstants.CONFIG_ROOT_INIT);
	    appInit.setValue("1");
	    this.systemConfigurationDAO.merge(appInit);

	    // Log the results.
	    String message = "Super admin " + ROOT_USER_NAME + " account was generated.";
	    logger.warn(this.logHelper.logMessage(message));
	}
    }

    /**
     * Create a new system user.
     */
    @Override
    @Transactional
    public String create(SystemUser systemUser) {

	AuthenticationToken auth = this.authHelper.getAuth();

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
	} else {
	    // Else, get it somewhere else.
	    Company authCompany = auth.getCompany();
	    systemUser.setCompany(authCompany);
	    staff.setCompany(authCompany);
	}

	if (this.authHelper.isActionAuthorized(systemUser)) {

	    // Log and notify.
	    this.messageHelper.send(AuditAction.CREATE, SystemUser.OBJECT_NAME, systemUser.getId());

	    // Do service.
	    // Create the user and staff.
	    // Create the objects first.
	    this.systemUserDAO.create(systemUser);
	    this.staffDAO.create(staff);

	    // Then link them together.
	    systemUser.setStaff(staff);
	    this.systemUserDAO.update(systemUser);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateCreate(SystemUser.OBJECT_NAME,
		    systemUser.getUsername());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE, SystemUser.OBJECT_NAME,
		systemUser.getId(), systemUser.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateCreate(SystemUser.OBJECT_NAME, systemUser.getUsername());
    }

    /**
     * Get an object by id.
     */
    @Override
    @Transactional
    public SystemUser getByID(long id, boolean override) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemUser obj = this.systemUserDAO.getByID(id);

	if (override || this.authHelper.isActionAuthorized(obj)) {
	    // Log info.
	    logger.info(this.logHelper.logGetObject(auth, SystemUser.OBJECT_NAME, id, obj.getUsername()));

	    // Return obj.
	    return obj;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET, SystemUser.OBJECT_NAME, id,
		obj.getUsername()));

	// Return empty.
	return new SystemUser();
    }

    /**
     * Get object by id, with security.
     */
    @Override
    @Transactional
    public SystemUser getWithSecurityByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemUser obj = this.systemUserDAO.getWithSecurityByID(id);

	if (this.authHelper.isActionAuthorized(obj)) {
	    // Log info.
	    logger.info(this.logHelper.logGetObjectWithProperty(auth, SystemUser.OBJECT_NAME,
		    "securityAccess,securityRoles", id, obj.getUsername()));

	    // Return obj.
	    return obj;
	}

	// Log warn.
	// logger.warn(this.logHelper.logUnauthorized(auth,
	// AuditAction.GET_PARTIAL,
	// SystemUser.OBJECT_NAME, id, obj.getUsername()));

	// Return empty.
	return new SystemUser();
    }

    /**
     * Get obj by id.
     */
    @Override
    @Transactional
    public SystemUser getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemUser user = getByID(id, false);

	if (this.authHelper.isActionAuthorized(user)) {
	    // Log info.
	    logger.info(this.logHelper.logGetObject(auth, SystemUser.OBJECT_NAME, id, user.getUsername()));

	    // Return obj.
	    return user;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET, SystemUser.OBJECT_NAME, id,
		user.getUsername()));

	// Return empty.
	return new SystemUser();
    }

    /**
     * Update a user.
     */
    @Override
    @Transactional
    public String update(SystemUser user) {
	AuthenticationToken auth = this.authHelper.getAuth();
	String encPassword = this.authHelper.encodePassword(user.getPassword(), user);
	user.setPassword(encPassword);

	if (this.authHelper.isActionAuthorized(user)) {

	    // Log and notify.
	    this.messageHelper.send(AuditAction.UPDATE, SystemUser.OBJECT_NAME, user.getId());

	    // Do service.
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(SystemUser.OBJECT_NAME, user.getUsername());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE, SystemUser.OBJECT_NAME,
		user.getId(), user.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(SystemUser.OBJECT_NAME, user.getUsername());
    }

    /**
     * Update a user.
     */
    @Override
    @Transactional
    public String update(SystemUser user, boolean systemOverride) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (systemOverride || this.authHelper.isActionAuthorized(user)) {

	    if (!systemOverride) {
		// Log and notify.
		this.messageHelper.send(AuditAction.UPDATE, SystemUser.OBJECT_NAME, user.getId());
	    }

	    // Do service.
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(SystemUser.OBJECT_NAME, user.getUsername());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE, SystemUser.OBJECT_NAME,
		user.getId(), user.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(SystemUser.OBJECT_NAME, user.getUsername());
    }

    /**
     * Delete a user given an id.
     */
    @Override
    @Transactional
    public String delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemUser obj = this.systemUserDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(obj)) {

	    // Log and notify.
	    this.messageHelper.send(AuditAction.UPDATE, SystemUser.OBJECT_NAME, obj.getId());

	    // Do service.
	    this.systemUserDAO.delete(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(SystemUser.OBJECT_NAME, obj.getUsername());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE, SystemUser.OBJECT_NAME,
		obj.getId(), obj.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(SystemUser.OBJECT_NAME, obj.getUsername());
    }

    /**
     * List all system users.
     */
    @Override
    @Transactional
    public List<SystemUser> list() {
	AuthenticationToken token = this.authHelper.getAuth();

	if (token.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListAsSuperAdmin(token, SystemUser.OBJECT_NAME));

	    // Return list.
	    return this.systemUserDAO.list(null);
	}

	Company co = token.getCompany();

	// Log info.
	logger.info(this.logHelper.logListFromCompany(token, SystemUser.OBJECT_NAME, co));

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
