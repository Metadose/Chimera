package com.cebedo.pmsys.utils;

public class HTMLUtils {

    public static String getSpanHTML(String className, String styleColor,
	    String styleBgColor, String styleBorderColor, String label) {
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
}
