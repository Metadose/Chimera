package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.listener.AuditMessageListener;
import com.cebedo.pmsys.listener.LogMessageListener;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class JMSMessage implements Serializable {

    private static final long serialVersionUID = 1678239421332274417L;
    public static final String DESTINATIONS = String.format("%s,%s",
	    AuditMessageListener.MESSAGE_DESTINATION, LogMessageListener.MESSAGE_DESTINATION);

    // Who?
    private AuthenticationToken auth;

    // Audit.
    private AuditAction auditAction;

    // Linked project.
    private long projectID;

    // Type of object and (ID or Key).
    private String objectName = "";
    private long objectID = -1;
    private String objectKey = "";
    private String entryName;

    // Associated object and ID.
    private String assocObjectName = "";
    private long assocObjectID = -1;

    // Transients.
    private String ipAddress = "";

    public JMSMessage() {
	;
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action, String objectName2,
	    long objectID2) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(objectID2);
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action, String objectName2, long objectID2,
	    String assocName) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(objectID2);
	setAssocObjectName(assocName);
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action, String objectName2) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action, String objectName2,
	    String objectKey2) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectKey(objectKey2);
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action, String objectName2, long objectID2,
	    String assocName, String key) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(objectID2);
	setAssocObjectName(assocName);
	setObjectKey(key);
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action, String objectName2,
	    String objectKey2, String assocName) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectKey(objectKey2);
	setAssocObjectName(assocName);
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action, String objectName2, long id,
	    String assocName, long assocID) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(id);
	setAssocObjectName(assocName);
	setAssocObjectID(assocID);
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action) {
	setAuth(auth2);
	setAuditAction(action);
    }

    public JMSMessage(String ipAddress, SystemUser user, AuditAction action) {
	setIpAddress(ipAddress);
	setObjectID(user == null ? -1 : user.getId());
	setObjectName(user == null ? "" : user.getUsername());
	setAuditAction(action);
    }

    public JMSMessage(AuthenticationToken auth2, AuditAction action, String objectName2, long objectID2,
	    Project project, String entryName) {
	setAuth(auth2);
	setAuditAction(action);
	setObjectName(objectName2);
	setObjectID(objectID2);
	setProjectID(project.getId());
	setEntryName(entryName);
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

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public long getProjectID() {
	return projectID;
    }

    public void setProjectID(long projectID) {
	this.projectID = projectID;
    }

    public String getEntryName() {
	return entryName;
    }

    public void setEntryName(String entryName) {
	this.entryName = entryName;
    }

}
