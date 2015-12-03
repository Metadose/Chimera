package com.cebedo.pmsys.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.Transactional;

import com.cebedo.pmsys.dao.AuditLogDAO;
import com.cebedo.pmsys.enums.AuditAction;

public class AuditMessageListenerImpl implements MessageListener {

    public final static String MESSAGE_DESTINATION = "system.audit";
    public static final AuditAction[] AUDITABLE = { AuditAction.ACTION_CREATE,
	    AuditAction.ACTION_CREATE_MASS, AuditAction.ACTION_SET, AuditAction.ACTION_SET_MULTI,
	    AuditAction.ACTION_SET_IF_ABSENT, AuditAction.ACTION_UPDATE, AuditAction.ACTION_MERGE,
	    AuditAction.ACTION_DELETE, AuditAction.ACTION_DELETE_COLLECTION,
	    AuditAction.ACTION_DELETE_ALL, AuditAction.ACTION_ASSIGN, AuditAction.ACTION_ASSIGN_MASS,
	    AuditAction.ACTION_ASSIGN_ALL, AuditAction.ACTION_UNASSIGN, AuditAction.ACTION_UNASSIGN_ALL,
	    AuditAction.ACTION_ESTIMATE, AuditAction.ACTION_COMPUTE };

    private AuditLogDAO auditLogDAO;

    public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
	this.auditLogDAO = auditLogDAO;
    }

    @Override
    @Transactional
    public void onMessage(Message message) {

	// if (message instanceof ActiveMQObjectMessage) {
	// JMSMessage sysMessage;
	// try {
	//
	// // Get the contents.
	// // Get the user details.
	// // Get action details.
	// sysMessage = (JMSMessage) ((ActiveMQObjectMessage)
	// message).getObject();
	// AuditAction action = sysMessage.getAuditAction();
	//
	// // If the action is auditable.
	// if (ArrayUtils.contains(AUDITABLE, action)) {
	// AuthenticationToken auth = sysMessage.getAuth();
	//
	// // Company.
	// Company company = auth == null ? null : auth.getCompany();
	// company = auth == null ? null : company;
	//
	// // User.
	// SystemUser user = auth == null ? null : auth.getUser();
	//
	// // IP Address.
	// String ipAddr = (auth == null || auth.getIpAddress().isEmpty())
	// ? sysMessage.getIpAddress() : auth.getIpAddress();
	//
	// // Action.
	// int actionID = action.id();
	//
	// // Object details.
	// String objName = sysMessage.getObjectName();
	// long objID = sysMessage.getObjectID();
	// String objKey = sysMessage.getObjectKey();
	// String associatedObjName = sysMessage.getAssocObjectName();
	// Long associatedObjID = sysMessage.getAssocObjectID() == -1 ? null
	// : sysMessage.getAssocObjectID();
	// String associatedObjKey = sysMessage.getAssocObjectKey();
	//
	// // Project reference.
	// long projectID = sysMessage.getProjectID();
	// String entryName = sysMessage.getEntryName();
	//
	// // Do the audit.
	// AuditLog audit = new AuditLog(actionID, user, ipAddr, company,
	// objName, objID);
	// audit.setObjectKey(objKey);
	// audit.setAssocObjName(associatedObjName);
	// audit.setAssocObjID(associatedObjID);
	// audit.setEntryName(entryName);
	// audit.setAssocObjKey(associatedObjKey);
	// if (projectID != 0) {
	// audit.setProject(new Project(projectID));
	// }
	// this.auditLogDAO.create(audit);
	// }
	// } catch (JMSException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// else {
	// throw new IllegalArgumentException("MessageThread must be of type
	// SystemMessage");
	// }
    }
}