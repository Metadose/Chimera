package com.cebedo.pmsys.payroll.domain;

public enum Status {
	PRESENT(1, "Present"), ABSENT(2, "Absent"), LATE(3, "Late"), LEAVE(4,
			"Leave"), HALFDAY(5, "Half-day"), OVERTIME(6, "Overtime");

	String label;
	int id;

	Status(int idn) {
		this.id = idn;
	}

	Status(int idn, String lbl) {
		this.label = lbl;
		this.id = idn;
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

	public int id() {
		return this.id;
	}
}