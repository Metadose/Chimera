package com.cebedo.pmsys.field.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cebedo.pmsys.project.model.Project;

@Entity
@Table(name = Field.TABLE_NAME)
public class Field implements Serializable {

	public static final String TABLE_NAME = "fields";
	public static final String COLUMN_PRIMARY_KEY = "id";

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String type;
	private Set<Project> projects;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = TABLE_NAME)
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

}
