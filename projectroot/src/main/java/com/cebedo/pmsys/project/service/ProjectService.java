package com.cebedo.pmsys.project.service;

import java.util.List;

import com.cebedo.pmsys.project.model.Project;

public interface ProjectService {

	public void create(Project project);

	public Project getByID(int id);

	public void update(Project project);

	public void delete(int id);

	public List<Project> list();

}