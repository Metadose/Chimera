package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.listener.AuditMessageListener;
import com.cebedo.pmsys.listener.LogMessageListener;
import com.cebedo.pmsys.token.AuthenticationToken;

public class SystemMessage implements Serializable {

    private static final long serialVersionUID = 1678239421332274417L;
    public static final String DESTINATIONS = AuditMessageListener.MESSAGE_DESTINATION + ","
	    + LogMessageListener.MESSAGE_DESTINATION;

    // Who?
    private AuthenticationToken auth;

    // Audit.
    private AuditAction auditAction;
    private String objectName;
    private long objectID;
    private String assocObjectName;
    private long assocObjectID;

    // Log.
    private String logName;

    public SystemMessage() {
	;
    }

    public SystemMessage(AuthenticationToken auth2, AuditAction action, String objectName2,
	    long objectID2) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(objectID2);
	setLogName(objectName2);
    }

    public AuthenticationToken getAuth() {
	return auth;
    }

    public void setAuth(AuthenticationToken auth) {
	this.auth = auth;
    }

    public AuditAction getAuditAction() {
	return auditAction;
    }

    public void setAuditAction(AuditAction auditAction) {
	this.auditAction = auditAction;
    }

    public String getObjectName() {
	return objectName;
    }

    public void setObjectName(String objectName) {
	this.objectName = objectName;
    }

    public String getLogName() {
	return logName;
    }

    public void setLogName(String logName) {
	this.logName = logName;
    }

    public long getObjectID() {
	return objectID;
    }

    public void setObjectID(long objectID) {
	this.objectID = objectID;
    }

    public String getAssocObjectName() {
	return assocObjectName;
    }

    public void setAssocObjectName(String assocObjectName) {
	this.assocObjectName = assocObjectName;
    }

    public long getAssocObjectID() {
	return assocObjectID;
    }

    public void setAssocObjectID(long assocObjectID) {
	this.assocObjectID = assocObjectID;
    }

}
