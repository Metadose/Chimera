package com.cebedo.pmsys.security.audit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthHelper;
import com.cebedo.pmsys.security.audit.dao.AuditLogDAO;
import com.cebedo.pmsys.security.audit.model.AuditLog;
import com.cebedo.pmsys.system.login.authentication.AuthenticationToken;

@Service
public class AuditLogServiceImpl implements AuditLogService {

	private AuthHelper authHelper = new AuthHelper();
	private AuditLogDAO auditLogDAO;

	public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
		this.auditLogDAO = auditLogDAO;
	}

	@Override
	@Transactional
	public void create(AuditLog auditLog) {
		this.auditLogDAO.create(auditLog);
	}

	@Override
	@Transactional
	public AuditLog getByID(long id) {
		AuditLog obj = this.auditLogDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(obj)) {
			return obj;
		}
		return new AuditLog();
	}

	@Override
	@Transactional
	public void delete(long id) {
		AuditLog obj = this.auditLogDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(obj)) {
			this.auditLogDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<AuditLog> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.auditLogDAO.list(null);
		}
		return this.auditLogDAO.list(token.getCompany().getId());
	}
}
