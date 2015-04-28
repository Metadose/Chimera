package com.cebedo.pmsys.system.message.listener;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.log4j.Logger;

public class LogMessageListener implements MessageListener {

	public final static String MESSAGE_DESTINATION = "system.service.post.log";
	public final static String KEY_LOG_NAME = "logName";
	public final static String KEY_LOG_TEXT = "logText";

	@Override
	@Transactional
	public void onMessage(Message message) {
		if (message instanceof ActiveMQMapMessage) {
			Map<String, Object> messageMap;
			try {
				messageMap = ((ActiveMQMapMessage) message).getContentMap();
				// Log the action.
				Logger.getLogger(String.valueOf(messageMap.get(KEY_LOG_NAME)))
						.info(String.valueOf(messageMap.get(KEY_LOG_TEXT)));
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("MessageThread must be of type Map");
		}
	}
}
