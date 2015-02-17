package com.cebedo.pmsys.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.team.model.Team;

@Entity
@Table(name = Task.TABLE_NAME)
public class Task {

	public static final String CLASS_NAME = "Task";
	public static final String TABLE_NAME = "tasks";
	public static final String OBJECT_NAME = "task";
	public static final String COLUMN_PRIMARY_KEY = "task_id";

	private long id;
	private String content;
	private Date dateStart;
	private Date dateEnd;
	private Project project;
	private Staff staff;
	private Team team;
	private int status;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "content", nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "date_start", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	@Column(name = "date_end")
	@Temporal(TemporalType.DATE)
	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = Project.COLUMN_PRIMARY_KEY)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = Staff.COLUMN_PRIMARY_KEY)
	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = Team.COLUMN_PRIMARY_KEY)
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Column(name = "status", nullable = false)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "[Task] " + getId() + " [Content] " + getContent();
	}

}
