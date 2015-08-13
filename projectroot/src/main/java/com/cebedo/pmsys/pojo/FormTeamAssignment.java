package com.cebedo.pmsys.pojo;

public class FormTeamAssignment {

	private long teamID;
	private long taskID;

	public FormTeamAssignment() {
		;
	}

	public FormTeamAssignment(long sampleID) {
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

	public FormTeamAssignment setTaskID(long taskID) {
		this.taskID = taskID;
		return this;
	}

}
