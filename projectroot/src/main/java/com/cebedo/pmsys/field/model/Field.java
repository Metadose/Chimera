package com.cebedo.pmsys.field.model;

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
@Table(name = Field.TABLE_NAME)
public class Field implements Serializable {

	public static final String TABLE_NAME = "fields";
	public static final String COLUMN_PRIMARY_KEY = "field_id";

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private Set<Project> projects;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = true)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "name", unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "fieldAssignments")
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

}
