package com.cebedo.pmsys.enums;

public enum AuditAction {

    CREATE(1, "Create", "Created"),

    UPDATE(2, "Update", "Updated"),

    DELETE(3, "Delete", "Deleted"),

    GET(4, "Get", "Get"),

    GET_WITH_COLLECTIONS(5, "Get With All Collections", "Get With All Collections"),

    LIST(6, "List", "Listed"),

    LIST_WITH_COLLECTIONS(7, "List With All Collections", "Listed With All Collections"),

    ASSIGN(8, "Assign", "Assigned"),

    UNASSIGN(9, "Unassign", "Unassigned"),

    UNASSIGN_ALL(10, "Unassign All", "Unassigned All"),

    MARK_AS(11, "Mark As", "Marked As"),

    DELETE_ALL(12, "Delete All", "Deleted All"),

    GET_PARTIAL(13, "Get With Partial Properties", "Get With Partial Properties"),

    UPLOAD(14, "Upload", "Uploaded");

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