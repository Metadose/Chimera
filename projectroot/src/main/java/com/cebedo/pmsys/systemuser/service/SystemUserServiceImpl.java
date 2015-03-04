package com.cebedo.pmsys.systemuser.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	}

	@Override
	@Transactional
	public SystemUser getByID(long id) {
		return this.systemUserDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(SystemUser systemUser) {
		this.systemUserDAO.update(systemUser);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.systemUserDAO.delete(id);
	}

	@Override
	@Transactional
	public List<SystemUser> list() {
		return this.systemUserDAO.list();
	}

	@Override
	@Transactional
	public SystemUser searchDatabase(String name) {
		return this.systemUserDAO.searchDatabase(name);
	}
}
