package com.cebedo.pmsys.listener;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQMapMessage;

import com.cebedo.pmsys.dao.AuditLogDAO;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;

public class AuditMessageListener implements MessageListener {

    public final static String MESSAGE_DESTINATION = "system.service.post.audit";
    public final static String KEY_COMPANY = "company";
    public final static String KEY_AUDIT_ACTION = "auditAction";
    public final static String KEY_AUDIT_OBJECT_NAME = "objName";
    public final static String KEY_AUDIT_OBJECT_ID = "objID";
    public final static String KEY_AUDIT_OBJECT_NAME_ASSOC = "objNameAssoc";
    public final static String KEY_AUDIT_OBJECT_ID_ASSOC = "objIDAssoc";

    private AuditLogDAO auditLogDAO;

    public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
	this.auditLogDAO = auditLogDAO;
    }

    @Override
    @Transactional
    public void onMessage(Message message) {
	if (message instanceof ActiveMQMapMessage) {
	    Map<String, Object> messageMap;
	    try {
		// Get the map contents.
		messageMap = ((ActiveMQMapMessage) message).getContentMap();

		// Get the user details.
		long userID = Long.valueOf(String.valueOf(messageMap
			.get(MessageListenerImpl.KEY_USER_ID)));
		SystemUser user = new SystemUser(userID);
		String ipAddr = String.valueOf(messageMap
			.get(MessageListenerImpl.KEY_USER_IP_ADDR));

		// Audit the action.
		int actionID = Integer.valueOf(String.valueOf(messageMap
			.get(KEY_AUDIT_ACTION)));
		String objName = String.valueOf(messageMap
			.get(KEY_AUDIT_OBJECT_NAME));
		long objID = Long.valueOf(String.valueOf(messageMap
			.get(KEY_AUDIT_OBJECT_ID)));
		Long companyID = Long.valueOf(String.valueOf(messageMap
			.get(KEY_COMPANY)));
		Company company = new Company(companyID);

		// If a slave exists,
		// create separate entry.
		String associatedObjName = messageMap
			.get(KEY_AUDIT_OBJECT_NAME_ASSOC) == null ? null
			: String.valueOf(messageMap
				.get(KEY_AUDIT_OBJECT_NAME_ASSOC));
		Long associatedObjID = messageMap
			.get(KEY_AUDIT_OBJECT_ID_ASSOC) == null ? null : Long
			.valueOf(String.valueOf(messageMap
				.get(KEY_AUDIT_OBJECT_ID_ASSOC)));

		if (associatedObjName != null) {
		    // New audit entry.
		    AuditLog audit = new AuditLog(actionID, user, ipAddr);
		    audit.setCompany(company);
		    audit.setObjectName(objName);
		    audit.setObjectID(objID);
		    this.auditLogDAO.create(audit);

		    // If a slave exists, create separate entry.
		    audit.setObjectName(associatedObjName);
		    audit.setObjectID(associatedObjID);
		    this.auditLogDAO.create(audit);
		}

		// Normal behaviour.
		// New audit entry.
		else {
		    AuditLog audit = new AuditLog(actionID, user, ipAddr);
		    audit.setCompany(company);
		    audit.setObjectName(objName);
		    audit.setObjectID(objID);
		    this.auditLogDAO.create(audit);
		}

	    } catch (JMSException e) {
		e.printStackTrace();
	    }
	} else {
	    throw new IllegalArgumentException(
		    "MessageThread must be of type Map");
	}
    }
}