package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.SecurityAccess;

public interface SecurityAccessDAO {

	public void create(SecurityAccess securityAccess);

	public SecurityAccess getByID(long id);

	public void update(SecurityAccess securityAccess);

	public void delete(long id);

	public List<SecurityAccess> list();

}
