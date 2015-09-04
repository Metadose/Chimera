package com.cebedo.pmsys.enums;

public enum ProjectStatus {

    NEW(0, "New", "btn-info"),

    ONGOING(1, "Ongoing", "btn-primary"),

    COMPLETED(2, "Completed (On-Time)", "btn-success"),

    DELAYED(3, "Ongoing (Delayed)", "btn-warning"),

    COMPLETED2(3, "Ongoing (Delayed)", "btn-danger");

    String label;
    int id;
    String css;

    ProjectStatus(int idn) {
	this.id = idn;
    }

    ProjectStatus(int idn, String lbl, String cssClass) {
	this.label = lbl;
	this.id = idn;
	this.css = cssClass;
    }

    public static ProjectStatus of(int idn) {
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