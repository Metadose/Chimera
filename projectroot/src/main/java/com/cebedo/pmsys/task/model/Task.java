package com.cebedo.pmsys.task.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.Staff;

@Entity
@Table(name = Task.TABLE_NAME)
public class Task {

	public static final String CLASS_NAME = "Task";
	public static final String TABLE_NAME = "tasks";
	public static final String OBJECT_NAME = "task";
	public static final String COLUMN_PRIMARY_KEY = "task_id";

	private long id;
	private String taskContent;
	private Timestamp datetimeStart;
	private Timestamp datetimeEnd;
	private Project project;
	private Staff staff;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "task_content", nullable = false)
	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

	@Column(name = "datetime_start", nullable = false)
	public Timestamp getDatetimeStart() {
		return datetimeStart;
	}

	public void setDatetimeStart(Timestamp datetimeStart) {
		this.datetimeStart = datetimeStart;
	}

	@Column(name = "datetime_end")
	public Timestamp getDatetimeEnd() {
		return datetimeEnd;
	}

	public void setDatetimeEnd(Timestamp datetimeEnd) {
		this.datetimeEnd = datetimeEnd;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = Project.COLUMN_PRIMARY_KEY, nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = Staff.COLUMN_PRIMARY_KEY, nullable = false)
	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

}
