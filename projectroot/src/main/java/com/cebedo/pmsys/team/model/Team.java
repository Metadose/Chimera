package com.cebedo.pmsys.team.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.task.model.Task;

@Entity
@Table(name = Team.TABLE_NAME)
public class Team implements Serializable {

	public static final String CLASS_NAME = "Team";
	public static final String OBJECT_NAME = "team";
	public static final String TABLE_NAME = "teams";
	public static final String COLUMN_PRIMARY_KEY = "team_id";

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private Set<Project> projects;
	private Set<Task> tasks;
	private Set<Staff> members;
	private Company company;

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

	@ManyToMany(mappedBy = "assignedTeams")
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	@ManyToMany(mappedBy = "teams")
	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	@ManyToMany(mappedBy = "teams")
	public Set<Staff> getMembers() {
		return members;
	}

	public void setMembers(Set<Staff> members) {
		this.members = members;
	}

	@ManyToOne
	@JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
