package com.cebedo.pmsys.enums;

public enum AuditAction {

    // Actions.

    ACTION_CREATE(100, "Create"),

    ACTION_CREATE_MASS(101, "Create Mass"),

    ACTION_SET(102, "Set"),

    ACTION_SET_MULTI(103, "Multi-Set"),

    ACTION_SET_IF_ABSENT(104, "Set If Absent"),

    ACTION_UPDATE(105, "Update"),

    ACTION_MERGE(106, "Merge"),

    ACTION_DELETE(107, "Delete"),

    ACTION_DELETE_COLLECTION(108, "Delete Collection"),

    ACTION_DELETE_ALL(109, "Delete All"),

    ACTION_GET(110, "Get"),

    ACTION_GET_MULTI(111, "Multi-Get"),

    ACTION_GET_JSON(112, "Get JSON"),

    ACTION_GET_MAP(113, "Get Map"),

    ACTION_LIST(114, "List"),

    ACTION_RANGE(115, "Range"),

    ACTION_ASSIGN(116, "Assign"),

    ACTION_ASSIGN_MASS(117, "Assign Mass"),

    ACTION_ASSIGN_ALL(118, "Assign All"),

    ACTION_UNASSIGN(119, "Unassign"),

    ACTION_UNASSIGN_ALL(120, "Unassign All"),

    ACTION_ESTIMATE(121, "Estimate"),

    ACTION_COMPUTE(122, "Compute"),

    ACTION_CONVERT_FILE(123, "Convert File"),

    ACTION_RENAME(124, "Rename"),

    ACTION_EXPORT(125, "Export"),

    // Login.

    LOGIN_USER_NOT_EXIST(800, "User does not exist"),

    LOGIN_COMPANY_EXPIRED(801, "Company expired"),

    LOGIN_USER_LOCKED(802, "User locked"),

    LOGIN_INVALID_PASSWORD(803, "Invalid password"),

    LOGIN_AUTHENTICATED(804, "User authenticated"),

    LOGIN_COMPANY_NOT_BETA(805, "Not Beta"),

    // Errors.

    ERROR_UNAUTHORIZED(900, "Unauthorized");

    String label;
    int id;

    AuditAction(int idn) {
	this.id = idn;
    }

    AuditAction(int idn, String lbl) {
	this.label = lbl;
	this.id = idn;
    }

    public static AuditAction of(int idn) {
	if (idn == ACTION_CREATE.id()) {
	    return ACTION_CREATE;

	} else if (idn == ACTION_UPDATE.id()) {
	    return ACTION_UPDATE;

	} else if (idn == ACTION_DELETE.id()) {
	    return ACTION_DELETE;

	}
	return null;
    }

    public String label() {
	return this.label;
    }

    public int id() {
	return this.id;
    }

}