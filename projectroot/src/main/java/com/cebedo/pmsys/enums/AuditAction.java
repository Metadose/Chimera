package com.cebedo.pmsys.enums;

public enum AuditAction {

    CREATE(1, "Create"),

    CREATE_MASS(2, "Create Mass"),

    SET(3, "Set"),

    SET_MULTI(4, "Multi-Set"),

    SET_IF_ABSENT(5, "Set If Absent"),

    UPDATE(6, "Update"),

    MERGE(7, "Merge"),

    DELETE(8, "Delete"),

    DELETE_COLLECTION(9, "Delete Collection"),

    DELETE_ALL(10, "Delete All"),

    GET(11, "Get"),

    GET_MULTI(12, "Multi-Get"),

    GET_JSON(13, "Get JSON"),

    GET_MAP(14, "Get Map"),

    LIST(15, "List"),

    RANGE(16, "Range"),

    ASSIGN(17, "Assign"),

    ASSIGN_MASS(18, "Assign Mass"),

    ASSIGN_ALL(19, "Assign All"),

    UNASSIGN(20, "Unassign"),

    UNASSIGN_ALL(21, "Unassign All"),

    ESTIMATE(22, "Estimate"),

    COMPUTE(23, "Compute"),

    CONVERT_FILE(24, "Convert File"),

    RENAME(25, "Rename"),

    UNAUTHORIZED(26, "Unauthorized");

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