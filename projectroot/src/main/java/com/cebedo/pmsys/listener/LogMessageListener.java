package com.cebedo.pmsys.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

import com.cebedo.pmsys.bean.SystemMessage;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class LogMessageListener implements MessageListener {

    public final static String MESSAGE_DESTINATION = "system.log.info";
    private static Logger logger = Logger.getLogger("sysLogger");

    @Override
    @Transactional
    public void onMessage(Message message) {
	if (message instanceof ActiveMQObjectMessage) {
	    SystemMessage sysMessage;
	    try {
		// Get contents.
		sysMessage = (SystemMessage) ((ActiveMQObjectMessage) message).getObject();

		// Log.
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		AuthenticationToken auth = sysMessage.getAuth();
		Company company = auth.getCompany() == null ? new Company() : auth.getCompany();
		SystemUser user = auth.getUser();
		Staff staff = auth.getStaff();
		AuditAction action = sysMessage.getAuditAction();

		// Identification.
		long nowLong = System.currentTimeMillis();
		Date now = new Date(nowLong);
		String ipAddr = auth.getIpAddress();
		long companyID = company.getId();
		String companyName = company.getName();
		long userID = user.getId();
		String userName = user.getUsername();
		long staffID = staff.getId();
		String staffName = staff.getFullName();
		boolean companyAdmin = auth.isCompanyAdmin();
		boolean superAdmin = auth.isSuperAdmin();

		// Action.
		int actionID = action.id();
		String actionName = action.label();
		long objectID = sysMessage.getObjectID();
		String objectName = sysMessage.getObjectName();
		long assocObjID = sysMessage.getAssocObjectID();
		String assocObjName = sysMessage.getAssocObjectName();

		// Construct the log message.
		String logMessage = "TSTAMP:\"%s\" IP:\"%s\" COM_ID:\"%s\" COM_NAME:\"%s\" USER_ID:\"%s\" USER_NAME:\"%s\" STAFF_ID:\"%s\" STAFF_NAME:\"%s\" COM_ADMIN:\"%s\" SUPER_ADMIN:\"%s\" ";
		logMessage += "ACT_ID:\"%s\" ACT_NAME:\"%s\" OBJ_ID:\"%s\" OBJ_NAME:\"%s\" ASSOC_ID:\"%s\" ASSOC_NAME:\"%s\"";

		// Do log.
		logger.info(String.format(logMessage, formatter.format(now), ipAddr, companyID,
			companyName, userID, userName, staffID, staffName, companyAdmin, superAdmin,
			actionID, actionName, objectID, objectName, assocObjID, assocObjName));
	    } catch (JMSException e) {
		e.printStackTrace();
	    }
	}

	else {
	    throw new IllegalArgumentException("MessageThread must be of type SystemMessage");
	}
    }
}
