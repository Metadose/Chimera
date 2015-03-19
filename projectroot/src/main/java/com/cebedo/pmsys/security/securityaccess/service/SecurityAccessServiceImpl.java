package com.cebedo.pmsys.security.securityaccess.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.security.securityaccess.dao.SecurityAccessDAO;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;

@Service
public class SecurityAccessServiceImpl implements SecurityAccessService {

	private SecurityAccessDAO securityAccessDAO;

	public void setSecurityAccessDAO(SecurityAccessDAO securityAccessDAO) {
		this.securityAccessDAO = securityAccessDAO;
	}

	@Override
	@Transactional
	public void create(SecurityAccess securityAccess) {
		this.securityAccessDAO.create(securityAccess);
	}

	@Override
	@Transactional
	public SecurityAccess getByID(long id) {
		SecurityAccess obj = this.securityAccessDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(obj)) {
			return obj;
		}
		return new SecurityAccess();
	}

	@Override
	@Transactional
	public void update(SecurityAccess securityAccess) {
		if (AuthUtils.isActionAuthorized(securityAccess)) {
			this.securityAccessDAO.update(securityAccess);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		SecurityAccess obj = this.securityAccessDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(obj)) {
			this.securityAccessDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<SecurityAccess> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.securityAccessDAO.list(null);
		}
		return this.securityAccessDAO.list(token.getCompany().getId());
	}
}
