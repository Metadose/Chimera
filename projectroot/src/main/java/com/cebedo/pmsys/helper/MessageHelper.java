package com.cebedo.pmsys.helper;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

import com.cebedo.pmsys.bean.SystemMessage;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.sender.MessageSender;
import com.cebedo.pmsys.token.AuthenticationToken;

public class MessageHelper {

    private BeanHelper beanHelper = new BeanHelper();

    public void send(AuthenticationToken auth, AuditAction action, String objectName, long objectID) {
	SystemMessage msg = new SystemMessage(auth, action, objectName, objectID);
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