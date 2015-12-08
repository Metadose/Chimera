package com.cebedo.pmsys.concurrency;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;

import com.cebedo.pmsys.bean.JMSMessage;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.listener.AuditMessageListenerImpl;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.service.AuditLogService;
import com.cebedo.pmsys.token.AuthenticationToken;

@Component
public class AuditExecutor {

    private TaskExecutor taskExecutor;
    private AuditLogService auditLogService;

    @Autowired
    @Qualifier(value = "auditLogService")
    public void setAuditLogService(AuditLogService auditLogService) {
	this.auditLogService = auditLogService;
    }

    public AuditExecutor(TaskExecutor taskExecutor) {
	this.taskExecutor = taskExecutor;
    }

    public void execute(JMSMessage msg) {
	AuditorRunnableImpl auditor = new AuditorRunnableImpl(msg);
	boolean success = false;
	while (!success) {
	    try {
		this.taskExecutor.execute(auditor);
		success = true;
	    } catch (TaskRejectedException e) {
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e1) {
		    e1.printStackTrace();
		}
	    }
	}
    }

    /**
     * Thread to run to create new audit rows.
     */
    private class AuditorRunnableImpl implements Runnable {

	private final AuditAction[] AUDITABLE = AuditMessageListenerImpl.AUDITABLE;

	private AuthenticationToken auth;
	private AuditAction action;
	private String ipAddress;
	private long objID;
	private String objKey;
	private String objName;
	private long assocObjID;
	private String associatedObjName;
	private String associatedObjKey;
	private long projectID;
	private String entryName;

	public AuditorRunnableImpl(JMSMessage msg) {
	    this.auth = msg.getAuth();
	    this.action = msg.getAuditAction();
	    this.ipAddress = msg.getIpAddress();
	    this.objID = msg.getObjectID();
	    this.objKey = msg.getObjectKey();
	    this.objName = msg.getObjectName();
	    this.assocObjID = msg.getAssocObjectID();
	    this.associatedObjName = msg.getAssocObjectName();
	    this.associatedObjKey = msg.getAssocObjectKey();
	    this.projectID = msg.getProjectID();
	    this.entryName = msg.getEntryName();
	}

	@Override
	public void run() {
	    // Get the contents.
	    // Get the user details.
	    // Get action details.

	    // If the action is auditable.
	    if (ArrayUtils.contains(AUDITABLE, action)) {

		// Company.
		Company company = auth == null ? null : auth.getCompany();
		company = auth == null ? null : company;

		// User.
		SystemUser user = auth == null ? null : auth.getUser();

		// IP Address.
		String ipAddr = (auth == null || auth.getIpAddress().isEmpty()) ? this.ipAddress
			: auth.getIpAddress();

		// Action.
		int actionID = action.id();

		// Object details.
		Long associatedObjID = assocObjID == -1 ? null : assocObjID;

		// Do the audit.
		AuditLog audit = new AuditLog(actionID, user, ipAddr, company, objName, objID);
		audit.setObjectKey(objKey);
		audit.setAssocObjName(associatedObjName);
		audit.setAssocObjID(associatedObjID);
		audit.setEntryName(entryName);
		audit.setAssocObjKey(associatedObjKey);
		if (projectID != 0) {
		    audit.setProject(new Project(projectID));
		}

		auditLogService.create(audit);
	    }
	}
    }
}
