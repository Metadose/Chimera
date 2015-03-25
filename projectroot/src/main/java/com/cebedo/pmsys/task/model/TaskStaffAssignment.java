package com.cebedo.pmsys.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.staff.model.Staff;

@Entity
@Table(name = TaskStaffAssignment.TABLE_NAME)
public class TaskStaffAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_task_staff";
	public static final String OBJECT_NAME = "TaskStaffAssignment";

	public static final String PROPERTY_TASK_ID = "taskID";
	public static final String PROPERTY_STAFF_ID = "staffID";

	private static final long serialVersionUID = 1L;

	private long taskID;
	private long staffID;

	@Id
	@Column(name = Task.COLUMN_PRIMARY_KEY, nullable = false)
	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}

	@Id
	@Column(name = Staff.COLUMN_PRIMARY_KEY, nullable = false)
	public long getStaffID() {
		return staffID;
	}

	public void setStaffID(long staffID) {
		this.staffID = staffID;
	}

}
