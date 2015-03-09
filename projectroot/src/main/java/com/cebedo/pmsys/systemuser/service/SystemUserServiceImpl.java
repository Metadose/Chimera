package com.cebedo.pmsys.systemuser.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.systemuser.dao.SystemUserDAO;
import com.cebedo.pmsys.systemuser.model.SystemUser;

@Service
public class SystemUserServiceImpl implements SystemUserService {

	private SystemUserDAO systemUserDAO;

	public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
		this.systemUserDAO = systemUserDAO;
	}

	@Override
	@Transactional
	public void create(SystemUser systemUser) {
		this.systemUserDAO.create(systemUser);
		AuthenticationToken auth = AuthUtils.getAuth();
		Company authCompany = auth.getCompany();
		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			systemUser.setCompany(authCompany);
			this.systemUserDAO.update(systemUser);
		}
	}

	@Override
	@Transactional
	public SystemUser getByID(long id) {
		SystemUser obj = this.systemUserDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(obj)) {
			return obj;
		}
		return new SystemUser();
	}

	@Override
	@Transactional
	public void update(SystemUser systemUser) {
		if (AuthUtils.isActionAuthorized(systemUser)) {
			this.systemUserDAO.update(systemUser);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		SystemUser obj = this.systemUserDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(obj)) {
			this.systemUserDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<SystemUser> list() {
		AuthenticationToken token = AuthUtils.getAuth();
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
}
