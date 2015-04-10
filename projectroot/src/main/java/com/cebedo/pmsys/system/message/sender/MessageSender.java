package com.cebedo.pmsys.system.message.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class MessageSender {
	public final static String BEAN_NAME = "messageSender";
	private final JmsTemplate jmsTemplate;

	@Autowired
	public MessageSender(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void send(String destination, final String msg) {
		jmsTemplate.convertAndSend(destination, msg);
	}
}