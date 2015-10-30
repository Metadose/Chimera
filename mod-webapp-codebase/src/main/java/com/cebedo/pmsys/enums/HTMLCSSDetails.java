package com.cebedo.pmsys.enums;

import com.cebedo.pmsys.utils.HTMLUtils;

public enum HTMLCSSDetails {

    INFO("btn-info", "Info", "#fff", "#5bc0de", "#46b8da"),

    PRIMARY("btn-primary", "Primary", "#fff", "#337ab7", "#2e6da4"),

    SUCCESS("btn-success", "Success", "#fff", "#5cb85c", "#4cae4c"),

    SUCCESS_2("btn-success-2", "Success", "#fff", "#078F07", "#4cae4c"),

    WARNING("btn-warning", "Warning", "#fff", "#f0ad4e", "#eea236"),

    DANGER("btn-danger", "Danger", "#fff", "#d9534f", "#d43f3a"),

    DEFAULT("btn-default", "Default", "#333", "#fff", "#ccc"),

    DEFAULT_HOVER("btn-default:hover", "Default Hover", "#333", "#e6e6e6", "#adadad"),

    ON_TIME("bg-green", "On-time", "#fff", "#5cb85c", "#4cae4c"),

    DELAYED("bg-red", "Delayed", "#fff", "#d9534f", "#d43f3a"),

    SPENT("bg-green", "Spent", "#fff", "#5cb85c", "#4cae4c"),

    OVERSPENT("bg-red", "Overspent", "#fff", "#d9534f", "#d43f3a");

    String className;
    String label;
    String color;
    String backgroundColor;
    String borderColor;

    public static String backgroundColorOf(String css) {
	for (HTMLCSSDetails cssClass : HTMLCSSDetails.class.getEnumConstants()) {
	    if (cssClass.className().equals(css)) {
		return cssClass.backgroundColor();
	    }
	}
	return DEFAULT.backgroundColor();
    }

    HTMLCSSDetails(String cName, String label, String color, String backgroundColor, String borderColor) {
	this.className = cName;
	this.label = label;
	this.color = color;
	this.backgroundColor = backgroundColor;
	this.borderColor = borderColor;
    }

    public static HTMLCSSDetails of(String className) {
	if (className.equals(INFO.className())) {
	    return INFO;
	}

	else if (className.equals(PRIMARY.className())) {
	    return PRIMARY;
	}

	else if (className.equals(SUCCESS.className())) {
	    return SUCCESS;
	}

	else if (className.equals(WARNING.className())) {
	    return WARNING;
	}
	return INFO;
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

    public String getSpanHTML(String spanBoxLabel, String label) {
	return getSpanHTML(spanBoxLabel) + "&nbsp;" + label;
    }

}