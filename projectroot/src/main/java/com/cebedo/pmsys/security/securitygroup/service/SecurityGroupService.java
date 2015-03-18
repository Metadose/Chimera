package com.cebedo.pmsys.security.securitygroup.service;

import java.util.List;

import com.cebedo.pmsys.security.securitygroup.model.SecurityGroup;

public interface SecurityGroupService {

	public void create(SecurityGroup securityGroup);

	public SecurityGroup getByID(long id);

	public void update(SecurityGroup securityGroup);

	public void delete(long id);

	public List<SecurityGroup> list();

}
