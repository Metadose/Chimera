package com.cebedo.pmsys.sender;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import com.cebedo.pmsys.bean.JMSMessageCreatorImpl;
import com.cebedo.pmsys.bean.JMSMessage;

public class MessageSender {
    public final static String BEAN_NAME = "messageSender";

    private final JmsTemplate jmsTemplate;

    @Autowired
    public MessageSender(final JmsTemplate jmsTemplate) {
	this.jmsTemplate = jmsTemplate;
    }

    public void send(Queue dest, final JMSMessage msg) {
	this.jmsTemplate.send(dest, new JMSMessageCreatorImpl(msg));
    }
}