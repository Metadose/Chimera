package com.cebedo.pmsys.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.listener.AuditMessageListener;
import com.cebedo.pmsys.listener.LogMessageListener;
import com.cebedo.pmsys.listener.MailMessageListener;
import com.cebedo.pmsys.listener.MessageListenerImpl;
import com.cebedo.pmsys.listener.NotificationMessageListener;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Delivery;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.SecurityAccess;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.sender.MessageSender;
import com.cebedo.pmsys.token.AuthenticationToken;

public class MessageHelper {

    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private BeanHelper beanHelper = new BeanHelper();
    private NotificationHelper notifHelper = new NotificationHelper();

    private Map<String, Object> addMsgMapUser(Map<String, Object> messageMap,
	    long userID, String ipAddr, Company company) {
	messageMap.put(MessageListenerImpl.KEY_USER_ID, userID);
	messageMap.put(MessageListenerImpl.KEY_USER_IP_ADDR, ipAddr);
	messageMap.put(AuditMessageListener.KEY_COMPANY, company);
	return messageMap;
    }

    /**
     * Add the user's details to the message map.
     * 
     * @param messageMap
     * @param auth
     * @return
     */
    private Map<String, Object> addMsgMapUser(Map<String, Object> messageMap,
	    AuthenticationToken auth) {
	return addMsgMapUser(messageMap, auth.getUser().getId(),
		auth.getIpAddress(), auth.getCompany());
    }

    /**
     * Add the audit details to the message map.
     * 
     * @param messageMap
     * @param auditAction
     * @param objectName
     * @param objectID
     * @return
     */
    private Map<String, Object> addMsgMapAudit(Map<String, Object> messageMap,
	    int auditAction, String objectName, long objectID) {
	messageMap.put(AuditMessageListener.KEY_AUDIT_ACTION, auditAction);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_NAME, objectName);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_ID, objectID);
	return messageMap;
    }

    /**
     * Add the audit details to the message map.
     * 
     * @param messageMap
     * @param auditAction
     * @param objectName
     * @param objectID
     * @return
     */
    private Map<String, Object> addMsgMapAuditWithAssoc(
	    Map<String, Object> messageMap, int auditAction, String objectName,
	    long objectID, String objNameAssoc) {
	messageMap.put(AuditMessageListener.KEY_AUDIT_ACTION, auditAction);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_NAME, objectName);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_ID, objectID);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_NAME_ASSOC,
		objNameAssoc);
	return messageMap;
    }

    /**
     * Add the audit details to the message map.
     * 
     * @param messageMap
     * @param auditAction
     * @param objectName
     * @param objectID
     * @return
     */
    private Map<String, Object> addMsgMapAuditWithAssoc(
	    Map<String, Object> messageMap, int auditAction, String objectName,
	    long objectID, String objNameAssoc, long objIDAssoc) {
	messageMap.put(AuditMessageListener.KEY_AUDIT_ACTION, auditAction);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_NAME, objectName);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_ID, objectID);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_NAME_ASSOC,
		objNameAssoc);
	messageMap.put(AuditMessageListener.KEY_AUDIT_OBJECT_ID_ASSOC,
		objIDAssoc);
	return messageMap;
    }

    private Map<String, Object> addMsgMapLog(Map<String, Object> messageMap,
	    String objectName, String logText) {
	messageMap.put(LogMessageListener.KEY_LOG_NAME, objectName);
	messageMap.put(LogMessageListener.KEY_LOG_TEXT, logText);
	return messageMap;
    }

    /**
     * Add the notification details to the message map.
     * 
     * @param messageMap
     * @param notificationRecipients
     * @param notificationText
     * @return
     */
    private Map<String, Object> addMsgMapNotification(
	    Map<String, Object> messageMap, List<Long> notificationRecipients,
	    String notificationText) {
	messageMap.put(NotificationMessageListener.KEY_NOTIFICATION_RECIPIENTS,
		notificationRecipients);
	messageMap.put(NotificationMessageListener.KEY_NOTIFICATION_TEXT,
		notificationText);
	return messageMap;
    }

    /**
     * Add the log details to the message map.
     * 
     * @param messageMap
     * @param objectName
     * @param auth
     * @param logText
     * @return
     */
    private Map<String, Object> addMsgMapLog(Map<String, Object> messageMap,
	    String objectName, AuthenticationToken auth, String logText) {
	return addMsgMapLog(messageMap, objectName,
		this.logHelper.logMessage(auth, logText));
    }

    /**
     * Construct the message map to be sent to the listeners.
     * 
     * @param objName
     * @param action
     * @param objID
     * @param name
     * @return
     */
    private Map<String, Object> constructAction(String objName,
	    AuditAction action, long objID, String name,
	    List<Long> notificationRecipients) {

	// Get auth object.
	// Construct needed texts.
	AuthenticationToken auth = this.authHelper.getAuth();
	String textNotif = this.notifHelper.constructNotificationText(auth,
		action, objName, name);
	String textLog = this.logHelper.constructTextActionOnObj(action,
		objName, name);

	// Add data to map.
	Map<String, Object> messageMap = new HashMap<String, Object>();
	messageMap = addMsgMapUser(messageMap, auth);
	messageMap = addMsgMapAudit(messageMap, action.id(), objName, objID);
	messageMap = addMsgMapNotification(messageMap, notificationRecipients,
		textNotif);
	messageMap = addMsgMapLog(messageMap, objName, auth, textLog);
	return messageMap;
    }

    /**
     * Construct the message map to be sent to the listeners.<br>
     * Notification sample: John assigned Team Excavators to Project Rizal Dorm.<br>
     * Log sample: (Team under Project) Assign: Excavators under ABC Dorm
     * 
     * @param objName
     * @param action
     * @param objID
     * @param name
     * @return
     */
    private Map<String, Object> constructAssignUnassign(String objName,
	    AuditAction action, long objID, String name,
	    List<Long> notificationRecipients, String objNameAssoc,
	    String nameAssoc, long objIDAssoc) {

	// Get auth object.
	// Construct needed texts.
	AuthenticationToken auth = this.authHelper.getAuth();

	// If the action is to assign.
	String textNotif = this.notifHelper
		.constructNotificationAssignUnassign(auth, action, objName,
			name, objNameAssoc, nameAssoc);
	String textLog = this.logHelper.constructTextActionOnObjWithAssoc(
		action, objName, name, objNameAssoc, nameAssoc);

	// Add data to map.
	Map<String, Object> messageMap = new HashMap<String, Object>();
	messageMap = addMsgMapUser(messageMap, auth);
	messageMap = addMsgMapAuditWithAssoc(messageMap, action.id(), objName,
		objID, objNameAssoc, objIDAssoc);
	messageMap = addMsgMapNotification(messageMap, notificationRecipients,
		textNotif);
	messageMap = addMsgMapLog(messageMap, objName, auth, textLog);

	// Return the map.
	return messageMap;
    }

    /**
     * Construct the message map to be sent to the listeners.
     * 
     * @param objName
     * @param action
     * @param objID
     * @param name
     * @return
     */
    private Map<String, Object> constructAction(String objName,
	    AuditAction action, long objID, String name) {

	// Get auth object.
	// Construct needed texts.
	AuthenticationToken auth = this.authHelper.getAuth();
	String textNotif = this.notifHelper.constructNotificationText(auth,
		action, objName, name);
	String textLog = this.logHelper.constructTextActionOnObj(action,
		objName, name);
	List<Long> notificationRecipients = this.notifHelper
		.getRecipientsFromCompany(auth.getCompany());

	// Add data to map.
	Map<String, Object> messageMap = new HashMap<String, Object>();
	messageMap = addMsgMapUser(messageMap, auth);
	messageMap = addMsgMapAudit(messageMap, action.id(), objName, objID);
	messageMap = addMsgMapNotification(messageMap, notificationRecipients,
		textNotif);
	messageMap = addMsgMapLog(messageMap, objName, auth, textLog);
	return messageMap;
    }

    /**
     * Responsible for all post-service notifications.<br>
     * Logging, auditing, notification, emailing.
     * 
     * Send messages/notifications.<br>
     * Use message brokers as instructions.<br>
     * LIKE send message to logger to log.<br>
     * AND auditor to audit.<br>
     * Fire up the message so that it would go parallel with the service.
     * 
     * @param objName
     * @param action
     * @param objID
     * @param name
     * @return
     */
    public void sendAction(String objName, AuditAction action, long objID,
	    String name) {
	// Construct the message then send.
	Map<String, Object> messageMap = constructAction(objName, action,
		objID, name);
	sendMessageMap(messageMap);
    }

    /**
     * Add a set of notification recipients.
     * 
     * @param notificationRecipients
     * @param staffMembers
     * @return
     */
    private List<Long> addNotificationRecipients(
	    List<Long> notificationRecipients, Set<Staff> staffMembers) {
	for (Staff staff : staffMembers) {
	    SystemUser user = staff.getUser();

	    // If there is no user or
	    // the user is already added.
	    if (user == null || notificationRecipients.contains(user.getId())) {
		return notificationRecipients;
	    }

	    // Else, add the user id.
	    notificationRecipients.add(user.getId());
	}
	return notificationRecipients;
    }

    /**
     * Add a notification recipient.
     * 
     * @param notificationRecipients
     * @param staff
     * @return
     */
    private List<Long> addNotificationRecipient(
	    List<Long> notificationRecipients, Staff staff) {
	SystemUser user = staff.getUser();

	// If there is no user or
	// the user is already added.
	if (user == null || notificationRecipients.contains(user.getId())) {
	    return notificationRecipients;
	}

	// Else, add the user id.
	notificationRecipients.add(user.getId());
	return notificationRecipients;
    }

    /**
     * Construct and send a message map.
     * 
     * @param action
     * @param delivery
     */
    public void sendAction(AuditAction action, Delivery delivery) {
	List<Long> notificationRecipients = new ArrayList<Long>();
	Project proj = delivery.getProject();

	// Notify all teams in the project.
	for (Team team : proj.getAssignedTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, team.getMembers());
	}

	// Notify all managers of the project.
	for (ManagerAssignment managerAssign : proj.getManagerAssignments()) {
	    Staff staff = managerAssign.getManager();
	    notificationRecipients = addNotificationRecipient(
		    notificationRecipients, staff);
	}

	// Notify all staff involved in this delivery.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, delivery.getStaff());

	// Construct the message then send.
	Map<String, Object> messageMap = constructAction(Delivery.OBJECT_NAME,
		action, delivery.getId(), delivery.getName(),
		notificationRecipients);
	sendMessageMap(messageMap);
    }

    /**
     * Construct and send a message map.
     * 
     * @param action
     * @param delivery
     */
    public void sendAction(AuditAction action, Task task) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Notify all staff involved in this task.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, task.getStaff());

	// Notify all staff in teams in this task.
	for (Team team : task.getTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, team.getMembers());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAction(Task.OBJECT_NAME,
		action, task.getId(), task.getTitle(), notificationRecipients);
	sendMessageMap(messageMap);
    }

    /**
     * Construct and send a message map.
     * 
     * @param action
     * @param delivery
     */
    public void sendAssignUnassign(AuditAction action, Team team, Task task) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	notificationRecipients = addNotificationRecipients(
		notificationRecipients, team.getMembers());

	// Notify all staff involved in this task.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, task.getStaff());

	// Notify all staff in teams in this task.
	for (Team assignedTeam : task.getTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, assignedTeam.getMembers());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		Task.OBJECT_NAME, action, task.getId(), task.getTitle(),
		notificationRecipients, Team.OBJECT_NAME, team.getName(),
		team.getId());
	sendMessageMap(messageMap);
    }

    /**
     * Construct and send a message map.
     * 
     * @param action
     * @param delivery
     */
    public void sendAssignUnassign(AuditAction action, Staff staff, Task task) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	notificationRecipients = addNotificationRecipient(
		notificationRecipients, staff);

	// Notify all staff involved in this task.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, task.getStaff());

	// Notify all staff in teams in this task.
	for (Team team : task.getTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, team.getMembers());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		Task.OBJECT_NAME, action, task.getId(), task.getTitle(),
		notificationRecipients, Staff.OBJECT_NAME, staff.getFullName(),
		staff.getId());
	sendMessageMap(messageMap);
    }

    /**
     * Construct then send the message map.
     * 
     * @param action
     * @param proj
     */
    public void sendAction(AuditAction action, Project proj) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Notify all staff assigned to task.
	for (Task task : proj.getAssignedTasks()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, task.getStaff());
	}

	// Notify all company admins.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, proj.getCompany().getAdmins());

	// Notify all teams in the project.
	for (Team team : proj.getAssignedTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, team.getMembers());
	}

	// Notify all managers of the project.
	for (ManagerAssignment managerAssign : proj.getManagerAssignments()) {
	    Staff staff = managerAssign.getManager();
	    notificationRecipients = addNotificationRecipient(
		    notificationRecipients, staff);
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAction(Project.OBJECT_NAME,
		action, proj.getId(), proj.getName(), notificationRecipients);
	sendMessageMap(messageMap);
    }

    /**
     * Send constructed message map.
     * 
     * @param action
     * @param team
     */
    public void sendAction(AuditAction action, Team team) {
	List<Long> notificationRecipients = new ArrayList<Long>();
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, team.getMembers());

	// Construct the message then send.
	Map<String, Object> messageMap = constructAction(Team.OBJECT_NAME,
		action, team.getId(), team.getName(), notificationRecipients);
	sendMessageMap(messageMap);
    }

    /**
     * Send the message map.
     * 
     * @param messageMap
     */
    private void sendMessageMap(Map<String, Object> messageMap) {
	// Get the bean
	MessageSender sender = (MessageSender) this.beanHelper
		.getBean(MessageSender.BEAN_NAME);

	// Define the destinations.
	String destinations = AuditMessageListener.MESSAGE_DESTINATION + ",";
	destinations += LogMessageListener.MESSAGE_DESTINATION + ",";
	destinations += NotificationMessageListener.MESSAGE_DESTINATION + ",";
	destinations += MailMessageListener.MESSAGE_DESTINATION;
	Queue dest = new ActiveMQQueue(destinations);

	// Send the message.
	sender.send(dest, messageMap);
    }

    /**
     * Send the message map.
     * 
     * @param messageMap
     */
    public void sendAssignUnassign(AuditAction action, Project proj, Staff staff) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	notificationRecipients = addNotificationRecipient(
		notificationRecipients, staff);

	// Notify all staff assigned to task.
	for (Task task : proj.getAssignedTasks()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, task.getStaff());
	}

	// Notify all company admins.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, proj.getCompany().getAdmins());

	// Notify all teams in the project.
	for (Team assignedTeam : proj.getAssignedTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, assignedTeam.getMembers());
	}

	// Notify all managers of the project.
	for (ManagerAssignment managerAssign : proj.getManagerAssignments()) {
	    Staff assignedStaff = managerAssign.getManager();
	    notificationRecipients = addNotificationRecipient(
		    notificationRecipients, assignedStaff);
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		Project.OBJECT_NAME, action, proj.getId(), proj.getName(),
		notificationRecipients, Staff.OBJECT_NAME, staff.getFullName(),
		staff.getId());
	sendMessageMap(messageMap);
    }

    /**
     * Send the message map.
     * 
     * @param messageMap
     */
    public void sendAssignUnassign(AuditAction action, Project proj, Team team) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Add all team members.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, team.getMembers());

	// Notify all staff assigned to task.
	for (Task task : proj.getAssignedTasks()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, task.getStaff());
	}

	// Notify all company admins.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, proj.getCompany().getAdmins());

	// Notify all teams in the project.
	for (Team assignedTeam : proj.getAssignedTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, assignedTeam.getMembers());
	}

	// Notify all managers of the project.
	for (ManagerAssignment managerAssign : proj.getManagerAssignments()) {
	    Staff staff = managerAssign.getManager();
	    notificationRecipients = addNotificationRecipient(
		    notificationRecipients, staff);
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		Project.OBJECT_NAME, action, proj.getId(), proj.getName(),
		notificationRecipients, Team.OBJECT_NAME, team.getName(),
		team.getId());
	sendMessageMap(messageMap);
    }

    /**
     * Unassign all objects under project.
     * 
     * @param action
     * @param objectName
     * @param project
     */
    public void sendUnassignAll(String objAssoc, Project proj) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Notify all staff assigned to task.
	for (Task task : proj.getAssignedTasks()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, task.getStaff());
	}

	// Notify all company admins.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, proj.getCompany().getAdmins());

	// Notify all teams in the project.
	for (Team assignedTeam : proj.getAssignedTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, assignedTeam.getMembers());
	}

	// Notify all managers of the project.
	for (ManagerAssignment managerAssign : proj.getManagerAssignments()) {
	    Staff staff = managerAssign.getManager();
	    notificationRecipients = addNotificationRecipient(
		    notificationRecipients, staff);
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructUnassignAllDeleteAll(
		Project.OBJECT_NAME, AuditAction.UNASSIGN_ALL, proj.getId(),
		proj.getName(), notificationRecipients, objAssoc);
	sendMessageMap(messageMap);
    }

    /**
     * Send message where obj is associated to multiple objects.<br>
     * Function is typically during "Unassign All".
     * 
     * @param objectName
     * @param action
     * @param id
     * @param name
     * @param notificationRecipients
     * @param objectNameAssoc
     * @return
     */
    private Map<String, Object> constructUnassignAllDeleteAll(
	    String objectName, AuditAction action, long id, String name,
	    List<Long> notificationRecipients, String objectNameAssoc) {

	// Get auth object.
	// Construct needed texts.
	AuthenticationToken auth = this.authHelper.getAuth();

	// If the action is to assign.
	String textNotif = this.notifHelper
		.constructNotificationUnassignAllText(auth, action, objectName,
			name, objectNameAssoc);
	String textLog = this.logHelper.constructTextActionOnObjWithAssoc(
		action, objectName, name, objectNameAssoc);

	// Add data to map.
	Map<String, Object> messageMap = new HashMap<String, Object>();
	messageMap = addMsgMapUser(messageMap, auth);
	messageMap = addMsgMapAuditWithAssoc(messageMap, action.id(),
		objectName, id, objectNameAssoc);
	messageMap = addMsgMapNotification(messageMap, notificationRecipients,
		textNotif);
	messageMap = addMsgMapLog(messageMap, objectName, auth, textLog);

	// Return the map.
	return messageMap;
    }

    /**
     * Send message where obj is associated to multiple objects.<br>
     * Function is typically during "Unassign All".
     * 
     * @param action
     * @param objectNameAssoc
     * @param team
     */
    public void sendUnassignAll(String objectNameAssoc, Team team) {

	// Get recipients.
	List<Long> notificationRecipients = new ArrayList<Long>();
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, team.getMembers());

	// Construct the message then send.
	Map<String, Object> messageMap = constructUnassignAllDeleteAll(
		Team.OBJECT_NAME, AuditAction.UNASSIGN_ALL, team.getId(),
		team.getName(), notificationRecipients, objectNameAssoc);
	sendMessageMap(messageMap);
    }

    /**
     * Send message where obj is associated to multiple objects.<br>
     * Function is typically during "Unassign All".
     * 
     * @param action
     * @param objectNameAssoc
     * @param team
     */
    public void sendUnassignAll(String objectNameAssoc, Task task) {

	// Get recipients.
	List<Long> notificationRecipients = new ArrayList<Long>();

	// All staff in this task.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, task.getStaff());

	// All teams in this task.
	for (Team team : task.getTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, team.getMembers());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructUnassignAllDeleteAll(
		Task.OBJECT_NAME, AuditAction.UNASSIGN_ALL, task.getId(),
		task.getTitle(), notificationRecipients, objectNameAssoc);
	sendMessageMap(messageMap);
    }

    public void sendMarkAs(AuditAction action, TaskStatus taskStatus, Task task) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Notify all staff involved in this task.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, task.getStaff());

	// Notify all staff in teams in this task.
	for (Team team : task.getTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, team.getMembers());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructMarkAs(Task.OBJECT_NAME,
		action, task, taskStatus, notificationRecipients);
	sendMessageMap(messageMap);
    }

    /**
     * Used by "Mark As" action.<br>
     * When updating status.
     * 
     * @param objName
     * @param action
     * @param task
     * @param taskStatus
     * @param notificationRecipients
     * @return
     */
    private Map<String, Object> constructMarkAs(String objName,
	    AuditAction action, Task task, TaskStatus taskStatus,
	    List<Long> notificationRecipients) {

	long objID = task.getId();
	String name = task.getTitle();

	// Get auth object.
	// Construct needed texts.
	AuthenticationToken auth = this.authHelper.getAuth();
	String textNotif = this.notifHelper.constructNotificationStatusUpdate(
		auth, objName, name, taskStatus);
	String textLog = this.logHelper.constructTextActionOnObj(action,
		objName, name);

	// Add data to map.
	Map<String, Object> messageMap = new HashMap<String, Object>();
	messageMap = addMsgMapUser(messageMap, auth);
	messageMap = addMsgMapAudit(messageMap, action.id(), objName, objID);
	messageMap = addMsgMapNotification(messageMap, notificationRecipients,
		textNotif);
	messageMap = addMsgMapLog(messageMap, objName, auth, textLog);
	return messageMap;
    }

    public void sendDeleteAll(String objAssoc, Project proj) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Notify all staff assigned to task.
	for (Task task : proj.getAssignedTasks()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, task.getStaff());
	}

	// Notify all company admins.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, proj.getCompany().getAdmins());

	// Notify all teams in the project.
	for (Team assignedTeam : proj.getAssignedTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, assignedTeam.getMembers());
	}

	// Notify all managers of the project.
	for (ManagerAssignment managerAssign : proj.getManagerAssignments()) {
	    Staff staff = managerAssign.getManager();
	    notificationRecipients = addNotificationRecipient(
		    notificationRecipients, staff);
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructUnassignAllDeleteAll(
		Project.OBJECT_NAME, AuditAction.DELETE_ALL, proj.getId(),
		proj.getName(), notificationRecipients, objAssoc);
	sendMessageMap(messageMap);
    }

    public void sendAssignUnassign(AuditAction action, Project proj, Task task) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	notificationRecipients = addNotificationRecipients(
		notificationRecipients, task.getStaff());

	// Add all team members.
	for (Team team : task.getTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, team.getMembers());
	}

	// Notify all staff assigned to task.
	for (Task assignedTask : proj.getAssignedTasks()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, assignedTask.getStaff());
	}

	// Notify all company admins.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, proj.getCompany().getAdmins());

	// Notify all teams in the project.
	for (Team assignedTeam : proj.getAssignedTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, assignedTeam.getMembers());
	}

	// Notify all managers of the project.
	for (ManagerAssignment managerAssign : proj.getManagerAssignments()) {
	    Staff staff = managerAssign.getManager();
	    notificationRecipients = addNotificationRecipient(
		    notificationRecipients, staff);
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		Project.OBJECT_NAME, action, proj.getId(), proj.getName(),
		notificationRecipients, Task.OBJECT_NAME, task.getTitle(),
		task.getId());
	sendMessageMap(messageMap);
    }

    public void sendAction(AuditAction action, SystemUser systemUser) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Add recipients.
	notificationRecipients = addNotificationRecipient(
		notificationRecipients, systemUser);
	Company co = systemUser.getCompany();
	if (co != null) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, co.getAdmins());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAction(
		SystemUser.OBJECT_NAME, action, systemUser.getId(),
		systemUser.getUsername(), notificationRecipients);
	sendMessageMap(messageMap);
    }

    /**
     * Add a system user to the list of recipients.
     * 
     * @param notificationRecipients
     * @param user
     * @return
     */
    private List<Long> addNotificationRecipient(
	    List<Long> notificationRecipients, SystemUser user) {

	// If there is no user or
	// the user is already added.
	if (user == null || notificationRecipients.contains(user.getId())) {
	    return notificationRecipients;
	}

	// Else, add the user id.
	notificationRecipients.add(user.getId());
	return notificationRecipients;
    }

    public void sendAssignUnassign(AuditAction action, SystemUser systemUser,
	    SecurityAccess secAcc) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Add recipients.
	notificationRecipients = addNotificationRecipient(
		notificationRecipients, systemUser);
	Company co = systemUser.getCompany();
	if (co != null) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, co.getAdmins());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		SystemUser.OBJECT_NAME, action, systemUser.getId(),
		systemUser.getUsername(), notificationRecipients,
		SecurityAccess.OBJECT_NAME, secAcc.getLabel(), secAcc.getId());
	sendMessageMap(messageMap);
    }

    public void sendAssignUnassign(AuditAction action, SystemUser systemUser,
	    SecurityRole secRole) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Add recipients.
	notificationRecipients = addNotificationRecipient(
		notificationRecipients, systemUser);
	Company co = systemUser.getCompany();
	if (co != null) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, co.getAdmins());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		SystemUser.OBJECT_NAME, action, systemUser.getId(),
		systemUser.getUsername(), notificationRecipients,
		SecurityRole.OBJECT_NAME, secRole.getLabel(), secRole.getId());
	sendMessageMap(messageMap);
    }

    public void sendUnassignAll(String objectNameAssoc, SystemUser systemUser) {
	// Add recipients.
	List<Long> notificationRecipients = new ArrayList<Long>();
	notificationRecipients = addNotificationRecipient(
		notificationRecipients, systemUser);
	Company co = systemUser.getCompany();
	if (co != null) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, co.getAdmins());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructUnassignAllDeleteAll(
		SystemUser.OBJECT_NAME, AuditAction.UNASSIGN_ALL,
		systemUser.getId(), systemUser.getUsername(),
		notificationRecipients, objectNameAssoc);
	sendMessageMap(messageMap);
    }

    public void sendAction(AuditAction action, Staff staff) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Add recipients.
	notificationRecipients = addNotificationRecipient(
		notificationRecipients, staff);

	// Construct the message then send.
	Map<String, Object> messageMap = constructAction(Staff.OBJECT_NAME,
		action, staff.getId(), staff.getFullName(),
		notificationRecipients);
	sendMessageMap(messageMap);
    }

    public void sendAssignUnassign(AuditAction action, Staff staff, Team team) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Add recipients.
	notificationRecipients = addNotificationRecipient(
		notificationRecipients, staff);
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, team.getMembers());

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		Staff.OBJECT_NAME, action, staff.getId(), staff.getFullName(),
		notificationRecipients, Team.OBJECT_NAME, team.getName(),
		team.getId());
	sendMessageMap(messageMap);
    }

    public void sendUnassignAll(String objectNameAssoc, Staff staff) {

	// Get recipients.
	List<Long> notificationRecipients = new ArrayList<Long>();
	notificationRecipients = addNotificationRecipient(
		notificationRecipients, staff);
	for (Team team : staff.getTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, team.getMembers());
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructUnassignAllDeleteAll(
		Staff.OBJECT_NAME, AuditAction.UNASSIGN_ALL, staff.getId(),
		staff.getFullName(), notificationRecipients, objectNameAssoc);
	sendMessageMap(messageMap);
    }

    public void sendAction(AuditAction action, Field field) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Add recipients.
	AuthenticationToken auth = this.authHelper.getAuth();
	Company co = auth.getCompany();
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, co.getAdmins());

	// Construct the message then send.
	Map<String, Object> messageMap = constructAction(Field.OBJECT_NAME,
		action, field.getId(), field.getName(), notificationRecipients);
	sendMessageMap(messageMap);
    }

    public void sendAssignUnassign(AuditAction action, Project proj,
	    FieldAssignment fieldAssignment) {
	List<Long> notificationRecipients = new ArrayList<Long>();

	// Notify all staff assigned to task.
	for (Task task : proj.getAssignedTasks()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, task.getStaff());
	}

	// Notify all company admins.
	notificationRecipients = addNotificationRecipients(
		notificationRecipients, proj.getCompany().getAdmins());

	// Notify all teams in the project.
	for (Team assignedTeam : proj.getAssignedTeams()) {
	    notificationRecipients = addNotificationRecipients(
		    notificationRecipients, assignedTeam.getMembers());
	}

	// Notify all managers of the project.
	for (ManagerAssignment managerAssign : proj.getManagerAssignments()) {
	    Staff staff = managerAssign.getManager();
	    notificationRecipients = addNotificationRecipient(
		    notificationRecipients, staff);
	}

	// Construct the message then send.
	Map<String, Object> messageMap = constructAssignUnassign(
		Project.OBJECT_NAME, action, proj.getId(), proj.getName(),
		notificationRecipients, Field.OBJECT_NAME,
		fieldAssignment.getLabel(), fieldAssignment.getField().getId());
	sendMessageMap(messageMap);
    }
}