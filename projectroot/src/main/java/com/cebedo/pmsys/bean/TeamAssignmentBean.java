package com.cebedo.pmsys.bean;

public class TeamAssignmentBean {

	private long teamID;
	private long taskID;

	public TeamAssignmentBean() {
		;
	}

	public TeamAssignmentBean(long sampleID) {
		setTeamID(sampleID);
	}

	public long getTeamID() {
		return teamID;
	}

	public void setTeamID(long teamID) {
		this.teamID = teamID;
	}

	public long getTaskID() {
		return taskID;
	}

	public TeamAssignmentBean setTaskID(long taskID) {
		this.taskID = taskID;
		return this;
	}

}
