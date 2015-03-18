package com.cebedo.pmsys.systemuser.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.security.securitygroup.model.SecurityGroup;

@Entity
@Table(name = UserGroupAssignment.TABLE_NAME)
public class UserGroupAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_group_user";
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
	@Column(name = SecurityGroup.COLUMN_PRIMARY_KEY, nullable = false)
	public long getSecurityGroupID() {
		return groupID;
	}

	public void setSecurityGroupID(long groupID) {
		this.groupID = groupID;
	}
}
