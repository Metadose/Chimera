package com.cebedo.pmsys.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

@Deprecated
public class MailMessageListenerImpl implements MessageListener {

    public static final String MESSAGE_DESTINATION = "system.mail.send";

    @Override
    @Transactional
    public void onMessage(Message message) {
	// if (message instanceof ActiveMQMapMessage) {
	// Map<String, Object> messageMap;
	// try {
	// messageMap = ((ActiveMQMapMessage) message).getContentMap();
	//
	// // String to = String.valueOf(messageMap.get(KEY_MAIL_TO));
	// // String subj =
	// // String.valueOf(messageMap.get(KEY_MAIL_SUBJECT));
	// // String text = String.valueOf(messageMap.get(KEY_MAIL_TEXT));
	//
	// BeanHelper beanHelper = new BeanHelper();
	// MailerService mailer = (MailerService)
	// beanHelper.getBean(MailerService.BEAN_NAME);
	// System.out.println("TODO configure.this@system.properties");
	// mailer.sendMail("configure.this@system.properties", "to", "subj",
	// "content text");
	// } catch (JMSException e) {
	// e.printStackTrace();
	// }
	// } else {
	// throw new IllegalArgumentException("MessageThread must be of type
	// Map");
	// }
    }

}
