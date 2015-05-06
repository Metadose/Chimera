package com.cebedo.pmsys.system.bean;

public class UserSecAccessBean {

	private long userID;
	private long securityAccessID;

	public UserSecAccessBean() {
		;
	}

	public UserSecAccessBean(int id) {
		setUserID(id);
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public long getSecurityAccessID() {
		return securityAccessID;
	}

	public void setSecurityAccessID(long securityAccessID) {
		this.securityAccessID = securityAccessID;
	}

}
