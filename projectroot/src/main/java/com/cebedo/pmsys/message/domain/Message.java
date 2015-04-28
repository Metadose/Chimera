package com.cebedo.pmsys.message.domain;

import com.cebedo.pmsys.system.redis.domain.IDomainObject;

public class Message implements IDomainObject {

	public static final String OBJECT_NAME = "message";
	public static final String OBJECT_KEY = "message";
	private static final long serialVersionUID = 1L;

	// This will be the id of the message.
	// For every list of unique contributors, there is an existing conversation.
	// private List<Long> contributors;

	// "Sender" sent a message containing "content" at "timestamp".
	// To be read by the list of contributors.
	private long senderUserID;
	private long recipientUserID;
	private long timestamp;
	private String content;
	private boolean read;

	// All contributors already read this message?
	// private boolean allRead;

	// Which contributor(s) has already read this message?
	// private Map<Long, Boolean> readMap;

	// public List<Long> getContributors() {
	// return contributors;
	// }
	//
	// public void setContributors(List<Long> contributors) {
	// this.contributors = contributors;
	// }

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public long getSenderUserID() {
		return senderUserID;
	}

	public void setSenderUserID(long senderUserID) {
		this.senderUserID = senderUserID;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// public boolean isAllRead() {
	// return allRead;
	// }
	//
	// public void setAllRead(boolean allRead) {
	// this.allRead = allRead;
	// }
	//
	// public Map<Long, Boolean> getReadMap() {
	// return readMap;
	// }
	//
	// public void setReadMap(Map<Long, Boolean> readMap) {
	// this.readMap = readMap;
	// }

	public long getRecipientUserID() {
		return recipientUserID;
	}

	public void setRecipientUserID(long recipientUserID) {
		this.recipientUserID = recipientUserID;
	}

	@Override
	public String getKey() {
		// Key: message:[id,id2,id3,..idn]:allread:[true/false]
		// Will be indexed by timestamp.
		// return OBJECT_KEY + ":" + StringUtils.join(contributors, ",");

		// Key:
		// message:recipient:[recipient-id]:read:[true/false]
		return OBJECT_KEY + ":recipient:" + getRecipientUserID() + ":read:"
				+ isRead();
	}

}
