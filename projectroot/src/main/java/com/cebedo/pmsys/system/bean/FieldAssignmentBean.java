package com.cebedo.pmsys.system.bean;

public class FieldAssignmentBean {

	private long projectID;
	private long fieldID;
	private String label;
	private String value;

	public FieldAssignmentBean() {
		;
	}

	public FieldAssignmentBean(long id, long fID) {
		setProjectID(id);
		setFieldID(fID);
	}

	public FieldAssignmentBean(long projectID, long fieldID, String label,
			String value) {
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
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
