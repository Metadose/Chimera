package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.SecurityRole;

public interface SecurityRoleDAO {

	public void create(SecurityRole securityRole);

	public SecurityRole getByID(long id);

	public void update(SecurityRole securityRole);

	public void delete(long id);

	public List<SecurityRole> list();

}
