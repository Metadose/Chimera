package com.cebedo.pmsys.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.UserSecAccessBean;
import com.cebedo.pmsys.bean.UserSecRoleBean;
import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.controller.LoginLogoutController;
import com.cebedo.pmsys.dao.SecurityAccessDAO;
import com.cebedo.pmsys.dao.SecurityRoleDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SecurityAccess;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;
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
    private SecurityAccessDAO securityAccessDAO;
    private SecurityRoleDAO securityRoleDAO;
    private SystemConfigurationDAO systemConfigurationDAO;

    private static final String ROOT_USER_NAME = "root";
    private static final String ROOT_PASSWORD = "0p;/9ol.8ik,";

    public void setSystemConfigurationDAO(
	    SystemConfigurationDAO systemConfigurationDAO) {
	this.systemConfigurationDAO = systemConfigurationDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    public void setSecurityAccessDAO(SecurityAccessDAO accessDAO) {
	this.securityAccessDAO = accessDAO;
    }

    public void setSecurityRoleDAO(SecurityRoleDAO roleDAO) {
	this.securityRoleDAO = roleDAO;
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
	    rootUser.setPassword(this.authHelper.encodePassword(ROOT_PASSWORD,
		    rootUser));
	    rootUser.setSuperAdmin(true);
	    List<SecurityAccess> allAccess = this.securityAccessDAO.list();
	    List<SecurityRole> allRoles = this.securityRoleDAO.list();
	    rootUser.setSecurityAccess(new HashSet<SecurityAccess>(allAccess));
	    rootUser.setSecurityRoles(new HashSet<SecurityRole>(allRoles));

	    // Create the account.
	    this.systemUserDAO.create(rootUser);

	    // Update the config value.
	    LoginLogoutController.appInit = true;
	    SystemConfiguration appInit = this.systemConfigurationDAO
		    .getByName(SystemConstants.CONFIG_ROOT_INIT);
	    appInit.setValue("1");
	    this.systemConfigurationDAO.merge(appInit);

	    // Log the results.
	    String message = "Super admin " + ROOT_USER_NAME
		    + " account was generated.";
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
	String encPassword = this.authHelper.encodePassword(
		systemUser.getPassword(), systemUser);
	systemUser.setPassword(encPassword);

	// Construct the staff of the user.
	Staff staff = new Staff();

	// Set the user company.
	// If it's already carrying a company ID,
	// use it.
	if (systemUser.getCompanyID() != null
		&& systemUser.getCompany() == null) {
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
	    this.messageHelper.sendAction(AuditAction.CREATE, systemUser);

	    // Do service.
	    // Create the user and staff.
	    // Create the objects first.
	    this.systemUserDAO.create(systemUser);
	    this.staffDAO.create(staff);

	    // Then link them together.
	    systemUser.setStaff(staff);
	    this.systemUserDAO.update(systemUser);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    SystemUser.OBJECT_NAME, systemUser.getUsername());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		SystemUser.OBJECT_NAME, systemUser.getId(),
		systemUser.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateCreate(SystemUser.OBJECT_NAME,
		systemUser.getUsername());
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
	    logger.info(this.logHelper.logGetObject(auth,
		    SystemUser.OBJECT_NAME, id, obj.getUsername()));

	    // Return obj.
	    return obj;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET,
		SystemUser.OBJECT_NAME, id, obj.getUsername()));

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
	    logger.info(this.logHelper.logGetObjectWithProperty(auth,
		    SystemUser.OBJECT_NAME, "securityAccess,securityRoles", id,
		    obj.getUsername()));

	    // Return obj.
	    return obj;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.GET_PARTIAL, SystemUser.OBJECT_NAME, id,
		obj.getUsername()));

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
	    logger.info(this.logHelper.logGetObject(auth,
		    SystemUser.OBJECT_NAME, id, user.getUsername()));

	    // Return obj.
	    return user;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET,
		SystemUser.OBJECT_NAME, id, user.getUsername()));

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
	String encPassword = this.authHelper.encodePassword(user.getPassword(),
		user);
	user.setPassword(encPassword);

	if (this.authHelper.isActionAuthorized(user)) {

	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE, user);

	    // Do service.
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(
		    SystemUser.OBJECT_NAME, user.getUsername());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		SystemUser.OBJECT_NAME, user.getId(), user.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(SystemUser.OBJECT_NAME,
		user.getUsername());
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
		this.messageHelper.sendAction(AuditAction.UPDATE, user);
	    }

	    // Do service.
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(
		    SystemUser.OBJECT_NAME, user.getUsername());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		SystemUser.OBJECT_NAME, user.getId(), user.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(SystemUser.OBJECT_NAME,
		user.getUsername());
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
	    this.messageHelper.sendAction(AuditAction.DELETE, obj);

	    // Do service.
	    this.systemUserDAO.delete(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(
		    SystemUser.OBJECT_NAME, obj.getUsername());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		SystemUser.OBJECT_NAME, obj.getId(), obj.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(SystemUser.OBJECT_NAME,
		obj.getUsername());
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
	    logger.info(this.logHelper.logListAsSuperAdmin(token,
		    SystemUser.OBJECT_NAME));

	    // Return list.
	    return this.systemUserDAO.list(null);
	}

	Company co = token.getCompany();

	// Log info.
	logger.info(this.logHelper.logListFromCompany(token,
		SystemUser.OBJECT_NAME, co));

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

    /**
     * Assign a security access to a user.
     */
    @Override
    @Transactional
    public String assignSecurityAccess(SystemUser user,
	    UserSecAccessBean secAccBean) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Set<SecurityAccess> secAccList = user.getSecurityAccess();
	SecurityAccess secAcc = this.securityAccessDAO.getByID(secAccBean
		.getSecurityAccessID());

	if (this.authHelper.isActionAuthorized(user)) {

	    // Update the list of access.
	    secAccList.add(secAcc);

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, user,
		    secAcc);

	    // Do service.
	    user.setSecurityAccess(secAccList);

	    // Update the user object.
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(
		    SecurityAccess.OBJECT_NAME, secAcc.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		SystemUser.OBJECT_NAME, user.getId(), user.getUsername()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		SecurityAccess.OBJECT_NAME, secAcc.getId(), secAcc.getLabel()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(
		SecurityAccess.OBJECT_NAME, secAcc.getLabel());
    }

    /**
     * Assign a security role to a user.
     */
    @Override
    @Transactional
    public String assignSecurityRole(SystemUser user,
	    UserSecRoleBean secRoleBean) {

	// Update the list.
	AuthenticationToken auth = this.authHelper.getAuth();
	Set<SecurityRole> secRoleList = user.getSecurityRoles();
	SecurityRole secRole = this.securityRoleDAO.getByID(secRoleBean
		.getSecurityRoleID());

	if (this.authHelper.isActionAuthorized(user)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, user,
		    secRole);

	    // Do service.
	    // Update the user object.
	    secRoleList.add(secRole);
	    user.setSecurityRoles(secRoleList);
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(
		    SecurityRole.OBJECT_NAME, secRole.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		SystemUser.OBJECT_NAME, user.getId(), user.getUsername()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		SecurityAccess.OBJECT_NAME, secRole.getId(), secRole.getLabel()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(SecurityRole.OBJECT_NAME,
		secRole.getLabel());
    }

    /**
     * Unassign a security access.
     */
    @Override
    @Transactional
    public String unassignSecurityAccess(SystemUser user, long secAccID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SecurityAccess secAcc = this.securityAccessDAO.getByID(secAccID);

	if (this.authHelper.isActionAuthorized(user)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN, user,
		    secAcc);

	    // Do service.
	    Set<SecurityAccess> secAccList = user.getSecurityAccess();

	    // Loop through each current access.
	    SecurityAccess accessToRemove = null;
	    for (SecurityAccess access : secAccList) {

		// If an access is equal to access to remove.
		if (access.getId() == secAccID) {
		    accessToRemove = access;
		    break;
		}
	    }

	    // Remove here since can't remove while loop through list.
	    if (accessToRemove != null) {
		secAccList.remove(accessToRemove);
	    }

	    // Update the list and the user obj.
	    user.setSecurityAccess(secAccList);
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassign(
		    SecurityAccess.OBJECT_NAME, secAcc.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		SystemUser.OBJECT_NAME, user.getId(), user.getUsername()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		SecurityAccess.OBJECT_NAME, secAcc.getId(), secAcc.getLabel()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassign(
		SecurityAccess.OBJECT_NAME, secAcc.getLabel());
    }

    /**
     * Unassign a security role.
     */
    @Override
    @Transactional
    public String unassignSecurityRole(SystemUser user, long secRoleID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Set<SecurityRole> secRoleList = user.getSecurityRoles();
	SecurityRole secRole = this.securityRoleDAO.getByID(secRoleID);

	if (this.authHelper.isActionAuthorized(user)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN, user,
		    secRole);

	    // Do service.
	    // Loop through each current role.
	    SecurityRole roleToRemove = null;
	    for (SecurityRole role : secRoleList) {

		// If an access is equal to access to remove.
		if (role.getId() == secRoleID) {
		    roleToRemove = role;
		    break;
		}
	    }

	    // Remove here since can't remove while loop through list.
	    if (roleToRemove != null) {
		secRoleList.remove(roleToRemove);
	    }

	    // Update the list and the user obj.
	    user.setSecurityRoles(secRoleList);
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassign(
		    SecurityRole.OBJECT_NAME, secRole.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		SystemUser.OBJECT_NAME, user.getId(), user.getUsername()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		SecurityRole.OBJECT_NAME, secRole.getId(), secRole.getLabel()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassign(
		SecurityRole.OBJECT_NAME, secRole.getLabel());
    }

    /**
     * Unassign all security access from a user.
     */
    @Override
    @Transactional
    public String unassignAllSecurityAccess(SystemUser user) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(user)) {

	    // Log and notify.
	    this.messageHelper
		    .sendUnassignAll(SecurityAccess.OBJECT_NAME, user);

	    // Do service.
	    // Update the list of access.
	    user.setSecurityAccess(null);

	    // Update the user object.
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS
		    .generateUnassignAll(SecurityAccess.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, SystemUser.OBJECT_NAME, user.getId(),
		user.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED
		.generateUnassignAll(SecurityAccess.OBJECT_NAME);
    }

    /**
     * Unassign all security roles.
     */
    @Override
    @Transactional
    public String unassignAllSecurityRoles(SystemUser user) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(user)) {

	    // Log and notify.
	    this.messageHelper.sendUnassignAll(SecurityRole.OBJECT_NAME, user);

	    // Do service.
	    // Update the list of roles.
	    // Update the user object.
	    user.setSecurityRoles(null);
	    this.systemUserDAO.update(user);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS
		    .generateUnassignAll(SecurityRole.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, SystemUser.OBJECT_NAME, user.getId(),
		user.getUsername()));

	// Return fail.
	return AlertBoxGenerator.FAILED
		.generateUnassignAll(SecurityRole.OBJECT_NAME);
    }

}
