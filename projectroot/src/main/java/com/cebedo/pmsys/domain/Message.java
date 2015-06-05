package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.model.Company;

public class Message implements IDomainObject {

    private static final long serialVersionUID = 1L;
    private Long companyID;
    private long senderID;
    private long recipientID;
    private Date timestamp;
    private String content;
    private boolean read;
    private Map<String, Object> extMap;

    public Long getCompanyID() {
	return companyID;
    }

    public void setCompanyID(Long companyID) {
	this.companyID = companyID;
    }

    public long getSenderID() {
	return senderID;
    }

    public void setSenderID(long sender) {
	this.senderID = sender;
    }

    public Date getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public boolean isRead() {
	return read;
    }

    public void setRead(boolean read) {
	this.read = read;
    }

    public long getRecipientID() {
	return recipientID;
    }

    public void setRecipientID(long recipientID) {
	this.recipientID = recipientID;
    }

    public static String constructKey(Long companyID, long recipientID,
	    long senderID) {
	// Key: message:object:.1.4.
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";
	String key = companyPart + RedisConstants.OBJECT_MESSAGE + ":object:";

	if (recipientID <= senderID) {
	    key += "." + recipientID + "." + senderID + ".";
	} else if (recipientID > senderID) {
	    key += "." + senderID + "." + recipientID + ".";
	}
	return key;
    }

    @Override
    public String getKey() {
	// Key: message:object:.1.4.
	long companyID = getCompanyID() == null ? 0 : getCompanyID();
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";
	String key = companyPart + RedisConstants.OBJECT_MESSAGE + ":object:";

	long recipientID = getRecipientID();
	long senderID = getSenderID();

	if (recipientID <= senderID) {
	    key += "." + recipientID + "." + senderID + ".";
	} else if (recipientID > senderID) {
	    key += "." + senderID + "." + recipientID + ".";
	}
	return key;
    }

    @Override
    public Map<String, Object> getExtMap() {
	return extMap;
    }

    @Override
    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

}
