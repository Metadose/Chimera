package com.cebedo.pmsys.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.field.dao.FieldDAO;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.team.dao.TeamDAO;
import com.cebedo.pmsys.team.model.Team;

@Service
public class ProjectServiceImpl implements ProjectService {

	private ProjectDAO projectDAO;
	private FieldDAO fieldDAO;
	private StaffDAO staffDAO;
	private TeamDAO teamDAO;

	public void setFieldDAO(FieldDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}

	public void setStaffDAO(StaffDAO staffDAO) {
		this.staffDAO = staffDAO;
	}

	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

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
	public Project getByID(int id) {
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

	@Override
	@Transactional
	public List<Field> listAllFields() {
		return this.fieldDAO.list();
	}

	@Override
	@Transactional
	public List<Staff> listAllStaff() {
		return this.staffDAO.list();
	}

	@Override
	@Transactional
	public List<Team> listAllTeams() {
		return this.teamDAO.list();
	}

}