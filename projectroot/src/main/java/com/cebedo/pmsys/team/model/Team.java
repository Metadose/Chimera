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
@Table(name = Team.tableName)
public class Team implements Serializable {

	public static final String tableName = "teams";
	public static final String primaryKey = "id";

	private static final long serialVersionUID = 1L;

	private int id;
	private Set<Project> projects;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = primaryKey, unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = tableName)
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

}
