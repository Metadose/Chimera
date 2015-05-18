package com.cebedo.pmsys.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MessageListenerImpl implements MessageListener {

	public static final String KEY_USER_ID = "userID";
	public static final String KEY_USER_IP_ADDR = "ipAddr";

	/**
	 * TODO Handle event when message is sent to a default destination.
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				System.out.println(((TextMessage) message).getText());
			} catch (JMSException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			throw new IllegalArgumentException(
					"MessageThread must be of type TextMessage");
		}
	}
}