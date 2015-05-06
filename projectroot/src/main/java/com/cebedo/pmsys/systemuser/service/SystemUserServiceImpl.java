package com.cebedo.pmsys.systemuser.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.security.securityaccess.dao.SecurityAccessDAO;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;
import com.cebedo.pmsys.security.securityrole.dao.SecurityRoleDAO;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.system.bean.UserSecAccessBean;
import com.cebedo.pmsys.system.constants.SystemConstants;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.system.helper.LogHelper;
import com.cebedo.pmsys.system.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.system.login.controller.LoginLogoutController;
import com.cebedo.pmsys.systemconfiguration.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.systemconfiguration.model.SystemConfiguration;
import com.cebedo.pmsys.systemuser.dao.SystemUserDAO;
import com.cebedo.pmsys.systemuser.model.SystemUser;

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
			List<SecurityAccess> allAccess = this.securityAccessDAO.list(null);
			List<SecurityRole> allRoles = this.securityRoleDAO.list(null);
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
			logger.warn(this.logHelper.generateLogMessage(message));
		}
	}

	@Override
	@Transactional
	public void create(SystemUser systemUser) {
		String encPassword = this.authHelper.encodePassword(
				systemUser.getPassword(), systemUser);
		systemUser.setPassword(encPassword);

		this.systemUserDAO.create(systemUser);
		AuthenticationToken auth = this.authHelper.getAuth();
		Company authCompany = auth.getCompany();
		Staff staff = new Staff();

		// If it's already carrying a company ID,
		// use it.
		if (systemUser.getCompanyID() != null
				&& systemUser.getCompany() == null) {
			Company company = new Company(systemUser.getCompanyID());
			systemUser.setCompany(company);
			staff.setCompany(company);
		}
		// Else, get it somewhere else.
		// If I'm not a super admin, use my company.
		// Else, leave the object alone.
		else if (this.authHelper.notNullObjNotSuperAdmin(authCompany)) {
			systemUser.setCompany(authCompany);
			staff.setCompany(authCompany);
		}
		this.staffDAO.create(staff);
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
	public void unassignAllSecurityAccess(SystemUser user) {
		// Update the list of access.
		user.setSecurityAccess(null);

		// Update the user object.
		this.systemUserDAO.update(user);
	}

}
