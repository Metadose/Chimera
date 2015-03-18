package com.cebedo.pmsys.systemuser.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;

@Entity
@Table(name = UserGroupAssignment.TABLE_NAME)
public class UserGroupAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_access_user";
	private static final long serialVersionUID = 1L;

	private long userID;
	private long groupID;

	@Id
	@Column(name = SystemUser.COLUMN_PRIMARY_KEY, nullable = false)
	public long getSystemUserID() {
		return userID;
	}

	public void setSystemUserID(long userID) {
		this.userID = userID;
	}

	@Id
	@Column(name = SecurityAccess.COLUMN_PRIMARY_KEY, nullable = false)
	public long getSecurityAccessID() {
		return groupID;
	}

	public void setSecurityAccessID(long groupID) {
		this.groupID = groupID;
	}
}
