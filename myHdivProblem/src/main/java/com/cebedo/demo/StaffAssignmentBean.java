package com.cebedo.demo;

public class StaffAssignmentBean {

	private long staffID;
	private String position;

	public StaffAssignmentBean() {
		;
	}

	public StaffAssignmentBean(long sampleID, String pos) {
		setStaffID(sampleID);
		setPosition(pos);
	}

	public long getStaffID() {
		return staffID;
	}

	public void setStaffID(long staffID) {
		this.staffID = staffID;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
