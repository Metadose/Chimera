package com.cebedo.pmsys.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

public class Notification implements IDomainObject {

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("Notification");
    private Date timestamp;
    private Long companyID;
    private long userID;
    private boolean read;
    private String content;
    private Map<String, Object> extMap;

    public Notification() {
	;
    }

    public Notification(Long companyID, String content, long userId) {
	setCompanyID(companyID);
	setContent(content);
	setUserID(userId);
	setRead(false);
	setTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    public Long getCompanyID() {
	return companyID;
    }

    public void setCompanyID(Long companyID) {
	this.companyID = companyID;
    }

    public Date getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    public long getUserID() {
	return userID;
    }

    public void setUserID(long userID) {
	this.userID = userID;
    }

    public boolean isRead() {
	return read;
    }

    public void setRead(boolean read) {
	this.read = read;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public static String constructKey(long companyID, long userID, boolean read) {
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";
	String key = companyPart + "user:" + userID;
	key += ":" + RedisConstants.OBJECT_NOTIFICATION;
	key += ":read:" + read;

	return key;
    }

    @Override
    public String getKey() {
	long companyID = getCompanyID() == null ? 0 : getCompanyID();
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";

	String key = companyPart + "user:" + getUserID();
	key += ":" + RedisConstants.OBJECT_NOTIFICATION;
	key += ":read:" + isRead();

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
