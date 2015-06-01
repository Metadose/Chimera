package com.cebedo.pmsys.helper;

import java.util.ArrayList;
import java.util.List;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class NotificationHelper {

    public String constructNotificationText(AuthenticationToken auth,
	    AuditAction action, String objName, String name) {
	String notifTxt = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName() + " ";
	notifTxt += action.pastTense().toLowerCase() + " ";
	notifTxt += objName.toLowerCase() + " " + name + ".";
	return notifTxt;
    }

    /**
     * Sample: John assigned all Team entries under Project Rizal Dorm.
     * 
     * @param auth
     * @param action
     * @param objName
     * @param name
     * @param objNameAssoc
     * @return
     */
    public String constructNotificationUnassignAllText(
	    AuthenticationToken auth, AuditAction action, String objName,
	    String name, String objNameAssoc) {

	// Sample: John assigned all Team entries under Project Rizal Dorm.
	String executor = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName();
	String actionStr = action.pastTense().toLowerCase();

	// Construct.
	String notifTxt = executor + " " + actionStr + " " + objNameAssoc
		+ " entries under " + objName + " " + name;

	return notifTxt;
    }

    /**
     * Assign/unassign objects.
     * 
     * @param auth
     * @param action
     * @param objName
     * @param name
     * @param objNameAssoc
     * @param nameAssoc
     * @return
     */
    public String constructNotificationAssignText(AuthenticationToken auth,
	    AuditAction action, String objName, String name,
	    String objNameAssoc, String nameAssoc) {

	// Sample: John assigned Team Excavators to Project Rizal Dorm.
	String executor = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName();
	String actionStr = action.pastTense().toLowerCase();

	// Construct.
	String notifTxt = "" + executor + " " + actionStr + " " + objNameAssoc
		+ " " + nameAssoc + " to " + objName + " " + name;

	return notifTxt;
    }

    /**
     * Get notification recipients given a company.
     * 
     * @param co
     * @return
     */
    public List<Long> getRecipientsFromCompany(Company co) {
	List<Long> notificationRecipients = new ArrayList<Long>();
	if (co != null) {
	    for (Staff staff : co.getAdmins()) {
		SystemUser user = staff.getUser();
		notificationRecipients.add(user.getId());
	    }
	}
	return notificationRecipients;
    }

}
