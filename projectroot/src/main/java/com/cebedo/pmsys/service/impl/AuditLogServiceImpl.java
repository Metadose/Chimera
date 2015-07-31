package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.AuditLogDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.service.AuditLogService;
import com.cebedo.pmsys.token.AuthenticationToken;

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
