package com.cebedo.pmsys.utils;

import com.cebedo.pmsys.enums.CSSClass;

public class HTMLUtils {

    public static String getSpanHTML(String className, String styleColor, String styleBgColor,
	    String styleBorderColor, String label) {
	String html = "<span";
	html += " class=\"label " + className + "\"";
	html += " style=\"color: " + styleColor + ";";
	html += " background-color: " + styleBgColor + ";";
	html += " border: 1px solid " + styleBorderColor + ";";
	html += " \">";
	html += " " + label + "";
	html += "</span>";
	return html;
    }

    public static String getSpanHTML(CSSClass css, String label) {
	String html = "<span";
	html += " class=\"label " + css.className() + "\"";
	html += " style=\"color: " + css.color() + ";";
	html += " background-color: " + css.backgroundColor() + ";";
	html += " border: 1px solid " + css.borderColor() + ";";
	html += " \">";
	html += " " + label + "";
	html += "</span>";
	return html;
    }

    public static String getBadgeHTML(CSSClass css, String label) {
	String html = "<span";
	html += " class=\"badge " + css.className() + "\"";
	html += " style=\"color: " + css.color() + ";";
	html += " background-color: " + css.backgroundColor() + ";";
	html += " border: 1px solid " + css.borderColor() + ";";
	html += " \">";
	html += " " + label + "";
	html += "</span>";
	return html;
    }
}
