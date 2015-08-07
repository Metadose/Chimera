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

    ASSIGN_ALL(17, "Assign All"),

    UNASSIGN(18, "Unassign"),

    UNASSIGN_ALL(19, "Unassign All"),

    ESTIMATE(20, "Estimate"),

    RENAME(21, "Rename"),

    UNAUTHORIZED(22, "Unauthorized");

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