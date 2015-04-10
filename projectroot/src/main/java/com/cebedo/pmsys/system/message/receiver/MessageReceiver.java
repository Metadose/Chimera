package com.cebedo.pmsys.system.message.receiver;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class MessageReceiver {
	public final static String BEAN_NAME = "messageReceiver";
	private final JmsTemplate jmsTemplate;

	@Autowired
	public MessageReceiver(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * Receive the first message on the queue.<br>
	 * Message will then be dequeued.
	 * 
	 * @param destination
	 * @return
	 */
	public String receive(final String destination) {
		TextMessage msg = (TextMessage) jmsTemplate.receive(destination);
		try {
			return msg.getText();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return "";
	}
}