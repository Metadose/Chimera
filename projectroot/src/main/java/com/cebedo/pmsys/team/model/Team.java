package com.cebedo.pmsys.team.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cebedo.pmsys.project.model.Project;

@Entity
@Table(name = Team.TABLE_NAME)
public class Team implements Serializable {

	public static final String TABLE_NAME = "teams";
	public static final String COLUMN_PRIMARY_KEY = "id";

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private Set<Project> projects;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 16)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = TABLE_NAME)
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

}
