package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.AuditLogDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.service.AuditLogService;
import com.cebedo.pmsys.token.AuthenticationToken;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
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

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(AuditLog.OBJECT_NAME);
	    return new AuditLog();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, AuditLog.OBJECT_NAME, obj.getId());
	return obj;
    }

    @Override
    @Transactional
    public void delete(long id) {

	AuditLog obj = this.auditLogDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(AuditLog.OBJECT_NAME);
	    return; // TODO Return notification?
	}

	// Do service.
	this.auditLogDAO.delete(id);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, AuditLog.OBJECT_NAME, obj.getId());
    }

    @Override
    @Transactional
    public List<AuditLog> list() {
	AuthenticationToken token = this.authHelper.getAuth();

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, AuditLog.OBJECT_NAME);

	if (token.isSuperAdmin()) {
	    return this.auditLogDAO.list(null);
	}
	return this.auditLogDAO.list(token.getCompany().getId());
    }
}
