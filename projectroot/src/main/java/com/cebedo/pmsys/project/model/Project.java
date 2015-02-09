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
import javax.persistence.Table;

import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.model.FieldAssignments;
import com.cebedo.pmsys.staff.model.ManagerAssignments;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.model.TeamAssignments;

@Entity
@Table(name = Project.TABLE_NAME)
public class Project implements Serializable {

	public static final String OBJECT_NAME = "project";
	public static final String TABLE_NAME = "projects";
	public static final String COLUMN_PRIMARY_KEY = "project_id";

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private int type;
	private int status;
	private Set<Staff> assignedManagers;
	private Set<Team> assignedTeams;
	private Set<Field> assignedFields;

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

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = ManagerAssignments.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
	public Set<Staff> getAssignedManagers() {
		return this.assignedManagers;
	}

	public void setAssignedManagers(Set<Staff> man) {
		this.assignedManagers = man;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = TeamAssignments.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Team.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
	public Set<Team> getAssignedTeams() {
		return this.assignedTeams;
	}

	public void setAssignedTeams(Set<Team> teams) {
		this.assignedTeams = teams;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = FieldAssignments.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Field.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
	public Set<Field> getAssignedFields() {
		return assignedFields;
	}

	public void setAssignedFields(Set<Field> fields) {
		this.assignedFields = fields;
	}

	@Override
	public String toString() {
		return "[Project] " + getId() + " [Name] " + getName();
	}
}