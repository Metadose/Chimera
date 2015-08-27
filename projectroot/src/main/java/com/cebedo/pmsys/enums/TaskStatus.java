package com.cebedo.pmsys.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TaskStatus {

    NEW(0, "New", "btn-info"),

    ONGOING(1, "Ongoing", "btn-primary"),

    COMPLETED(2, "Completed", "btn-success");

    String label;
    int id;
    String css;

    TaskStatus(int idn) {
	this.id = idn;
    }

    TaskStatus(int idn, String lbl, String cssClass) {
	this.label = lbl;
	this.id = idn;
	this.css = cssClass;
    }

    public static List<Map<String, String>> getAllTaskStatusInMap() {
	List<Map<String, String>> statusMap = new ArrayList<Map<String, String>>();
	for (TaskStatus stat : TaskStatus.class.getEnumConstants()) {
	    Map<String, String> thisStatus = new HashMap<String, String>();
	    thisStatus.put("id", String.valueOf(stat.id()));
	    thisStatus.put("label", stat.label());
	    statusMap.add(thisStatus);
	}
	return statusMap;
    }

    public static TaskStatus of(int idn) {
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