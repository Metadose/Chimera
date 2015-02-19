package com.cebedo.pmsys.projectfile.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.projectfile.dao.ProjectFileDAO;
import com.cebedo.pmsys.projectfile.model.ProjectFile;

@Service
public class ProjectFileServiceImpl implements ProjectFileService {

	private ProjectFileDAO projectFileDAO;

	public void setProjectFileDAO(ProjectFileDAO projectFileDAO) {
		this.projectFileDAO = projectFileDAO;
	}

	@Override
	@Transactional
	public void create(ProjectFile projectFile) {
		this.projectFileDAO.create(projectFile);
	}

	@Override
	@Transactional
	public ProjectFile getByID(long id) {
		return this.projectFileDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(ProjectFile projectFile) {
		this.projectFileDAO.update(projectFile);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.projectFileDAO.delete(id);
	}

	@Override
	@Transactional
	public List<ProjectFile> list() {
		return this.projectFileDAO.list();
	}

	@Override
	@Transactional
	public List<ProjectFile> listWithAllCollections() {
		return this.projectFileDAO.listWithAllCollections();
	}

	@Override
	@Transactional
	public void updateDescription(long fileID, String description) {
		this.projectFileDAO.updateDescription(fileID, description);
	}

}
