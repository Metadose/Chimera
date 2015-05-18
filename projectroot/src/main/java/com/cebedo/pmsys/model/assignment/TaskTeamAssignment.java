package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;

@Entity
@Table(name = TaskTeamAssignment.TABLE_NAME)
public class TaskTeamAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_task_team";
	public static final String OBJECT_NAME = "TaskTeamAssignment";

	public static final String PROPERTY_TASK_ID = "taskID";
	public static final String PROPERTY_TEAM_ID = "teamID";

	private static final long serialVersionUID = 1L;

	private long taskID;
	private long teamID;

	@Id
	@Column(name = Task.COLUMN_PRIMARY_KEY, nullable = false)
	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long taskID) {
		this.taskID = taskID;
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
