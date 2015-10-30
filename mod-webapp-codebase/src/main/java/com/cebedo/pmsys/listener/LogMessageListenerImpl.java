package com.cebedo.pmsys.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

import com.cebedo.pmsys.bean.JMSMessage;
import com.cebedo.pmsys.constants.RegistryLogger;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class LogMessageListenerImpl implements MessageListener {

    public final static String MESSAGE_DESTINATION = "system.log.tail";
    private static Logger logger = Logger.getLogger(RegistryLogger.LOGGER_TAIL);

    @Override
    @Transactional
    public void onMessage(Message message) {
	if (message instanceof ActiveMQObjectMessage) {
	    JMSMessage sysMessage;
	    try {
		// Get contents.
		sysMessage = (JMSMessage) ((ActiveMQObjectMessage) message).getObject();

		// Log.
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		AuthenticationToken auth = sysMessage.getAuth();
		Company company = auth == null ? null : auth.getCompany();
		SystemUser user = auth == null ? null : auth.getUser();
		Staff staff = auth == null ? null : auth.getStaff();
		AuditAction action = sysMessage.getAuditAction();

		// Identification.
		long nowLong = System.currentTimeMillis();
		Date now = new Date(nowLong);
		String ipAddr = (auth == null || auth.getIpAddress().isEmpty())
			? sysMessage.getIpAddress() : auth.getIpAddress();
		long companyID = (auth == null || company == null) ? 0 : company.getId();
		String companyName = (auth == null || company == null) ? "" : company.getName();
		long userID = auth == null ? 0 : user.getId();
		String userName = auth == null ? "" : user.getUsername();
		long staffID = (auth == null || staff == null) ? 0 : staff.getId();
		String staffName = (auth == null || staff == null) ? "" : staff.getFullName();
		boolean companyAdmin = auth == null ? false : auth.isCompanyAdmin();
		boolean superAdmin = auth == null ? false : auth.isSuperAdmin();

		// Action.
		int actionID = action.id();
		String actionName = action.label();
		long objectID = sysMessage.getObjectID();
		String objectKey = sysMessage.getObjectKey();
		String objectName = sysMessage.getObjectName();
		long assocObjID = sysMessage.getAssocObjectID();
		String assocObjName = sysMessage.getAssocObjectName();

		// Construct the log message.
		String logMessage = "TSTAMP:\"%s\" IP:\"%s\" COM_ID:\"%s\" COM_NAME:\"%s\" USER_ID:\"%s\" USER_NAME:\"%s\" STAFF_ID:\"%s\" STAFF_NAME:\"%s\" COM_ADMIN:\"%s\" SUPER_ADMIN:\"%s\" ";
		logMessage += "ACT_ID:\"%s\" ACT_NAME:\"%s\" OBJ_ID:\"%s\" OBJ_NAME:\"%s\" OBJ_KEY:\"%s\" ASSOC_ID:\"%s\" ASSOC_NAME:\"%s\"";

		// Do log.
		logger.info(
			String.format(logMessage, formatter.format(now), ipAddr, companyID, companyName,
				userID, userName, staffID, staffName, companyAdmin, superAdmin, actionID,
				actionName, objectID, objectName, objectKey, assocObjID, assocObjName));
	    } catch (JMSException e) {
		e.printStackTrace();
	    }
	}

	else {
	    throw new IllegalArgumentException("MessageThread must be of type SystemMessage");
	}
    }
}
