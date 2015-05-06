package com.cebedo.pmsys.systemuser.service;

import java.util.List;

import com.cebedo.pmsys.system.bean.UserSecAccessBean;
import com.cebedo.pmsys.systemuser.model.SystemUser;

public interface SystemUserService {

	public void initRoot();

	public List<SystemUser> list();

	public void create(SystemUser user);

	public void update(SystemUser user);

	public void update(SystemUser user, boolean systemOverride);

	public void delete(long id);

	public SystemUser getByID(long id);

	public SystemUser getByID(long id, boolean override);

	public SystemUser searchDatabase(String name);

	public void assignSecurityAccess(SystemUser user,
			UserSecAccessBean secAccBean);

	public void unassignSecurityAccess(SystemUser user, long secAccID);

	public void unassignAllSecurityAccess(SystemUser user);

}
