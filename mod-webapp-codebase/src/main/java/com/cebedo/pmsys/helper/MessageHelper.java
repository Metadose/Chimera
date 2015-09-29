package com.cebedo.pmsys.helper;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

import com.cebedo.pmsys.bean.JMSMessage;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.sender.MessageSender;
import com.cebedo.pmsys.token.AuthenticationToken;

public class MessageHelper {

    private AuthHelper authHelper = new AuthHelper();
    private BeanHelper beanHelper = new BeanHelper();

    public void send(AuditAction action, String objectName, long objectID, String assocName) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectID, assocName);
	send(msg);
    }

    public void loginError(String ipAddress, SystemUser user, AuditAction action) {
	JMSMessage msg = new JMSMessage(ipAddress, user, action);
	send(msg);
    }

    public void login(AuditAction action, AuthenticationToken auth) {
	JMSMessage msg = new JMSMessage(auth, action);
	send(msg);
    }

    public void unauthorized(String objectName) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, AuditAction.ERROR_UNAUTHORIZED, objectName);
	send(msg);
    }

    public void unauthorized(String objectName, String objectKey) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, AuditAction.ERROR_UNAUTHORIZED, objectName, objectKey);
	send(msg);
    }

    public void unauthorized(String objectName, long objectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, AuditAction.ERROR_UNAUTHORIZED, objectName, objectID);
	send(msg);
    }

    public void send(AuditAction action, String objectName, long objectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectID);
	send(msg);
    }

    public void send(AuditAction action, String objectName) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName);
	send(msg);
    }

    private void send(JMSMessage msg) {
	// Get the bean
	MessageSender sender = (MessageSender) this.beanHelper.getBean(MessageSender.BEAN_NAME);

	// Define the destinations.
	Queue dest = new ActiveMQQueue(JMSMessage.DESTINATIONS);

	// Send the message.
	sender.send(dest, msg);
    }

    public void send(AuditAction action, String objectName, String objectKey) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectKey);
	send(msg);
    }

    public void send(AuditAction action, String objectName, long objectID, String assocName,
	    String key) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectID, assocName, key);
	send(msg);
    }

    public void send(AuditAction action, String objectName, String objectKey, String assocName) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectKey, assocName);
	send(msg);
    }

    public void send(AuditAction action, String objectName, long id, String assocName, long assocID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, id, assocName, assocID);
	send(msg);
    }

    public void send(AuditAction action, String objectName, long objectID, Project project) {
	AuthenticationToken auth = this.authHelper.getAuth();
	JMSMessage msg = new JMSMessage(auth, action, objectName, objectID, project);
	send(msg);
    }
}