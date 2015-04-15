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

	public static final String MESSAGE_DESTINATION = "system.mail.send";
	public static final String KEY_MAIL_TO = "mailTo";
	public static final String KEY_MAIL_SUBJECT = "mailSubject";
	public static final String KEY_MAIL_TEXT = "mailText";

	@Override
	@Transactional
	public void onMessage(Message message) {
		if (message instanceof ActiveMQMapMessage) {
			Map<String, Object> messageMap;
			try {
				messageMap = ((ActiveMQMapMessage) message).getContentMap();
				String to = String.valueOf(messageMap.get(KEY_MAIL_TO));
				String subj = String.valueOf(messageMap.get(KEY_MAIL_SUBJECT));
				String text = String.valueOf(messageMap.get(KEY_MAIL_TEXT));
				BeanHelper beanHelper = new BeanHelper();
				Mailer mailer = (Mailer) beanHelper.getBean(Mailer.BEAN_NAME);
				mailer.sendMail("configure.this@system.properties", to, subj,
						text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("Message must be of type Map");
		}
	}

}
