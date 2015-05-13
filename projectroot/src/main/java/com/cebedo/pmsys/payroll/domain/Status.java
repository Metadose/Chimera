package com.cebedo.pmsys.payroll.domain;

public enum Status {
	PRESENT(1, "Present", "btn-success"), ABSENT(2, "Absent", "btn-danger"), LATE(
			3, "Late", "btn-warning"), LEAVE(4, "Leave", "btn-default"), HALFDAY(
			5, "Half-day", "btn-info"), OVERTIME(6, "Overtime", "btn-success");

	String label;
	int id;
	String css;

	Status(int idn) {
		this.id = idn;
	}

	Status(int idn, String lbl, String cssClass) {
		this.label = lbl;
		this.id = idn;
		this.css = cssClass;
	}

	public static Status of(int idn) {
		if (idn == PRESENT.id()) {
			return PRESENT;

		} else if (idn == ABSENT.id()) {
			return ABSENT;

		} else if (idn == LATE.id()) {
			return LATE;

		} else if (idn == LEAVE.id()) {
			return LEAVE;

		} else if (idn == HALFDAY.id()) {
			return HALFDAY;

		} else if (idn == OVERTIME.id()) {
			return OVERTIME;
		}
		return ABSENT;
	}

	public String value() {
		return this.label;
	}

	public String css() {
		return this.css;
	}

	public int id() {
		return this.id;
	}

}