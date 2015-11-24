package com.cebedo.pmsys.pojo;

import org.apache.commons.lang.StringUtils;

public class FormFieldAssignment {

    private long projectID;
    private long fieldID;
    private String label;
    private String value;

    public FormFieldAssignment() {
	;
    }

    public FormFieldAssignment(long id, long fID) {
	setProjectID(id);
	setFieldID(fID);
    }

    public FormFieldAssignment(long projectID, long fieldID, String label, String value) {
	setProjectID(projectID);
	setFieldID(fieldID);
	setLabel(label);
	setValue(value);
    }

    public long getProjectID() {
	return projectID;
    }

    public void setProjectID(long projectID) {
	this.projectID = projectID;
    }

    public long getFieldID() {
	return fieldID;
    }

    public void setFieldID(long fieldID) {
	this.fieldID = fieldID;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = StringUtils.trim(label);
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = StringUtils.trim(value);
    }

    public String toString() {
	return String.format("[%s, %s, %s]", this.projectID, this.label, this.value);
    }

}
