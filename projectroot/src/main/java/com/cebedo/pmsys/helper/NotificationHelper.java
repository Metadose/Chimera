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
		.getUsername() : auth.getStaff().getFullName() + " "
		+ action.pastTense().toLowerCase() + " "
		+ objName.toLowerCase() + " " + name + ".";
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
