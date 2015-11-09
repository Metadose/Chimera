package com.cebedo.pmsys.enums;

public enum StatusProject {

    NEW(0, "New", "btn-info"),

    ONGOING(1, "Ongoing", "btn-primary"),

    COMPLETED(2, "Completed", "btn-success");

    String label;
    int id;
    String css;

    StatusProject(int idn) {
	this.id = idn;
    }

    StatusProject(int idn, String lbl, String cssClass) {
	this.label = lbl;
	this.id = idn;
	this.css = cssClass;
    }

    public static StatusProject of(int idn) {
	if (idn == NEW.id()) {
	    return NEW;

	} else if (idn == ONGOING.id()) {
	    return ONGOING;

	} else if (idn == COMPLETED.id()) {
	    return COMPLETED;

	}
	return NEW;
    }

    public String value() {
	return this.label;
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