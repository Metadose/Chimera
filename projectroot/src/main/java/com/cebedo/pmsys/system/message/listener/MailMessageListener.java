package com.cebedo.pmsys.system.message.listener;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQMapMessage;

import com.cebedo.pmsys.system.helper.BeanHelper;
import com.cebedo.pmsys.system.mail.mailer.Mailer;

public class MailMessageListener implements MessageListener {

	public final static String MESSAGE_DESTINATION = "system.mail.send";

	@Override
	@Transactional
	public void onMessage(Message message) {
		if (message instanceof ActiveMQMapMessage) {
			Map<String, Object> messageMap;
			try {
				messageMap = ((ActiveMQMapMessage) message).getContentMap();
				BeanHelper beanHelper = new BeanHelper();
				Mailer mm = (Mailer) beanHelper.getBean("mailer");
				mm.sendMail("configure.this@system.properties",
						"cebedo.vii@gmail.com", "Testing123",
						"Testing only \n\n Hello Spring Email Sender");
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("Message must be of type Map");
		}
	}

}
