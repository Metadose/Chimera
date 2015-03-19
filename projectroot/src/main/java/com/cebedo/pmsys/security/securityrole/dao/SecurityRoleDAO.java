package com.cebedo.pmsys.security.securityrole.dao;

import java.util.List;

import com.cebedo.pmsys.security.securityrole.model.SecurityRole;

public interface SecurityRoleDAO {

	public void create(SecurityRole securityRole);

	public SecurityRole getByID(long id);

	public void update(SecurityRole securityRole);

	public void delete(long id);

	public List<SecurityRole> list(Long companyID);

}
