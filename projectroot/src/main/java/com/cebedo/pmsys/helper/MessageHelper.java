package com.cebedo.pmsys.helper;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

import com.cebedo.pmsys.bean.SystemMessage;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.sender.MessageSender;
import com.cebedo.pmsys.token.AuthenticationToken;

public class MessageHelper {

    private AuthHelper authHelper = new AuthHelper();
    private BeanHelper beanHelper = new BeanHelper();

    public void send(AuditAction action, String objectName, long objectID, String assocName) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemMessage msg = new SystemMessage(auth, action, objectName, objectID, assocName);
	send(msg);
    }

    public void unauthorized(String objectName) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemMessage msg = new SystemMessage(auth, AuditAction.UNAUTHORIZED, objectName);
	send(msg);
    }

    public void unauthorized(String objectName, long objectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemMessage msg = new SystemMessage(auth, AuditAction.UNAUTHORIZED, objectName, objectID);
	send(msg);
    }

    public void send(AuditAction action, String objectName, long objectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemMessage msg = new SystemMessage(auth, action, objectName, objectID);
	send(msg);
    }

    public void send(AuditAction action, String objectName) {
	AuthenticationToken auth = this.authHelper.getAuth();
	SystemMessage msg = new SystemMessage(auth, action, objectName);
	send(msg);
    }

    private void send(SystemMessage msg) {
	// Get the bean
	MessageSender sender = (MessageSender) this.beanHelper.getBean(MessageSender.BEAN_NAME);

	// Define the destinations.
	Queue dest = new ActiveMQQueue(SystemMessage.DESTINATIONS);

	// Send the message.
	sender.send(dest, msg);
    }
}