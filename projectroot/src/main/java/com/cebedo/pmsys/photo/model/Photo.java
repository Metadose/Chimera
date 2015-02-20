package com.cebedo.pmsys.photo.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.task.model.Task;

@Entity
@Table(name = Photo.TABLE_NAME)
public class Photo implements Serializable {

	public static final String CLASS_NAME = "Photo";
	public static final String OBJECT_NAME = "photo";
	public static final String TABLE_NAME = "photos";
	public static final String COLUMN_PRIMARY_KEY = "photo_id";

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private Set<Project> projects;
	private Set<Task> tasks;

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

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "assignedPhotos")
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "photo")
	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

}
