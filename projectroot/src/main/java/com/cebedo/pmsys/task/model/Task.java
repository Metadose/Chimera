package com.cebedo.pmsys.task.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.team.model.Team;

@Entity
@Table(name = Task.TABLE_NAME)
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "tasks";
	public static final String OBJECT_NAME = "task";

	public static final String COLUMN_PRIMARY_KEY = "task_id";
	public static final String COLUMN_DATE_START = "date_start";
	public static final String COLUMN_STATUS = "status";

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_TITLE = "title";
	public static final String PROPERTY_PROJECT = "project";

	private long id;
	private String title;
	private String content;
	private Date dateStart;
	private int duration;
	private Project project;
	private Set<Staff> staff;
	private Set<Team> teams;
	private int status;
	@Deprecated
	private Set<TaskFieldAssignment> fields;
	private Company company;

	// private Set<Subcontractor> subcontractor;

	public Task() {
		;
	}

	public Task(Project proj) {
		setProject(proj);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@Column(name = "duration", nullable = false)
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = Project.COLUMN_PRIMARY_KEY)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToMany
	@JoinTable(name = TaskStaffAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
	public Set<Staff> getStaff() {
		return staff;
	}

	public void setStaff(Set<Staff> staff) {
		this.staff = staff;
	}

	@ManyToMany
	@JoinTable(name = TaskTeamAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Team.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
	public Set<Team> getTeams() {
		return teams;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}

	@Column(name = "status", nullable = false)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Deprecated
	@OneToMany(mappedBy = TaskFieldAssignment.PRIMARY_KEY + ".task", cascade = CascadeType.REMOVE)
	public Set<TaskFieldAssignment> getFields() {
		return fields;
	}

	@Deprecated
	public void setFields(Set<TaskFieldAssignment> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "[Task] " + getId() + " [Content] " + getContent();
	}

	/**
	 * Add new staff on the task.
	 * 
	 * @param stf
	 */
	public void assignStaff(Staff stf) {
		// TODO Make this function work.
		// Identify what Set object to initialize Set.
		if (staff == null) {
			Set<Staff> staffList = new TreeSet<Staff>();
			staffList.add(stf);
			setStaff(staffList);
		} else {
			this.staff.add(stf);
		}
	}

	@ManyToOne
	@JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	// @ManyToMany
	// @JoinTable(name = SubconTaskAssignment.TABLE_NAME, joinColumns = {
	// @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = {
	// @JoinColumn(name = Subcontractor.COLUMN_PRIMARY_KEY, nullable = false,
	// updatable = false) })
	// public Set<Subcontractor> getSubcontractor() {
	// return subcontractor;
	// }
	//
	// public void setSubcontractor(Set<Subcontractor> subcontractor) {
	// this.subcontractor = subcontractor;
	// }
}
