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
    public List<AuditLog> list() {
	AuthenticationToken token = this.authHelper.getAuth();

	// Log.
	this.messageHelper.nonAuditableListNoAssoc(AuditAction.ACTION_LIST, AuditLog.OBJECT_NAME);

	if (token.isSuperAdmin()) {
	    return this.auditLogDAO.list(null);
	}
	return this.auditLogDAO.list(token.getCompany().getId());
    }
}
