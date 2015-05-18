package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.SecurityAccessDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.SecurityAccess;

@Service
public class SecurityAccessServiceImpl implements SecurityAccessService {

	private AuthHelper authHelper = new AuthHelper();
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
		if (this.authHelper.isActionAuthorized(obj)) {
			return obj;
		}
		return new SecurityAccess();
	}

	@Override
	@Transactional
	public void update(SecurityAccess securityAccess) {
		if (this.authHelper.isActionAuthorized(securityAccess)) {
			this.securityAccessDAO.update(securityAccess);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		SecurityAccess obj = this.securityAccessDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(obj)) {
			this.securityAccessDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<SecurityAccess> list() {
		return this.securityAccessDAO.list();
	}
}
