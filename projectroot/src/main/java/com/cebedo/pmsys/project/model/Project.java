package com.cebedo.pmsys.project.model;

import java.io.Serializable;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cebedo.pmsys.field.model.FieldAssignment;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.model.TeamAssignment;

@Entity
@Table(name = Project.TABLE_NAME)
public class Project implements Serializable {

	public static final String CLASS_NAME = "Project";
	public static final String OBJECT_NAME = "project";
	public static final String TABLE_NAME = "projects";
	public static final String COLUMN_PRIMARY_KEY = "project_id";

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private int type;
	private int status;
	private Set<ManagerAssignment> managerAssignments;
	private Set<Team> assignedTeams;
	private Set<FieldAssignment> assignedFields;
	private String thumbnailURL;
	private String location;
	private String notes;
	private Set<Task> assignedTasks;
	private Set<ProjectFile> files;

	public Project() {
		;
	}

	public Project(long id) {
		setId(id);
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

	@Column(name = "name", nullable = false, length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "type", nullable = false, length = 2)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "status", nullable = false, length = 2)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = FieldAssignment.PRIMARY_KEY
			+ ".project", cascade = CascadeType.REMOVE)
	public Set<FieldAssignment> getAssignedFields() {
		return assignedFields;
	}

	public void setAssignedFields(Set<FieldAssignment> fields) {
		this.assignedFields = fields;
	}

	/**
	 * Project to Staff with extra columns.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = ManagerAssignment.PRIMARY_KEY
			+ ".project", cascade = CascadeType.REMOVE)
	public Set<ManagerAssignment> getManagerAssignments() {
		return this.managerAssignments;
	}

	public void setManagerAssignments(Set<ManagerAssignment> man) {
		this.managerAssignments = man;
	}

	/**
	 * Project to Team many-to-many without extra columns.
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = TeamAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Team.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
	public Set<Team> getAssignedTeams() {
		return this.assignedTeams;
	}

	public void setAssignedTeams(Set<Team> teams) {
		this.assignedTeams = teams;
	}

	/**
	 * Project to Task many-to-many without extra columns.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public Set<Task> getAssignedTasks() {
		return assignedTasks;
	}

	public void setAssignedTasks(Set<Task> assignedTasks) {
		this.assignedTasks = assignedTasks;
	}

	@Column(name = "thumbnail_url", length = 255)
	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	@Column(name = "location", length = 108)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@OneToMany(mappedBy = "project")
	public Set<ProjectFile> getFiles() {
		return files;
	}

	public void setFiles(Set<ProjectFile> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "[Project] " + getId() + " [Name] " + getName();
	}
}