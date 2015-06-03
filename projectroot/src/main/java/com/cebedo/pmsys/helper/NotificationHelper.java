package com.cebedo.pmsys.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class NotificationHelper {

    /**
     * Sample: John uploaded photo myphoto.jpg to project ABC Dorm.
     * 
     * @param auth
     * @param action
     * @param objName
     * @param name
     * @param file
     * @param objNameOfFile
     * @return
     */
    public String constructNotificationUpload(AuthenticationToken auth,
	    AuditAction action, String objName, String name,
	    String objNameOfFile, MultipartFile file) {

	String executor = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName();
	String actionStr = action.pastTense().toLowerCase();

	// Combine.
	String text = executor + " " + actionStr + " "
		+ objNameOfFile.toLowerCase() + " "
		+ file.getOriginalFilename() + " to " + objName.toLowerCase()
		+ " " + name + ".";

	return text;
    }

    /**
     * Sample: John deleted project ABC Dorm.
     * 
     * @param auth
     * @param action
     * @param objName
     * @param name
     * @return
     */
    public String constructNotificationText(AuthenticationToken auth,
	    AuditAction action, String objName, String name) {

	String executor = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName();
	String actionStr = action.pastTense().toLowerCase();
	String combine = (action == AuditAction.DELETE_PROFILE_PIC || action == AuditAction.UPLOAD_PROFILE_PIC) ? " of "
		: " ";

	// Combine.
	String notifTxt = executor + " ";
	notifTxt += actionStr + combine;
	notifTxt += objName.toLowerCase() + " " + name + ".";
	return notifTxt;
    }

    /**
     * Sample: John changed status of Task Excavation to Ongoing.
     * 
     * @param auth
     * @param action
     * @param objName
     * @param name
     * @param objNameAssoc
     * @return
     */
    public String constructNotificationStatusUpdate(AuthenticationToken auth,
	    String objName, String name, TaskStatus status) {

	// Sample: John assigned all Team entries under Project Rizal Dorm.
	String executor = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName();
	String statusStr = status.label();

	// Construct.
	String notifTxt = executor + " changed status of " + objName + " "
		+ name + " to " + statusStr + ".";

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
     * Assign/unassign objects.<br>
     * Sample: John assigned Team Excavators to Project Rizal Dorm.
     * 
     * @param auth
     * @param action
     * @param objName
     * @param name
     * @param objNameAssoc
     * @param nameAssoc
     * @return
     */
    public String constructNotificationAssignUnassign(AuthenticationToken auth,
	    AuditAction action, String objName, String name,
	    String objNameAssoc, String nameAssoc) {

	// Sample: John assigned Team Excavators to Project Rizal Dorm.
	String executor = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName();
	String actionStr = action.pastTense().toLowerCase();
	String link = action == AuditAction.ASSIGN ? "to" : "from";

	// Construct.
	String notifTxt = executor + " " + actionStr + " " + objNameAssoc + " "
		+ nameAssoc + " " + link + " " + objName + " " + name;

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
