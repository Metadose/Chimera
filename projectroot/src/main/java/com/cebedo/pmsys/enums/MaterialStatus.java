package com.cebedo.pmsys.enums;

public enum MaterialStatus {
    NOT_USED(0, "NOT USED", "btn-cebedo-adrift-in-dreams-1"), USED(1, "USED",
	    "btn-cebedo-adrift-in-dreams-2");

    int id;
    String label;
    String css;

    MaterialStatus(int id, String lbl, String cssClass) {
	this.id = id;
	this.label = lbl;
	this.css = cssClass;
    }

    public static MaterialStatus of(int id) {
	if (id == NOT_USED.id()) {
	    return NOT_USED;
	}

	else if (id == USED.id()) {
	    return USED;
	}

	return NOT_USED;
    }

    public static MaterialStatus of(String lbl) {
	if (lbl.equals(NOT_USED.label())) {
	    return NOT_USED;
	}

	else if (lbl.equals(USED.label())) {
	    return USED;
	}

	return NOT_USED;
    }

    public String css() {
	return this.css;
    }

    public String label() {
	return this.label;
    }

    public int id() {
	return this.id;
    }

}