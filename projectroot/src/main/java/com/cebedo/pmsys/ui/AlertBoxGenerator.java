package com.cebedo.pmsys.ui;

import java.util.Date;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.utils.DateUtils;

public class AlertBoxGenerator {

    public static AlertBoxGenerator SUCCESS = new AlertBoxGenerator(ConstantsSystem.UI_STATUS_SUCCESS);
    public static AlertBoxGenerator FAILED = new AlertBoxGenerator(ConstantsSystem.UI_STATUS_DANGER);
    public static String ERROR = new AlertBoxGenerator(ConstantsSystem.UI_STATUS_DANGER,
	    RegistryResponseMessage.ERROR_GENERIC).generateHTML();

    private final String CONFIG_ALERT_STATUS = "ALERT_STATUS";
    private final String CONFIG_ALERT_HEADER = "ALERT_HEADER";
    private final String CONFIG_ALERT_MESSAGE = "ALERT_MESSAGE";
    private final String CONFIG_ALERT_ICON = "ALERT_ICON";

    private static final String DELIMITER_OBJECT_TYPE = "DELIMITER_OBJECT_TYPE";
    private static final String DELIMITER_OBJECT_NAME = "DELIMITER_OBJECT_NAME";

    /**
     * Add.
     */
    private static String TEMPLATE_SUCCESS_ADD = "Successfully <b>added</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_ADD = "Failed to <b>add</b> the " + DELIMITER_OBJECT_TYPE
	    + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Pull out.
     */
    private static String TEMPLATE_SUCCESS_PULL_OUT = "Successfully <b>pulled-out</b> %s %s of <b>%s</b>.";

    private static String TEMPLATE_FAILED_PULL_OUT = "Failed to <b>pull-out</b> %s %s of <b>%s</b>.";

    /**
     * Pull out.
     */
    private static String TEMPLATE_SUCCESS_PULL_OUT_DELETE = "Successfully <b>deleted</b> the %s pull-out on <b>%s</b> by <b>%s</b>.";

    private static String TEMPLATE_FAILED_PULL_OUT_DELETE = "Failed to <b>delete</b> the %s pull-out on <b>%s</b> by <b>%s</b>.";

    /**
     * Create.
     */
    private static String TEMPLATE_SUCCESS_CREATE = "Successfully <b>created</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_CREATE = "Failed to <b>create</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Set.
     */
    private static String TEMPLATE_SUCCESS_SET = "Successfully <b>set</b> the %s <b>%s</b>.";

    private static String TEMPLATE_FAILED_SET = "Failed to <b>set</b> the %s <b>%s</b>.";

    /**
     * Upload.
     */
    private static String TEMPLATE_SUCCESS_UPLOAD = "Successfully <b>uploaded</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_UPLOAD = "Failed to <b>upload</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Include.
     */
    private static String TEMPLATE_SUCCESS_INCLUDE = "Successfully <b>included</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_INCLUDE = "Failed to <b>included</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";
    /**
     * Update.
     */
    private static String TEMPLATE_SUCCESS_UPDATE = "Successfully <b>updated</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_UPDATE = "Failed to <b>update</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Compute.
     */
    private static String TEMPLATE_SUCCESS_COMPUTE = "Successfully <b>computed</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_COMPUTE = "Failed to <b>compute</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Delete.
     */
    private static String TEMPLATE_SUCCESS_DELETE = "Successfully <b>deleted</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_DELETE = "Failed to <b>delete</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Delete profile pic.
     */
    private static String TEMPLATE_SUCCESS_DELETE_PROFILE_PIC = "Successfully <b>deleted</b> the profile pic of "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_DELETE_PROFILE_PIC = "Failed to <b>delete</b> the profile pic of "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Upload profile pic.
     */
    private static String TEMPLATE_SUCCESS_UPLOAD_PROFILE_PIC = "Successfully <b>uploaded</b> the profile pic of "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_UPLOAD_PROFILE_PIC = "Failed to <b>upload</b> the profile pic of "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Assign.
     */
    private static String TEMPLATE_SUCCESS_ASSIGN = "Successfully <b>assigned</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_ASSIGN = "Failed to <b>assign</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Assign.
     */
    private static String TEMPLATE_SUCCESS_ASSIGN_ENTRIES = "Successfully <b>assigned</b> "
	    + DELIMITER_OBJECT_TYPE + " entries.";

    private static String TEMPLATE_FAILED_ASSIGN_ENTRIES = "Failed to <b>assign</b> "
	    + DELIMITER_OBJECT_TYPE + " entries.";

    /**
     * Assign.
     */
    private static String TEMPLATE_SUCCESS_CREATE_ENTRIES = "Successfully <b>created</b> %s entries.";

    private static String TEMPLATE_FAILED_CREATE_ENTRIES = "Failed to <b>create</b> %s entries.";

    /**
     * Unassign.
     */
    private static String TEMPLATE_SUCCESS_UNASSIGN = "Successfully <b>unassigned</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_UNASSIGN = "Failed to <b>unassign</b> the "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Unassign All.
     */
    private static String TEMPLATE_SUCCESS_UNASSIGN_ALL = "Successfully <b>unassigned all</b> "
	    + DELIMITER_OBJECT_TYPE + " entries.";

    private static String TEMPLATE_FAILED_UNASSIGN_ALL = "Failed to <b>unassign all</b> "
	    + DELIMITER_OBJECT_TYPE + " entries.";

    /**
     * Delete All.
     */
    private static String TEMPLATE_SUCCESS_DELETE_ALL = "Successfully <b>deleted all</b> "
	    + DELIMITER_OBJECT_TYPE + " entries.";

    private static String TEMPLATE_FAILED_DELETE_ALL = "Failed to <b>delete all</b> "
	    + DELIMITER_OBJECT_TYPE + " entries.";

    /**
     * Mark As.
     */
    private static String TEMPLATE_SUCCESS_MARK_AS = "Successfully <b>updated the status</b> of "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    private static String TEMPLATE_FAILED_MARK_AS = "Failed to <b>update the status</b> of "
	    + DELIMITER_OBJECT_TYPE + " <b>" + DELIMITER_OBJECT_NAME + "</b>.";

    /**
     * Template.
     */
    private final String TEMPLATE = "<div class=\"alert alert-"
	    + CONFIG_ALERT_STATUS
	    + " alert-dismissable\"><i class=\"fa fa-"
	    + CONFIG_ALERT_ICON
	    + "\"></i><button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button><h4>"
	    + CONFIG_ALERT_HEADER + "</h4>" + CONFIG_ALERT_MESSAGE + "</div>";

    private String status;
    private String icon;
    private String header;
    private String message;

    public AlertBoxGenerator(String stat) {
	setStatus(stat);
    }

    public AlertBoxGenerator(String stat, String msg) {
	setStatus(stat);
	setMessage(msg);
    }

    public AlertBoxGenerator() {
	;
    }

    public String generateDeleteProfilePic(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_DELETE_PROFILE_PIC.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_DELETE_PROFILE_PIC.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateUploadProfilePic(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_UPLOAD_PROFILE_PIC.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_UPLOAD_PROFILE_PIC.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateDelete(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_DELETE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_DELETE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateMarkAs(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_MARK_AS.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_MARK_AS.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateDeleteAll(String object) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_DELETE_ALL.replace(DELIMITER_OBJECT_TYPE, object);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_DELETE_ALL.replace(DELIMITER_OBJECT_TYPE, object);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateUnassignAll(String object) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_UNASSIGN_ALL.replace(DELIMITER_OBJECT_TYPE, object);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_UNASSIGN_ALL.replace(DELIMITER_OBJECT_TYPE, object);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateUnassign(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_UNASSIGN.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_UNASSIGN.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateAssign(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_ASSIGN.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_ASSIGN.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateAssignEntries(String object) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_ASSIGN_ENTRIES.replace(DELIMITER_OBJECT_TYPE, object);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_ASSIGN_ENTRIES.replace(DELIMITER_OBJECT_TYPE, object);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateCreateEntries(String object) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = String.format(TEMPLATE_FAILED_CREATE_ENTRIES, object);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = String.format(TEMPLATE_SUCCESS_CREATE_ENTRIES, object);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateInclude(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_INCLUDE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_INCLUDE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	    result += " Please compute to view results.";
	}
	this.message = result;
	return generateHTML();
    }

    public String generateUpdatePayroll(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_UPDATE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_UPDATE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	    result += " Please compute to view results.";
	}
	this.message = result;
	return generateHTML();
    }

    public String generateCompute(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_COMPUTE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_COMPUTE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateUpdate(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_UPDATE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_UPDATE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateUpload(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_UPLOAD.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_UPLOAD.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generatePulloutDelete(String materialName, Date datetime, String staffName) {
	String result = "";
	String dateStr = DateUtils.formatDate(datetime, "yyyy/MM/dd hh:mm a");
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = String.format(TEMPLATE_FAILED_PULL_OUT_DELETE, materialName, dateStr, staffName);

	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = String.format(TEMPLATE_SUCCESS_PULL_OUT_DELETE, materialName, dateStr, staffName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generatePullout(double quantity, String units, String materialName) {
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = String.format(TEMPLATE_FAILED_PULL_OUT, quantity, units, materialName);

	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = String.format(TEMPLATE_SUCCESS_PULL_OUT, quantity, units, materialName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateAdd(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_ADD.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_ADD.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateCreate(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = TEMPLATE_FAILED_CREATE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = TEMPLATE_SUCCESS_CREATE.replace(DELIMITER_OBJECT_TYPE, object).replace(
		    DELIMITER_OBJECT_NAME, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateSet(String object, String objName) {
	object = object.toLowerCase();
	String result = "";
	if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    result = String.format(TEMPLATE_FAILED_SET, object, objName);
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    result = String.format(TEMPLATE_SUCCESS_SET, object, objName);
	}
	this.message = result;
	return generateHTML();
    }

    public String generateHTML() {
	String output = TEMPLATE.replace(CONFIG_ALERT_STATUS, getStatus());
	output = output.replace(CONFIG_ALERT_ICON, getIcon());
	output = output.replace(CONFIG_ALERT_HEADER, getHeader());
	output = output.replace(CONFIG_ALERT_MESSAGE, getMessage());
	return output;
    }

    public String getStatus() {
	if (status == null) {
	    return "info";
	}
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getIcon() {
	if (icon != null) {
	    return icon;
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_INFO)) {
	    setIcon("info");
	    return icon;
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    setIcon("ban");
	    return icon;
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_WARNING)) {
	    setIcon("warning");
	    return icon;
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    setIcon("check");
	    return icon;
	}
	return "info";
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public String getHeader() {
	if (header != null) {
	    return header;
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_INFO)) {
	    setHeader("Notice");
	    return header;
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_DANGER)) {
	    setHeader("Error!");
	    return header;
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_WARNING)) {
	    setHeader("Warning");
	    return header;
	} else if (this.status.equals(ConstantsSystem.UI_STATUS_SUCCESS)) {
	    setHeader("Success!");
	    return header;
	}
	return "Notification";
    }

    public void setHeader(String header) {
	this.header = header;
    }

    public String getMessage() {
	if (message == null) {
	    return "We hope you're having a great time!";
	}
	return message;
    }

    public AlertBoxGenerator setMessage(String message) {
	this.message = message;
	return this;
    }

    public String generateHTML(String msg) {
	setMessage(msg);
	return generateHTML();
    }

}
