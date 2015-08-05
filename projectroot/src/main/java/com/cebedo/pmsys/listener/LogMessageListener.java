package com.cebedo.pmsys.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

import com.cebedo.pmsys.bean.SystemMessage;
import com.cebedo.pmsys.helper.LogHelper;

public class LogMessageListener implements MessageListener {

    public final static String MESSAGE_DESTINATION = "system.service.post.log";
    private LogHelper logHelper = new LogHelper();

    @Override
    @Transactional
    public void onMessage(Message message) {
	if (message instanceof ActiveMQObjectMessage) {
	    SystemMessage sysMessage;
	    try {
		// Get contents.
		sysMessage = (SystemMessage) ((ActiveMQObjectMessage) message).getObject();

		// Construct message.
		String assocName = sysMessage.getAssocObjectName();
		String content = assocName == null ?

		this.logHelper.constructTextActionOnObj(sysMessage.getAuditAction(),
			sysMessage.getObjectName(), sysMessage.getObjectID()) :

		this.logHelper.constructTextActionOnObjWithAssoc(sysMessage.getAuditAction(),
			sysMessage.getObjectName(), sysMessage.getObjectID(),
			sysMessage.getAssocObjectName(), sysMessage.getAssocObjectID());

		String logMessage = this.logHelper.logMessage(sysMessage.getAuth(), content);

		// Log.
		Logger.getLogger(sysMessage.getLogName()).info(logMessage);
	    } catch (JMSException e) {
		e.printStackTrace();
	    }
	}

	else {
	    throw new IllegalArgumentException("MessageThread must be of type SystemMessage");
	}
    }
}
