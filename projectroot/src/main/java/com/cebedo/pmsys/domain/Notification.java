package com.cebedo.pmsys.domain;

import java.sql.Timestamp;
import java.util.Date;

public class Notification implements IDomainObject {

    public static final String OBJECT_KEY = "notifications";
    private static final long serialVersionUID = 1L;
    private Date timestamp;
    private long userID;
    private boolean read;
    private String content;

    public Notification() {
	;
    }

    public Notification(String content, long userId) {
	setContent(content);
	setUserID(userId);
	setRead(false);
	setTimestamp(new Timestamp(System.currentTimeMillis()));
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

    public static String constructKey(long userID, boolean read) {
	String key = "user:" + userID;
	key += ":" + OBJECT_KEY;
	key += ":read:" + read;

	return key;
    }

    @Override
    public String getKey() {
	String key = "user:" + getUserID();
	key += ":" + OBJECT_KEY;
	key += ":read:" + isRead();

	return key;
    }

}
