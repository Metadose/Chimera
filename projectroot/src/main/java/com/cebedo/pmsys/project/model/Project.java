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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cebedo.pmsys.field.model.FieldAssignments;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.model.TeamAssignments;

@Entity
@Table(name = Project.tableName)
public class Project implements Serializable {

	public static final String tableName = "projects";
	public static final String primaryKey = "id";

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private int type;

	private int status;

	private Staff manager;

	private Set<Team> assignedTeams;

	private Set<FieldAssignments> fieldAssignments;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = primaryKey, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = Staff.primaryKey, nullable = false)
	public Staff getManager() {
		return this.manager;
	}

	public void setManager(Staff man) {
		this.manager = man;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = TeamAssignments.tableName, joinColumns = { @JoinColumn(name = primaryKey, nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = Team.primaryKey, nullable = false, updatable = false) })
	public Set<Team> getAssignedTeams() {
		return this.assignedTeams;
	}

	public void setAssignedTeams(Set<Team> teams) {
		this.assignedTeams = teams;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = tableName, cascade = CascadeType.ALL)
	public Set<FieldAssignments> getFieldAssignments() {
		return fieldAssignments;
	}

	public void setFieldAssignments(Set<FieldAssignments> fieldAssignments) {
		this.fieldAssignments = fieldAssignments;
	}

	@Override
	public String toString() {
		return this.toString();
	}
}