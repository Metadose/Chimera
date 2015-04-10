package com.cebedo.pmsys.system.message.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class QueueSender {
	public final static String BEAN_NAME = "queueSender";
	private final JmsTemplate jmsTemplate;

	@Autowired
	public QueueSender(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void send(String destination, final String msg) {
		jmsTemplate.convertAndSend(destination, msg);
	}
}