package com.cebedo.pmsys.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TypeCalendarEvent {
    REMINDER("Reminder", "btn-info"),

    TASK("Task", "btn-primary"),

    DELIVERY("Delivery", "btn-success");

    String label;
    String css;

    TypeCalendarEvent(String lbl, String cssClass) {
	this.label = lbl;
	this.css = cssClass;
    }

    public static List<Map<String, String>> getEventTypePropertyMaps() {
	List<Map<String, String>> eventProperties = new ArrayList<Map<String, String>>();
	for (TypeCalendarEvent eventType : TypeCalendarEvent.class.getEnumConstants()) {
	    Map<String, String> propertyMap = new HashMap<String, String>();
	    propertyMap.put(eventType.label(), eventType.css());
	    eventProperties.add(propertyMap);
	}
	return eventProperties;
    }

    public static TypeCalendarEvent of(String lbl) {
	if (lbl.equals(TASK.label())) {
	    return TASK;
	}

	else if (lbl.equals(REMINDER.label())) {
	    return REMINDER;
	}

	else if (lbl.equals(DELIVERY.label())) {
	    return DELIVERY;
	}

	return TASK;
    }

    public String css() {
	return this.css;
    }

    public String label() {
	return this.label;
    }

}