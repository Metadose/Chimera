package com.cebedo.pmsys.security.securitygroup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.security.securitygroup.dao.SecurityGroupDAO;
import com.cebedo.pmsys.security.securitygroup.model.SecurityGroup;

@Service
public class SecurityGroupServiceImpl implements SecurityGroupService {

	private SecurityGroupDAO securityGroupDAO;

	public void setSecurityGroupDAO(SecurityGroupDAO securityGroupDAO) {
		this.securityGroupDAO = securityGroupDAO;
	}

	@Override
	@Transactional
	public void create(SecurityGroup securityGroup) {
		this.securityGroupDAO.create(securityGroup);
	}

	@Override
	@Transactional
	public SecurityGroup getByID(long id) {
		SecurityGroup grp = this.securityGroupDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(grp)) {
			return grp;
		}
		return new SecurityGroup();
	}

	@Override
	@Transactional
	public void update(SecurityGroup securityGroup) {
		if (AuthUtils.isActionAuthorized(securityGroup)) {
			this.securityGroupDAO.update(securityGroup);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		SecurityGroup grp = this.securityGroupDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(grp)) {
			this.securityGroupDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<SecurityGroup> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.securityGroupDAO.list(null);
		}
		return this.securityGroupDAO.list(token.getCompany().getId());
	}
}
