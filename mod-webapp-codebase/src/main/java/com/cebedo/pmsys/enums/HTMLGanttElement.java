package com.cebedo.pmsys.enums;

import com.cebedo.pmsys.utils.HTMLUtils;

public enum HTMLGanttElement {

    TASK_NEW("btn-info", "NEW", "#fff", "#5bc0de", "#46b8da"),

    TASK_ONGOING("btn-primary", "ONGOING", "#fff", "#337ab7", "#2e6da4"),

    TASK_COMPLETED("btn-success", "COMPLETED", "#fff", "#5cb85c", "#4cae4c");

    String className;
    String label;
    String color;
    String backgroundColor;
    String borderColor;

    HTMLGanttElement(String cName, String label, String color, String backgroundColor, String borderColor) {
	this.className = cName;
	this.label = label;
	this.color = color;
	this.backgroundColor = backgroundColor;
	this.borderColor = borderColor;
    }

    public static HTMLGanttElement of(String className) {
	if (className.equals(TASK_NEW.className())) {
	    return TASK_NEW;
	}

	else if (className.equals(TASK_ONGOING.className())) {
	    return TASK_ONGOING;
	}

	else if (className.equals(TASK_COMPLETED.className())) {
	    return TASK_COMPLETED;
	}

	return TASK_NEW;
    }

    public String getSpanHTML(String label) {
	return HTMLUtils.getSpanHTML(className(), color(), backgroundColor(), borderColor(), label);
    }

    public String getSpanHTML() {
	return HTMLUtils.getSpanHTML(className(), color(), backgroundColor(), borderColor(), label());
    }

    public String className() {
	return this.className;
    }

    public String label() {
	return this.label;
    }

    public String color() {
	return this.color;
    }

    public String backgroundColor() {
	return this.backgroundColor;
    }

    public String borderColor() {
	return this.borderColor;
    }

}