package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.SystemUser;

@Entity
@Table(name = UserRoleAssignment.TABLE_NAME)
public class UserRoleAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_user_role";
	private static final long serialVersionUID = 1L;

	private long userID;
	private long roleID;

	@Id
	@Column(name = SystemUser.COLUMN_PRIMARY_KEY, nullable = false)
	public long getSystemUserID() {
		return userID;
	}

	public void setSystemUserID(long userID) {
		this.userID = userID;
	}

	@Id
	@Column(name = SecurityRole.COLUMN_PRIMARY_KEY, nullable = false)
	public long getSecurityRoleID() {
		return roleID;
	}

	public void setSecurityRoleID(long roleID) {
		this.roleID = roleID;
	}
}
