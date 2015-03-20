package com.cebedo.pmsys.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.company.dao.CompanyDAO;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;

@Service
public class ProjectServiceImpl implements ProjectService {

	private ProjectDAO projectDAO;
	private CompanyDAO companyDAO;

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	@Override
	@Transactional
	public void create(Project project) {
		this.projectDAO.create(project);
		AuthenticationToken auth = AuthUtils.getAuth();
		Company authCompany = auth.getCompany();
		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			project.setCompany(authCompany);
			this.projectDAO.update(project);
		}
	}

	@Override
	@Transactional
	public void update(Project project) {
		long companyID = this.projectDAO.getCompanyIDByID(project.getId());
		Company company = this.companyDAO.getByID(companyID);
		project.setCompany(company);

		if (AuthUtils.isActionAuthorized(project)) {
			this.projectDAO.update(project);
		}
	}

	@Override
	@Transactional
	public List<Project> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectDAO.list(null);
		}
		return this.projectDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public Project getByID(long id) {
		Project project = this.projectDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(project)) {
			return project;
		}
		return new Project();
	}

	@Override
	@Transactional
	public void delete(int id) {
		Project project = this.projectDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(project)) {
			this.projectDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Project> listWithAllCollections() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectDAO.listWithAllCollections(null);
		}
		return this.projectDAO.listWithAllCollections(token.getCompany()
				.getId());
	}

	@Override
	@Transactional
	public Project getByIDWithAllCollections(int id) {
		Project project = this.projectDAO.getByIDWithAllCollections(id);
		if (AuthUtils.isActionAuthorized(project)) {
			return project;
		}
		return new Project();
	}

	@Override
	@Transactional
	public List<Project> listWithTasks() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectDAO.listWithTasks(null);
		}
		return this.projectDAO.listWithTasks(token.getCompany().getId());
	}
}