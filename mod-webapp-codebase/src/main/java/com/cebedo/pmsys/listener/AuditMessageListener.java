package com.cebedo.pmsys.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQObjectMessage;

import com.cebedo.pmsys.bean.JMSMessage;
import com.cebedo.pmsys.dao.AuditLogDAO;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class AuditMessageListener implements MessageListener {

    public final static String MESSAGE_DESTINATION = "system.audit";

    private AuditLogDAO auditLogDAO;

    public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
	this.auditLogDAO = auditLogDAO;
    }

    @Override
    @Transactional
    public void onMessage(Message message) {

	if (message instanceof ActiveMQObjectMessage) {
	    JMSMessage sysMessage;
	    try {
		// Get the contents.
		sysMessage = (JMSMessage) ((ActiveMQObjectMessage) message).getObject();

		// Get the user details.
		// Get action details.
		AuthenticationToken auth = sysMessage.getAuth();
		Company company = auth == null ? null : auth.getCompany();
		SystemUser user = auth == null ? null : auth.getUser();
		String ipAddr = (auth == null || auth.getIpAddress().isEmpty())
			? sysMessage.getIpAddress() : auth.getIpAddress();
		int actionID = sysMessage.getAuditAction().id();
		String objName = sysMessage.getObjectName();
		long objID = sysMessage.getObjectID();

		// Project reference.
		long projectID = sysMessage.getProjectID();

		// Do the audit.
		AuditLog audit = null;
		if (projectID == 0) {
		    audit = new AuditLog(actionID, user, ipAddr, auth == null ? null : company, objName,
			    objID);
		} else {
		    audit = new AuditLog(actionID, user, ipAddr, auth == null ? null : company, objName,
			    objID, projectID);
		}
		this.auditLogDAO.create(audit);

		// If a slave exists, create separate entry.
		String associatedObjName = sysMessage.getAssocObjectName();
		Long associatedObjID = sysMessage.getAssocObjectID();

		if (associatedObjName != null && !associatedObjName.isEmpty()) {
		    AuditLog assocAudit = new AuditLog(actionID, user, ipAddr,
			    auth == null ? null : company, associatedObjName, associatedObjID);
		    this.auditLogDAO.create(assocAudit);
		}
	    } catch (JMSException e) {
		e.printStackTrace();
	    }
	}

	else {
	    throw new IllegalArgumentException("MessageThread must be of type SystemMessage");
	}
    }
}