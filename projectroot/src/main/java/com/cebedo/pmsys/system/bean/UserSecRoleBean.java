package com.cebedo.pmsys.system.bean;

public class UserSecRoleBean {

	private long userID;
	private long securityRoleID;

	public UserSecRoleBean() {
		;
	}

	public UserSecRoleBean(long id) {
		setUserID(id);
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public long getSecurityRoleID() {
		return securityRoleID;
	}

	public void setSecurityRoleID(long securityRoleID) {
		this.securityRoleID = securityRoleID;
	}

}
