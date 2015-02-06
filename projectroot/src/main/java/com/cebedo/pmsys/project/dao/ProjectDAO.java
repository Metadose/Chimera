package com.cebedo.pmsys.project.dao;

import java.util.List;

import com.cebedo.pmsys.project.model.Project;

public interface ProjectDAO {

	public void save(Project p);

	public List<Project> list();

}