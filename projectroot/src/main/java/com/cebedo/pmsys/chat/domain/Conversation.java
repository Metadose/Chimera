package com.cebedo.pmsys.chat.domain;

import java.util.List;

import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.system.redis.domain.IDomainObject;
import com.cebedo.pmsys.systemuser.model.SystemUser;

public class Conversation implements IDomainObject {

	private static final long serialVersionUID = 1L;
	private List<SystemUser> contributors;

	public List<SystemUser> getContributors() {
		return contributors;
	}

	public void setContributors(List<SystemUser> contributors) {
		this.contributors = contributors;
	}

	public static String constructKey(long userID) {
		String key = "user:" + userID + ":" + Message.OBJECT_NAME
				+ ":conversations";
		return key;
	}

	@Override
	public String getKey() {
		// Message Key: message:1.4
		// This Key: user:131:message:conversations
		// This will be a (list of (list of contributors)).
		long userID = new AuthHelper().getAuth().getUser().getId();
		String key = "user:" + userID + ":" + Message.OBJECT_NAME
				+ ":conversations";
		return key;
	}

}
