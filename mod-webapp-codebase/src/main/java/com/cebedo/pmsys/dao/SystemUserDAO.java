package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.SystemUser;

public interface SystemUserDAO {

	public SystemUser searchDatabase(String username);

	public List<SystemUser> list(Long companyID);

	public void create(SystemUser user);

	public void update(SystemUser user);

	public void delete(long id);

	public SystemUser getByID(long id);

	public SystemUser getWithSecurityByID(long id);

}
