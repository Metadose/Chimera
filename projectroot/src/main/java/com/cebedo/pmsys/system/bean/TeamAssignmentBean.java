package com.cebedo.pmsys.system.bean;

public class TeamAssignmentBean {

	private long teamID;

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

}
