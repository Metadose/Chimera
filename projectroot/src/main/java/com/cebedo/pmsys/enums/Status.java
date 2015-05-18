package com.cebedo.pmsys.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Status {
	PRESENT(1, "Present", "btn-success"), ABSENT(2, "Absent", "btn-danger"), LATE(
			3, "Late", "btn-warning"), LEAVE(4, "Leave", "btn-primary"), HALFDAY(
			5, "Half-day", "btn-info"), OVERTIME(6, "Overtime", "btn-success"), DELETE(
			-1, "Delete", "");

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

	public static List<Map<String, String>> getAllStatusInMap() {
		List<Map<String, String>> statusMap = new ArrayList<Map<String, String>>();
		for (Status stat : Status.class.getEnumConstants()) {
			Map<String, String> thisStatus = new HashMap<String, String>();
			thisStatus.put("id", String.valueOf(stat.id()));
			thisStatus.put("label", stat.label());
			statusMap.add(thisStatus);
		}
		return statusMap;
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

		} else if (idn == DELETE.id()) {
			return DELETE;
		}
		return DELETE;
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