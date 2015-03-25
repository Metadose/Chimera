package com.cebedo.pmsys.security.securityrole.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthHelper;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.security.securityrole.dao.SecurityRoleDAO;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;

@Service
public class SecurityRoleServiceImpl implements SecurityRoleService {

	private AuthHelper authHelper = new AuthHelper();
	private SecurityRoleDAO securityRoleDAO;

	public void setSecurityRoleDAO(SecurityRoleDAO securityRoleDAO) {
		this.securityRoleDAO = securityRoleDAO;
	}

	@Override
	@Transactional
	public void create(SecurityRole securityRole) {
		this.securityRoleDAO.create(securityRole);
	}

	@Override
	@Transactional
	public SecurityRole getByID(long id) {
		SecurityRole obj = this.securityRoleDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(obj)) {
			return obj;
		}
		return new SecurityRole();
	}

	@Override
	@Transactional
	public void update(SecurityRole securityRole) {
		if (this.authHelper.isActionAuthorized(securityRole)) {
			this.securityRoleDAO.update(securityRole);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		SecurityRole obj = this.securityRoleDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(obj)) {
			this.securityRoleDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<SecurityRole> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.securityRoleDAO.list(null);
		}
		return this.securityRoleDAO.list(token.getCompany().getId());
	}
}
