package com.cebedo.pmsys.enums;

public enum AuditAction {

    CREATE(1, "Create", "Created"),

    CREATE_MASS(2, "Create Mass", "Created Mass"),

    UPDATE(3, "Update", "Updated"),

    DELETE(4, "Delete", "Deleted"),

    DELETE_ALL(5, "Delete All", "Deleted All"),

    GET(6, "Get", "Get"),

    GET_JSON(7, "Get JSON", "Get JSON"),

    GET_MAP(8, "Get Map", "Get Map"),

    LIST(9, "List", "Listed"),

    ASSIGN(10, "Assign", "Assigned"),

    ASSIGN_ALL(11, "Assign All", "Assigned All"),

    UNASSIGN(12, "Unassign", "Unassigned"),

    UNASSIGN_ALL(13, "Unassign All", "Unassigned All"),

    UNAUTHORIZED(14, "Unauthorized", "Unauthorized");

    String label;
    int id;
    String pastTense;

    AuditAction(int idn) {
	this.id = idn;
    }

    AuditAction(int idn, String lbl, String pastTense) {
	this.label = lbl;
	this.id = idn;
	this.pastTense = pastTense;
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

    public String pastTense() {
	return this.pastTense;
    }

    public String label() {
	return this.label;
    }

    public int id() {
	return this.id;
    }

}