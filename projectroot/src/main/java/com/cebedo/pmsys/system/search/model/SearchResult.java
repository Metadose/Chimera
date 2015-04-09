package com.cebedo.pmsys.system.search.model;

import org.apache.commons.lang.StringEscapeUtils;

public class SearchResult {

	private String id;
	private String text;
	private String objectName;
	private String objectID;

	public SearchResult() {
		;
	}

	public SearchResult(String text, String objName, String objID, String id) {
		setText(StringEscapeUtils.escapeHtml(text));
		setObjectName(StringEscapeUtils.escapeHtml(objName));
		setObjectID(StringEscapeUtils.escapeHtml(objID));
		setId(StringEscapeUtils.escapeHtml(id));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

}
