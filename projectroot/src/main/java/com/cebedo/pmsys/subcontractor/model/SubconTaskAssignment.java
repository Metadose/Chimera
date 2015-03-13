package com.cebedo.pmsys.subcontractor.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.task.model.Task;

@Entity
@Table(name = SubconTaskAssignment.TABLE_NAME)
public class SubconTaskAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_subcon_task";
	private static final long serialVersionUID = 1L;

	private long taskID;
	private long subconID;

	@Id
	@Column(name = Task.COLUMN_PRIMARY_KEY, nullable = false)
	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}

	@Id
	@Column(name = Subcontractor.COLUMN_PRIMARY_KEY, nullable = false)
	public long getSubcontractorID() {
		return subconID;
	}

	public void setSubcontractorID(long subconID) {
		this.subconID = subconID;
	}
}
