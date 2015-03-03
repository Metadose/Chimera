package com.cebedo.pmsys.systemuser.dao;

import java.util.List;

import com.cebedo.pmsys.systemuser.model.SystemUser;

public interface SystemUserDAO {

	public SystemUser searchDatabase(String username);

	public List<SystemUser> list();

	public void create(SystemUser user);

	public void update(SystemUser user);

	public void delete(long id);

	public SystemUser getByID(long id);

}
