package com.cebedo.pmsys.enums;

public enum AuditAction {

    CREATE(1, "Create"),

    CREATE_MASS(2, "Create Mass"),

    SET(3, "Set"),

    SET_MULTI(4, "Multi-Set"),

    SET_IF_ABSENT(5, "Set If Absent"),

    UPDATE(6, "Update"),

    DELETE(7, "Delete"),

    DELETE_COLLECTION(8, "Delete Collection"),

    DELETE_ALL(9, "Delete All"),

    GET(10, "Get"),

    GET_MULTI(11, "Multi-Get"),

    GET_JSON(12, "Get JSON"),

    GET_MAP(13, "Get Map"),

    LIST(14, "List"),

    RANGE(15, "Range"),

    ASSIGN(16, "Assign"),

    ASSIGN_MASS(17, "Assign Mass"),

    ASSIGN_ALL(18, "Assign All"),

    UNASSIGN(19, "Unassign"),

    UNASSIGN_ALL(20, "Unassign All"),

    ESTIMATE(21, "Estimate"),

    COMPUTE(22, "Compute"),

    CONVERT_FILE(23, "Convert File"),

    RENAME(24, "Rename"),

    UNAUTHORIZED(25, "Unauthorized");

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
	if (idn == CREATE.id()) {
	    return CREATE;

	} else if (idn == UPDATE.id()) {
	    return UPDATE;

	} else if (idn == DELETE.id()) {
	    return DELETE;

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