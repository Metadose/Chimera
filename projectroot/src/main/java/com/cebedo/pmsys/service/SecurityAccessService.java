package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.SecurityAccess;

public interface SecurityAccessService {

	public void create(SecurityAccess securityAccess);

	public SecurityAccess getByID(long id);

	public void update(SecurityAccess securityAccess);

	public void delete(long id);

	public List<SecurityAccess> list();

}
