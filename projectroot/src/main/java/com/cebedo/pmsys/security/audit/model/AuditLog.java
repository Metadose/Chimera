package com.cebedo.pmsys.security.audit.model;

import com.cebedo.pmsys.systemuser.model.SystemUser;

public class AuditLog {

	private long id;
	private SystemUser user;
	private int action;
	private String objectName;
	private long objectID;
	private String oldObjectProperties;
	private String newObjectProperties;

}
