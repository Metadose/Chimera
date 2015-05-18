package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.SecurityRoleDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.token.AuthenticationToken;

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
		return this.securityRoleDAO.list();
	}
}
