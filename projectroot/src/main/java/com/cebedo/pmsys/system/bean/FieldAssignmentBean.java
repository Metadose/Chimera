package com.cebedo.pmsys.system.bean;

public class FieldAssignmentBean {

	private long projectID;
	private long fieldID;
	private String label;
	private String value;

	public FieldAssignmentBean() {
		;
	}

	public FieldAssignmentBean(int id, int fID) {
		setProjectID(fID);
		setFieldID(fID);
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
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
