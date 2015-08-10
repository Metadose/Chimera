package com.cebedo.pmsys.bean;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class MessageCreatorImpl implements MessageCreator {

    private SystemMessage message;

    public MessageCreatorImpl() {
	;
    }

    public MessageCreatorImpl(SystemMessage msg) {
	setMessage(msg);
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
	return session.createObjectMessage(this.message);
    }

    public SystemMessage getMessage() {
	return message;
    }

    public void setMessage(SystemMessage message) {
	this.message = message;
    }

}
