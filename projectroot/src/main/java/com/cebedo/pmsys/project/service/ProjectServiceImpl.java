package com.cebedo.pmsys.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthHelper;
import com.cebedo.pmsys.company.dao.CompanyDAO;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;

@Service
public class ProjectServiceImpl implements ProjectService {

	private AuthHelper authHelper = new AuthHelper();
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
		AuthenticationToken auth = this.authHelper.getAuth();
		Company authCompany = auth.getCompany();
		if (this.authHelper.notNullObjNotSuperAdmin(authCompany)) {
			project.setCompany(authCompany);
			this.projectDAO.update(project);
		}
	}

	@Override
	@Transactional
	public void update(Project project) {
		Company company = this.companyDAO.getCompanyByObjID(Project.TABLE_NAME,
				Project.COLUMN_PRIMARY_KEY, project.getId());
		project.setCompany(company);

		if (this.authHelper.isActionAuthorized(project)) {
			this.projectDAO.update(project);
		}
	}

	@Override
	@Transactional
	public List<Project> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectDAO.list(null);
		}
		return this.projectDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public Project getByID(long id) {
		Project project = this.projectDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(project)) {
			return project;
		}
		return new Project();
	}

	@Override
	@Transactional
	public void delete(long id) {
		Project project = this.projectDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(project)) {
			this.projectDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Project> listWithAllCollections() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectDAO.listWithAllCollections(null);
		}
		return this.projectDAO.listWithAllCollections(token.getCompany()
				.getId());
	}

	@Override
	@Transactional
	public Project getByIDWithAllCollections(long id) {
		Project project = this.projectDAO.getByIDWithAllCollections(id);
		if (this.authHelper.isActionAuthorized(project)) {
			return project;
		}
		return new Project();
	}

	@Override
	@Transactional
	public List<Project> listWithTasks() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectDAO.listWithTasks(null);
		}
		return this.projectDAO.listWithTasks(token.getCompany().getId());
	}

	@Override
	@Transactional
	public String getNameByID(long projectID) {
		return this.projectDAO.getNameByID(projectID);
	}
}