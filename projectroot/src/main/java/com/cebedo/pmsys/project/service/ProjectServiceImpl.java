package com.cebedo.pmsys.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;

@Service
public class ProjectServiceImpl implements ProjectService {

	private ProjectDAO projectDAO;

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	@Override
	@Transactional
	public void create(Project project) {
		this.projectDAO.create(project);
	}

	@Override
	@Transactional
	public void update(Project project) {
		this.projectDAO.update(project);
	}

	@Override
	@Transactional
	public List<Project> list() {
		return this.projectDAO.list();
	}

	@Override
	@Transactional
	public Project getByID(long id) {
		return this.projectDAO.getByID(id);
	}

	@Override
	@Transactional
	public void delete(int id) {
		this.projectDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Project> listWithAllCollections() {
		return this.projectDAO.listWithAllCollections();
	}

	@Override
	@Transactional
	public Project getByIDWithAllCollections(int id) {
		return this.projectDAO.getByIDWithAllCollections(id);
	}
}