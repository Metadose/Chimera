package com.cebedo.pmsys.listener;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQMapMessage;

public class NotificationMessageListener implements MessageListener {

    public final static String MESSAGE_DESTINATION = "system.service.post.notification";
    public final static String KEY_NOTIFICATION_RECIPIENTS = "keyNotificationRecipients";
    public final static String KEY_NOTIFICATION_TEXT = "keyNotificationText";

    @Override
    @Transactional
    public void onMessage(Message message) {
	if (message instanceof ActiveMQMapMessage) {
	    Map<String, Object> messageMap;
	    try {
		// Get map contents.
		messageMap = ((ActiveMQMapMessage) message).getContentMap();

		// Construct and create notification.
		// for (long recipientID : notificationRecipients) {
		// Notification notification = new
		// Notification(notificationText,
		// recipientID);
		// // this.notificationZSetRepo.add(notification);
		// }

	    } catch (JMSException e) {
		e.printStackTrace();
	    }
	} else {
	    throw new IllegalArgumentException(
		    "MessageThread must be of type Map");
	}
    }
}
