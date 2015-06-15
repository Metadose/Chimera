package com.cebedo.pmsys.enums;

import java.util.HashMap;
import java.util.Map;

public enum MilestoneStatus {
    NOT_YET_STARTED(0, "NOT YET STARTED", "btn-cebedo-adrift-in-dreams-1"), ONGOING(
	    1, "ONGOING", "btn-cebedo-adrift-in-dreams-2"), DONE(2, "DONE",
	    "btn-cebedo-adrift-in-dreams-3");

    String label;
    int id;
    String css;

    MilestoneStatus(int idn) {
	this.id = idn;
    }

    MilestoneStatus(int idn, String lbl, String cssClass) {
	this.label = lbl;
	this.id = idn;
	this.css = cssClass;
    }

    public static Map<String, MilestoneStatus> getIdToStatusMap() {
	Map<String, MilestoneStatus> idToStatusMap = new HashMap<String, MilestoneStatus>();
	for (MilestoneStatus stat : MilestoneStatus.class.getEnumConstants()) {
	    idToStatusMap.put(stat.label(), stat);
	}
	return idToStatusMap;
    }

    public static MilestoneStatus of(int idn) {
	if (idn == NOT_YET_STARTED.id()) {
	    return NOT_YET_STARTED;

	} else if (idn == ONGOING.id()) {
	    return ONGOING;

	} else if (idn == DONE.id()) {
	    return DONE;

	}
	return NOT_YET_STARTED;
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