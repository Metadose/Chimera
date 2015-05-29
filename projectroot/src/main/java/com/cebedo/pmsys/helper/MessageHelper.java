package com.cebedo.pmsys.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.listener.AuditMessageListener;
import com.cebedo.pmsys.listener.LogMessageListener;
import com.cebedo.pmsys.listener.MailMessageListener;
import com.cebedo.pmsys.listener.MessageListenerImpl;
import com.cebedo.pmsys.listener.NotificationMessageListener;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.sender.MessageSender;
import com.cebedo.pmsys.token.AuthenticationToken;

public class MessageHelper {

    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private BeanHelper beanHelper = new BeanHelper();
    private NotificationHelper notifHelper = new NotificationHelper();

    private Map<String, Object> addMsgMapUser(Map<String, Object> messageMap,
	    long userID, String ipAddr, Company company) {
	messageMap.put(MessageListenerImpl.KEY_USER_ID, userID);
	messageMap.put(MessageListenerImpl.KEY_USER_IP_ADDR, ipAddr);
	messageMap.put(AuditMessageListener.KEY_COMPANY, company);
	return messageMap;
    }

    /**
     * Add the user's details to the message map.
     * 
     * @param messageMap
     * @param auth
     * @return
     */
    private Map<String, Object> addMsgMapUser(Map<String, Object> messageMap,
	    AuthenticationToken auth) {
	return addMsgMapUser(messageMap, auth.getUser().getId(),
		auth.getIpAddress(), auth.getCompany());
    }

    /**
     * Add the audit details to the message map.
     * 
     * @param messageMap
     * @param auditAction
     * @param objectName
     * @param objectID
     * @return
     */
    private Map<String, Object> addMsgMapAudit(Map<String, Object> messageMap,
	    int auditAction, String objectName, long objectID) {
	messageMap.put(AuditMessageListener.KEY_AUDIT_ACTION, auditAction);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_NAME, objectName);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_ID, objectID);
	return messageMap;
    }

    private Map<String, Object> addMsgMapLog(Map<String, Object> messageMap,
	    String objectName, String logText) {
	messageMap.put(LogMessageListener.KEY_LOG_NAME, objectName);
	messageMap.put(LogMessageListener.KEY_LOG_TEXT, logText);
	return messageMap;
    }

    /**
     * Add the notification details to the message map.
     * 
     * @param messageMap
     * @param notificationRecipients
     * @param notificationText
     * @return
     */
    private Map<String, Object> addMsgMapNotification(
	    Map<String, Object> messageMap, List<Long> notificationRecipients,
	    String notificationText) {
	messageMap.put(NotificationMessageListener.KEY_NOTIFICATION_RECIPIENTS,
		notificationRecipients);
	messageMap.put(NotificationMessageListener.KEY_NOTIFICATION_TEXT,
		notificationText);
	return messageMap;
    }

    /**
     * Add the log details to the message map.
     * 
     * @param messageMap
     * @param objectName
     * @param auth
     * @param logText
     * @return
     */
    private Map<String, Object> addMsgMapLog(Map<String, Object> messageMap,
	    String objectName, AuthenticationToken auth, String logText) {
	return addMsgMapLog(messageMap, objectName,
		this.logHelper.generateLogMessage(auth, logText));
    }

    /**
     * Construct the message map to be sent to the listeners.
     * 
     * @param objName
     * @param action
     * @param objID
     * @param name
     * @return
     */
    private Map<String, Object> constructMessageMap(String objName,
	    AuditAction action, long objID, String name) {

	// Get auth object.
	// Construct needed texts.
	AuthenticationToken auth = this.authHelper.getAuth();
	String textNotif = this.notifHelper.constructNotificationText(auth,
		action, objName, name);
	String textLog = this.logHelper.constructLogText(action, objName, name);
	List<Long> notificationRecipients = this.notifHelper
		.getRecipientsFromCompany(auth.getCompany());

	// Add data to map.
	Map<String, Object> messageMap = new HashMap<String, Object>();
	messageMap = addMsgMapUser(messageMap, auth);
	messageMap = addMsgMapAudit(messageMap, action.id(), objName, objID);
	messageMap = addMsgMapNotification(messageMap, notificationRecipients,
		textNotif);
	messageMap = addMsgMapLog(messageMap, objName, auth, textLog);
	return messageMap;
    }

    /**
     * Responsible for all post-service notifications.<br>
     * Logging, auditing, notification, emailing.
     * 
     * Send messages/notifications.<br>
     * Use message brokers as instructions.<br>
     * LIKE send message to logger to log.<br>
     * AND auditor to audit.<br>
     * Fire up the message so that it would go parallel with the service.
     * 
     * @param objName
     * @param action
     * @param objID
     * @param name
     * @return
     */
    public Map<String, Object> constructAndSendMessageMap(String objName,
	    AuditAction action, long objID, String name) {

	// Construct the message.
	Map<String, Object> messageMap = constructMessageMap(objName, action,
		objID, name);

	// Get the bean
	MessageSender sender = (MessageSender) this.beanHelper
		.getBean(MessageSender.BEAN_NAME);

	// Define the destinations.
	String destinations = AuditMessageListener.MESSAGE_DESTINATION + ",";
	destinations += LogMessageListener.MESSAGE_DESTINATION + ",";
	destinations += MailMessageListener.MESSAGE_DESTINATION;
	Queue dest = new ActiveMQQueue(destinations);

	// Send the message.
	sender.send(dest, messageMap);
	return messageMap;
    }

}