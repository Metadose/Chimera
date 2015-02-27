package com.cebedo.pmsys.team.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.project.model.Project;

@Entity
@Table(name = TeamAssignment.TABLE_NAME)
public class TeamAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_project_team";
	public static final String CLASS_NAME = "TeamAssignment";

	private static final long serialVersionUID = 1L;

	private long projectID;
	private long teamID;

	@Id
	@Column(name = Project.COLUMN_PRIMARY_KEY, nullable = false)
	public long getProjectID() {
		return projectID;
	}

	public void setProjectID(long projectID) {
		this.projectID = projectID;
	}

	@Id
	@Column(name = Team.COLUMN_PRIMARY_KEY, nullable = false)
	public long getTeamID() {
		return teamID;
	}

	public void setTeamID(long teamID) {
		this.teamID = teamID;
	}

}
