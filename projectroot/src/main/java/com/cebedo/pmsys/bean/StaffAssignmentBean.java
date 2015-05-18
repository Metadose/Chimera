package com.cebedo.pmsys.bean;


public class StaffAssignmentBean {

	private long staffID;
	private long taskID;
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

	public long getTaskID() {
		return taskID;
	}

	public StaffAssignmentBean setTaskID(long taskID) {
		this.taskID = taskID;
		return this;
	}

}
