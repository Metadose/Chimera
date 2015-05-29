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
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SecurityAccess;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

@Service
public class SystemUserServiceImpl implements SystemUserService {

	private AuthHelper authHelper = new AuthHelper();
	private SystemUserDAO systemUserDAO;
	private StaffDAO staffDAO;
	private SecurityAccessDAO securityAccessDAO;
	private SecurityRoleDAO securityRoleDAO;
	private SystemConfigurationDAO systemConfigurationDAO;
	private static final String ROOT_USER_NAME = "root";
	private static final String ROOT_PASSWORD = "0p;/9ol.8ik,";
	private static Logger logger = Logger
			.getLogger(SystemConstants.LOGGER_SYSTEM_USER);
	private LogHelper logHelper = new LogHelper();

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

	@Override
	@Transactional
	public void create(SystemUser systemUser) {

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
			AuthenticationToken auth = this.authHelper.getAuth();
			Company authCompany = auth.getCompany();
			systemUser.setCompany(authCompany);
			staff.setCompany(authCompany);
		}

		// Create the user and staff.
		// Create the objects first.
		this.systemUserDAO.create(systemUser);
		this.staffDAO.create(staff);

		// Then link them together.
		systemUser.setStaff(staff);
		this.systemUserDAO.update(systemUser);
	}

	@Override
	@Transactional
	public SystemUser getByID(long id, boolean override) {
		SystemUser obj = this.systemUserDAO.getByID(id);
		if (override || this.authHelper.isActionAuthorized(obj)) {
			return obj;
		}
		return new SystemUser();
	}

	@Override
	@Transactional
	public SystemUser getWithSecurityByID(long id) {
		SystemUser obj = this.systemUserDAO.getWithSecurityByID(id);
		if (this.authHelper.isActionAuthorized(obj)) {
			return obj;
		}
		return new SystemUser();
	}

	@Override
	@Transactional
	public SystemUser getByID(long id) {
		return getByID(id, false);
	}

	@Override
	@Transactional
	public void update(SystemUser user) {
		String encPassword = this.authHelper.encodePassword(user.getPassword(),
				user);
		user.setPassword(encPassword);
		if (this.authHelper.isActionAuthorized(user)) {
			this.systemUserDAO.update(user);
		}
	}

	@Override
	@Transactional
	public void update(SystemUser user, boolean systemOverride) {
		if (systemOverride || this.authHelper.isActionAuthorized(user)) {
			this.systemUserDAO.update(user);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		SystemUser obj = this.systemUserDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(obj)) {
			this.systemUserDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<SystemUser> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.systemUserDAO.list(null);
		}
		return this.systemUserDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public SystemUser searchDatabase(String name) {
		return this.systemUserDAO.searchDatabase(name);
	}

	@Override
	@Transactional
	public void assignSecurityAccess(SystemUser user,
			UserSecAccessBean secAccBean) {
		Set<SecurityAccess> secAccList = user.getSecurityAccess();

		// Update the list of access.
		SecurityAccess secAcc = this.securityAccessDAO.getByID(secAccBean
				.getSecurityAccessID());
		secAccList.add(secAcc);
		user.setSecurityAccess(secAccList);

		// Update the user object.
		this.systemUserDAO.update(user);
	}

	@Override
	@Transactional
	public void assignSecurityRole(SystemUser user, UserSecRoleBean secRoleBean) {
		Set<SecurityRole> secRoleList = user.getSecurityRoles();

		// Update the list.
		SecurityRole secRole = this.securityRoleDAO.getByID(secRoleBean
				.getSecurityRoleID());
		secRoleList.add(secRole);
		user.setSecurityRoles(secRoleList);

		// Update the user object.
		this.systemUserDAO.update(user);
	}

	@Override
	@Transactional
	public void unassignSecurityAccess(SystemUser user, long secAccID) {
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
	}

	@Override
	@Transactional
	public void unassignSecurityRole(SystemUser user, long secRoleID) {
		Set<SecurityRole> secRoleList = user.getSecurityRoles();

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
	}

	@Override
	@Transactional
	public void unassignAllSecurityAccess(SystemUser user) {
		// Update the list of access.
		user.setSecurityAccess(null);

		// Update the user object.
		this.systemUserDAO.update(user);
	}

	@Override
	@Transactional
	public void unassignAllSecurityRoles(SystemUser user) {
		// Update the list of roles.
		user.setSecurityRoles(null);

		// Update the user object.
		this.systemUserDAO.update(user);
	}

}
