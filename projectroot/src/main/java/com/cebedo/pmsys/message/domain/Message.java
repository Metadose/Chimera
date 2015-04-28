package com.cebedo.pmsys.message.domain;

import java.util.Date;

import com.cebedo.pmsys.system.redis.domain.IDomainObject;
import com.cebedo.pmsys.systemuser.model.SystemUser;

public class Message implements IDomainObject {

	public static final String OBJECT_NAME = "message";
	public static final String OBJECT_KEY = "message";
	private static final long serialVersionUID = 1L;

	private SystemUser sender;
	private SystemUser recipient;
	private Date timestamp;
	private String content;
	private boolean read;

	public SystemUser getSender() {
		return sender;
	}

	public void setSender(SystemUser sender) {
		this.sender = sender;
	}

	public SystemUser getRecipient() {
		return recipient;
	}

	public void setRecipient(SystemUser recipient) {
		this.recipient = recipient;
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

	public static String constructKey(long userID, boolean read) {
		return OBJECT_KEY + ":recipient:" + userID + ":read:" + read;
	}

	@Override
	public String getKey() {
		return OBJECT_KEY + ":recipient:" + getRecipient().getId() + ":read:"
				+ isRead();
	}

}
