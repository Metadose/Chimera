package com.cebedo.pmsys.helper;

import com.cebedo.pmsys.bean.JMSMessage;
import com.cebedo.pmsys.concurrency.AuditExecutor;
import com.cebedo.pmsys.concurrency.LoggerThread;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class MessageHelper {

    private AuthHelper authHelper = new AuthHelper();
    private BeanHelper beanHelper = new BeanHelper();

    /**
     * Non-auditable with key.
     * 
     * @param action
     * @param objectName
     * @param objectID
     * @param assocName
     * @param key
     */
    public void nonAuditableIDWithAssocWithKey(AuditAction action, String objectName, long objectID,
	    String assocName, String key) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectID, assocName, key);
	queueActiveMQ(msg);
    }

    /**
     * Non-auditable without key.
     * 
     * @param action
     * @param objectName
     * @param objectID
     * @param assocName
     * @param key
     */
    public void nonAuditableIDWithAssocNoKey(AuditAction action, String objectName, long objectID,
	    String assocName) {
	nonAuditableIDWithAssocWithKey(action, objectName, objectID, assocName, "");
    }

    /**
     * Non-auditable, no association.
     * 
     * @param action
     * @param objectName
     * @param objectID
     */
    public void nonAuditableIDNoAssoc(AuditAction action, String objectName, long objectID) {
	nonAuditableIDWithAssocWithKey(action, objectName, objectID, "", "");
    }

    public void loginFailed(String ipAddress, SystemUser user, AuditAction action) {
	JMSMessage msg = new JMSMessage(ipAddress, user, action);
	queueActiveMQ(msg);
    }

    public void loginSuccess(AuthenticationToken auth) {
	JMSMessage msg = new JMSMessage(auth, AuditAction.LOGIN_AUTHENTICATED);
	queueActiveMQ(msg);
    }

    public void unauthorized(String objectName) {
	unauthorizedKey(objectName, "");
    }

    public void unauthorizedKey(String objectName, String objectKey) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, AuditAction.ERROR_UNAUTHORIZED, objectName, objectKey);
	queueActiveMQ(msg);
    }

    public void unauthorizedID(String objectName, long objectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, AuditAction.ERROR_UNAUTHORIZED, objectName, objectID);
	queueActiveMQ(msg);
    }

    /**
     * Send the message to ActiveMQ.
     * 
     * @param msg
     */
    private void queueActiveMQ(JMSMessage msg) {

	// Uncomment block if we are using activeMQ.
	// Get the bean
	// MessageSender sender = (MessageSender)
	// this.beanHelper.getBean(MessageSender.BEAN_NAME);

	// Define the destinations.
	// Queue dest = new ActiveMQQueue(JMSMessage.DESTINATIONS);

	// Send the message.
	// sender.send(dest, msg);

	// Comment block if we are using activeMQ.
	LoggerThread loggerThread = new LoggerThread(msg);
	loggerThread.start();

	AuditExecutor auditExecutor = (AuditExecutor) this.beanHelper.getBean("auditExecutor");
	auditExecutor.execute(msg);
    }

    /**
     * Non-auditable, no association.
     * 
     * @param action
     * @param objectName
     * @param objectKey
     */
    public void nonAuditableKeyNoAssoc(AuditAction action, String objectName, String objectKey) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectKey);
	queueActiveMQ(msg);
    }

    /**
     * Non-auditable.
     * 
     * @param action
     * @param objectName
     * @param objectKey
     * @param assocName
     */
    public void nonAuditableListWithAssoc(AuditAction action, String objectName, String objectKey,
	    String assocName) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectKey, assocName);
	queueActiveMQ(msg);
    }

    /**
     * Non-auditable.
     * 
     * @param action
     * @param objectName
     */
    public void nonAuditableListNoAssoc(AuditAction action, String objectName) {
	nonAuditableListWithAssoc(action, objectName, "", "");
    }

    /**
     * Auditable.
     * 
     * @param action
     * @param objectName
     * @param objectID
     * @param assocName
     * @param key
     * @param proj
     * @param entry
     */
    public void auditableKey(AuditAction action, String objectName, long objectID, String assocName,
	    String key, Project proj, String entry) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectID, assocName, null);
	msg.setProjectID(proj == null ? 0 : proj.getId());
	msg.setEntryName(entry);
	msg.setAssocObjectKey(key);
	queueActiveMQ(msg);
    }

    /**
     * Auditable.
     * 
     * @param action
     * @param objectName
     * @param objectID
     * @param assocName
     * @param assocID
     * @param proj
     * @param entry
     */
    public void auditableID(AuditAction action, String objectName, long objectID, String assocName,
	    long assocID, Project proj, String entry) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectID, assocName, null);
	msg.setProjectID(proj == null ? 0 : proj.getId());
	msg.setEntryName(entry);
	msg.setAssocObjectID(assocID);
	queueActiveMQ(msg);
    }

    public void auditableID(AuditAction action, String objectName, long objectID, String entry) {
	auditableKey(action, objectName, objectID, "", "", null, entry);
    }
}