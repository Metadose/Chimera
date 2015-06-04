package com.cebedo.pmsys.helper;

public class HTMLHelper {

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
