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
    private String objectName = "";
    private long objectID = -1;
    private String objectKey = "";
    private String assocObjectName = "";
    private long assocObjectID = -1;

    public SystemMessage() {
	;
    }

    public SystemMessage(AuthenticationToken auth2, AuditAction action, String objectName2,
	    long objectID2) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(objectID2);
    }

    public SystemMessage(AuthenticationToken auth2, AuditAction action, String objectName2,
	    long objectID2, String assocName) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(objectID2);
	setAssocObjectName(assocName);
    }

    public SystemMessage(AuthenticationToken auth2, AuditAction action, String objectName2) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
    }

    public SystemMessage(AuthenticationToken auth2, AuditAction action, String objectName2,
	    String objectKey2) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectKey(objectKey2);
    }

    public SystemMessage(AuthenticationToken auth2, AuditAction action, String objectName2,
	    long objectID2, String assocName, String key) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(objectID2);
	setAssocObjectName(assocName);
	setObjectKey(key);
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

    public String getObjectKey() {
	return objectKey;
    }

    public void setObjectKey(String objectKey) {
	this.objectKey = objectKey;
    }

}
