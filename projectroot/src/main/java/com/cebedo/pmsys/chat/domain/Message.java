package com.cebedo.pmsys.chat.domain;

import java.util.Date;

import com.cebedo.pmsys.system.redis.domain.IDomainObject;
import com.cebedo.pmsys.systemuser.model.SystemUser;

public class Message implements IDomainObject {

	public static final String OBJECT_NAME = "message";
	public static final String OBJECT_KEY = "message";
	private static final long serialVersionUID = 1L;

	private SystemUser sender;
	private SystemUser recipient;
	private long recipientID;
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

	public long getRecipientID() {
		return recipientID;
	}

	public void setRecipientID(long recipientID) {
		this.recipientID = recipientID;
	}

	public static String constructKey(long recipientID, long senderID) {
		// Key: message:object:.1.4.
		String key = OBJECT_KEY + ":object:";

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
		String key = OBJECT_KEY + ":object:";
		long recipientID = getRecipient().getId();
		long senderID = getSender().getId();

		if (recipientID <= senderID) {
			key += "." + recipientID + "." + senderID + ".";
		} else if (recipientID > senderID) {
			key += "." + senderID + "." + recipientID + ".";
		}
		return key;
	}

}
