package com.cebedo.pmsys.enums;


public enum PayrollType {
    COMPANY(0, "Company"), PROJECT(1, "Project"), TASK(2, "Task");

    String label;
    int id;

    PayrollType(int idn) {
	this.id = idn;
    }

    PayrollType(int idn, String lbl) {
	this.label = lbl;
	this.id = idn;
    }

    public static PayrollType of(int idn) {
	if (idn == COMPANY.id()) {
	    return COMPANY;

	} else if (idn == PROJECT.id()) {
	    return PROJECT;

	} else if (idn == TASK.id()) {
	    return TASK;

	}
	return COMPANY;
    }

    public String value() {
	return this.label;
    }

    public String label() {
	return this.label;
    }

    public int id() {
	return this.id;
    }

}