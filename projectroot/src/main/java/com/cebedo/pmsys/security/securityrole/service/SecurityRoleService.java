package com.cebedo.pmsys.security.securityrole.service;

import java.util.List;

import com.cebedo.pmsys.security.securityrole.model.SecurityRole;

public interface SecurityRoleService {

	public void create(SecurityRole securityRole);

	public SecurityRole getByID(long id);

	public void update(SecurityRole securityRole);

	public void delete(long id);

	public List<SecurityRole> list();

}
