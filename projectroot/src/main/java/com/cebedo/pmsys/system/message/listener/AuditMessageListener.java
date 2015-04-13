package com.cebedo.pmsys.system.message.listener;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQMapMessage;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.security.audit.dao.AuditLogDAO;
import com.cebedo.pmsys.security.audit.model.AuditLog;
import com.cebedo.pmsys.systemuser.model.SystemUser;

public class AuditMessageListener implements MessageListener {

	public final static String MESSAGE_DESTINATION = "system.service.post.audit";
	public final static String KEY_COMPANY = "company";
	public final static String KEY_AUDIT_ACTION = "auditAction";
	public final static String KEY_AUDIT_OBJECT_NAME = "objName";
	public final static String KEY_AUDIT_OBJECT_ID = "objID";

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
				messageMap = ((ActiveMQMapMessage) message).getContentMap();
				long userID = Long.valueOf(String.valueOf(messageMap
						.get(MessageListenerImpl.KEY_USER_ID)));
				SystemUser user = new SystemUser(userID);
				String ipAddr = String.valueOf(messageMap
						.get(MessageListenerImpl.KEY_USER_IP_ADDR));

				// Audit the action.
				AuditLog audit = new AuditLog(AuditLog.ACTION_CREATE, user,
						ipAddr);
				audit.setCompany((Company) messageMap.get(KEY_COMPANY));
				audit.setObjectName(String.valueOf(messageMap
						.get(KEY_AUDIT_OBJECT_NAME)));
				audit.setObjectID(Long.valueOf(String.valueOf(messageMap
						.get(KEY_AUDIT_OBJECT_ID))));
				this.auditLogDAO.create(audit);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("Message must be of type Map");
		}
	}
}