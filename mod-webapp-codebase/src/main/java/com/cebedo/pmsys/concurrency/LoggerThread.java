package com.cebedo.pmsys.concurrency;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.cebedo.pmsys.bean.JMSMessage;
import com.cebedo.pmsys.constants.RegistryLogger;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class LoggerThread extends Thread {

    private static Logger logger = Logger.getLogger(RegistryLogger.LOGGER_TAIL);

    private AuthenticationToken auth;
    private AuditAction action;
    private String ipAddress;
    private long objectID;
    private String objectKey;
    private String objectName;
    private long assocObjID;
    private String assocObjName;

    public LoggerThread(JMSMessage msg) {
	this.auth = msg.getAuth();
	this.action = msg.getAuditAction();
	this.ipAddress = msg.getIpAddress();
	this.objectID = msg.getObjectID();
	this.objectKey = msg.getObjectKey();
	this.objectName = msg.getObjectName();
	this.assocObjID = msg.getAssocObjectID();
	this.assocObjName = msg.getAssocObjectName();
    }

    public LoggerThread() {
	;
    }

    @Override
    public void run() {

	// Log.
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	Company company = auth == null ? null : auth.getCompany();
	SystemUser user = auth == null ? null : auth.getUser();
	Staff staff = auth == null ? null : auth.getStaff();

	// Identification.
	long nowLong = System.currentTimeMillis();
	Date now = new Date(nowLong);
	String ipAddr = (auth == null || auth.getIpAddress().isEmpty()) ? ipAddress
		: auth.getIpAddress();
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

	// Construct the log message.
	String logMessage = "TSTAMP:\"%s\" IP:\"%s\" COM_ID:\"%s\" COM_NAME:\"%s\" USER_ID:\"%s\" USER_NAME:\"%s\" STAFF_ID:\"%s\" STAFF_NAME:\"%s\" COM_ADMIN:\"%s\" SUPER_ADMIN:\"%s\" ";
	logMessage += "ACT_ID:\"%s\" ACT_NAME:\"%s\" OBJ_ID:\"%s\" OBJ_NAME:\"%s\" OBJ_KEY:\"%s\" ASSOC_ID:\"%s\" ASSOC_NAME:\"%s\"";

	// Do log.
	logger.info(String.format(logMessage, formatter.format(now), ipAddr, companyID, companyName,
		userID, userName, staffID, staffName, companyAdmin, superAdmin, actionID, actionName,
		objectID, objectName, objectKey, assocObjID, assocObjName));
    }

}
