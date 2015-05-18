package com.cebedo.pmsys.helper;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

import com.cebedo.pmsys.listener.AuditMessageListener;
import com.cebedo.pmsys.listener.LogMessageListener;
import com.cebedo.pmsys.listener.MessageListenerImpl;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.sender.MessageSender;
import com.cebedo.pmsys.token.AuthenticationToken;

public class MessageHelper {

	private AuthHelper authHelper = new AuthHelper();
	private LogHelper logHelper = new LogHelper();
	private BeanHelper beanHelper = new BeanHelper();

	public Map<String, Object> constructMessageMap(Project project,
			int auditAction, String logText) {
		AuthenticationToken auth = this.authHelper.getAuth();
		Map<String, Object> messageMap = new HashMap<String, Object>();
		messageMap.put(MessageListenerImpl.KEY_USER_ID, auth.getUser().getId());
		messageMap.put(MessageListenerImpl.KEY_USER_IP_ADDR,
				auth.getIpAddress());
		messageMap.put(AuditMessageListener.KEY_COMPANY,
				auth.getCompany() == null ? project.getCompany() == null ? null
						: project.getCompany() : auth.getCompany());

		// Audit.
		messageMap.put(AuditMessageListener.KEY_AUDIT_ACTION, auditAction);
		messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_NAME,
				Project.OBJECT_NAME);
		messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_ID,
				project.getId());

		// Log.
		messageMap.put(LogMessageListener.KEY_LOG_NAME, Project.OBJECT_NAME);
		messageMap.put(LogMessageListener.KEY_LOG_TEXT,
				this.logHelper.generateLogMessage(auth, logText));

		return messageMap;
	}

	/**
	 * Responsible for all post-service notifications.<br>
	 * Logging, auditing, notification, emailing.
	 * 
	 * @param project
	 * @param auditAction
	 * @param logText
	 */
	public void constructSendMessage(Project project, int auditAction,
			String logText) {
		Map<String, Object> messageMap = constructMessageMap(project,
				auditAction, logText);
		MessageSender sender = (MessageSender) this.beanHelper
				.getBean(MessageSender.BEAN_NAME);

		Queue dest = new ActiveMQQueue(AuditMessageListener.MESSAGE_DESTINATION
				+ "," + LogMessageListener.MESSAGE_DESTINATION);
		sender.send(dest, messageMap);
	}
}