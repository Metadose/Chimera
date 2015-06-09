package com.cebedo.pmsys.enums;

import com.cebedo.pmsys.utils.HTMLUtils;

public enum GanttElement {
    TASK_NEW("btn-info", "NEW TASK", "#fff", "#5bc0de", "#46b8da"), TASK_ONGOING(
	    "btn-primary", "ONGOING TASK", "#fff", "#337ab7", "#2e6da4"), TASK_COMPLETED(
	    "btn-success", "COMPLETED TASK", "#fff", "#5cb85c", "#4cae4c"), TASK_CANCELLED(
	    "btn-warning", "CANCELLED TASK", "#fff", "#f0ad4e", "#eea236"), TASK_FAILED(
	    "btn-danger", "FAILED TASK", "#fff", "#d9534f", "#d43f3a"), PROJECT(
	    "btn-default", "PROJECT", "#333", "#fff", "#ccc"), MILESTONE(
	    "btn-default:hover", "MILESTONE", "#333", "#e6e6e6", "#adadad"), STAFF(
	    "btn-default", "STAFF", "#333", "#A6A6A6", "#ccc");

    String className;
    String label;
    String color;
    String backgroundColor;
    String borderColor;

    GanttElement(String cName, String label, String color,
	    String backgroundColor, String borderColor) {
	this.className = cName;
	this.label = label;
	this.color = color;
	this.backgroundColor = backgroundColor;
	this.borderColor = borderColor;
    }

    public static GanttElement of(String className) {
	if (className.equals(TASK_NEW.className())) {
	    return TASK_NEW;
	}

	else if (className.equals(TASK_ONGOING.className())) {
	    return TASK_ONGOING;
	}

	else if (className.equals(TASK_COMPLETED.className())) {
	    return TASK_COMPLETED;
	}

	else if (className.equals(TASK_CANCELLED.className())) {
	    return TASK_CANCELLED;
	}
	return TASK_NEW;
    }

    public String getSpanHTML(String label) {
	return HTMLUtils.getSpanHTML(className(), color(), backgroundColor(),
		borderColor(), label);
    }

    public String getSpanHTML() {
	return HTMLUtils.getSpanHTML(className(), color(), backgroundColor(),
		borderColor(), label());
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