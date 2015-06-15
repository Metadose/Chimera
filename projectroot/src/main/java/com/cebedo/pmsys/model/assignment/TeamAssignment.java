package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

@Entity
@Table(name = TeamAssignment.TABLE_NAME)
public class TeamAssignment implements Serializable {

    public static final String TABLE_NAME = "assignments_project_team";

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("TeamAssignment");

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
