package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.model.SecurityAccess;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

@Entity
@Table(name = UserAccessAssignment.TABLE_NAME)
public class UserAccessAssignment implements Serializable {

    public static final String TABLE_NAME = "assignments_user_access";
    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("UserAccessAssignment");

    private long userID;
    private long accessID;

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
	return accessID;
    }

    public void setSecurityAccessID(long accessID) {
	this.accessID = accessID;
    }
}
