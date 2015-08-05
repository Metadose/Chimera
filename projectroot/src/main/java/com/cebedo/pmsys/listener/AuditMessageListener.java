package com.cebedo.pmsys.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQObjectMessage;

import com.cebedo.pmsys.bean.SystemMessage;
import com.cebedo.pmsys.dao.AuditLogDAO;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class AuditMessageListener implements MessageListener {

    public final static String MESSAGE_DESTINATION = "system.service.post.audit";

    private AuditLogDAO auditLogDAO;

    public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
	this.auditLogDAO = auditLogDAO;
    }

    @Override
    @Transactional
    public void onMessage(Message message) {

	if (message instanceof ActiveMQObjectMessage) {
	    SystemMessage sysMessage;
	    try {
		// Get the contents.
		sysMessage = (SystemMessage) ((ActiveMQObjectMessage) message).getObject();

		// Get the user details.
		// Get action details.
		AuthenticationToken auth = sysMessage.getAuth();
		SystemUser user = auth.getUser();
		String ipAddr = auth.getIpAddress();
		int actionID = sysMessage.getAuditAction().id();
		String objName = sysMessage.getObjectName();
		long objID = sysMessage.getObjectID();
		Company company = auth.getCompany();

		// Do the audit.
		AuditLog audit = new AuditLog(actionID, user, ipAddr, company, objName, objID);
		this.auditLogDAO.create(audit);

		// If a slave exists, create separate entry.
		String associatedObjName = sysMessage.getAssocObjectName();
		Long associatedObjID = sysMessage.getAssocObjectID();
		if (associatedObjName != null) {
		    AuditLog assocAudit = new AuditLog(actionID, user, ipAddr, company,
			    associatedObjName, associatedObjID);
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