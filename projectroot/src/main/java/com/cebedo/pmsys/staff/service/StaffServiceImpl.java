package com.cebedo.pmsys.staff.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.company.dao.CompanyDAO;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.projectfile.service.ProjectFileService;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;
import com.cebedo.pmsys.staff.model.StaffWrapper;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.system.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.team.dao.TeamDAO;
import com.cebedo.pmsys.team.model.Team;

@Service
public class StaffServiceImpl implements StaffService {

	private AuthHelper authHelper = new AuthHelper();
	private StaffDAO staffDAO;
	private ProjectDAO projectDAO;
	private TeamDAO teamDAO;
	private CompanyDAO companyDAO;
	private ProjectFileService projectFileService;

	@Autowired(required = true)
	@Qualifier(value = "projectFileService")
	public void setProjectFileService(ProjectFileService ps) {
		this.projectFileService = ps;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setStaffDAO(StaffDAO staffDAO) {
		this.staffDAO = staffDAO;
	}

	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

	@Override
	@Transactional
	public void create(Staff staff) {
		// Create the staff first since to attach it's relationship
		// with the company.
		this.staffDAO.create(staff);

		// Update it afterwards.
		AuthenticationToken auth = this.authHelper.getAuth();
		Company authCompany = auth.getCompany();

		if (this.authHelper.notNullObjNotSuperAdmin(authCompany)) {
			staff.setCompany(authCompany);
			this.staffDAO.update(staff);
		}
	}

	@Override
	@Transactional
	public Staff getByID(long id) {
		Staff stf = this.staffDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(stf)) {
			return stf;
		}
		return new Staff();
	}

	@Override
	@Transactional
	public void update(Staff staff) {
		Company company = this.companyDAO.getCompanyByObjID(Staff.TABLE_NAME,
				Staff.COLUMN_PRIMARY_KEY, staff.getId());
		staff.setCompany(company);

		if (this.authHelper.isActionAuthorized(staff)) {
			this.staffDAO.update(staff);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Staff stf = this.staffDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(stf)) {
			// Check if the staff has any project files.
			Hibernate.initialize(stf.getFiles());
			for (ProjectFile file : stf.getFiles()) {

				// If not owned by a project, delete it.
				if (file.getProject() == null) {
					this.projectFileService.delete(file.getId());
					continue;
				}
				// If the file is owned by a project,
				// remove it's association with the staff.
				file.setUploader(null);
				this.projectFileService.update(file);
			}
			this.staffDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Staff> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.staffDAO.list(null);
		}
		return this.staffDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public List<Staff> listWithAllCollections() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.staffDAO.listWithAllCollections(null);
		}
		return this.staffDAO.listWithAllCollections(token.getCompany().getId());
	}

	@Override
	@Transactional
	public void assignProjectManager(long projectID, long staffID,
			String position) {
		Project project = this.projectDAO.getByID(projectID);
		Staff staff = this.staffDAO.getByID(staffID);

		// If this action is not authorized,
		// return.
		if (!this.authHelper.isActionAuthorized(staff)
				|| !this.authHelper.isActionAuthorized(project)) {
			return;
		}

		ManagerAssignment assignment = new ManagerAssignment();
		assignment.setProject(project);
		assignment.setManager(staff);
		assignment.setProjectPosition(position);
		this.staffDAO.assignProjectManager(assignment);
	}

	@Override
	@Transactional
	public void unassignProjectManager(long projectID, long staffID) {
		Project project = this.projectDAO.getByID(projectID);
		Staff staff = this.staffDAO.getByID(staffID);
		if (!this.authHelper.isActionAuthorized(staff)
				|| !this.authHelper.isActionAuthorized(project)) {
			return;
		}
		// If authorized, continue with the action.
		this.staffDAO.unassignProjectManager(projectID, staffID);
	}

	@Override
	@Transactional
	public void unassignAllProjectManagers(long projectID) {
		Project project = this.projectDAO.getByID(projectID);
		if (!this.authHelper.isActionAuthorized(project)) {
			return;
		}
		this.staffDAO.unassignAllProjectManagers(projectID);
	}

	@Override
	@Transactional
	public Staff getWithAllCollectionsByID(long id) {
		Staff stf = this.staffDAO.getWithAllCollectionsByID(id);
		if (this.authHelper.isActionAuthorized(stf)) {
			return stf;
		}
		return new Staff();
	}

	@Override
	@Transactional
	public void unassignTeam(long teamID, long staffID) {
		Team team = this.teamDAO.getByID(teamID);
		Staff staff = this.staffDAO.getByID(staffID);
		if (!this.authHelper.isActionAuthorized(staff)
				|| !this.authHelper.isActionAuthorized(team)) {
			return;
		}
		this.staffDAO.unassignTeam(teamID, staffID);
	}

	@Override
	@Transactional
	public void unassignAllTeams(long staffID) {
		Staff staff = this.staffDAO.getByID(staffID);
		if (!this.authHelper.isActionAuthorized(staff)) {
			return;
		}
		this.staffDAO.unassignAllTeams(staffID);
	}

	@Override
	@Transactional
	public void assignTeam(StaffTeamAssignment stAssign) {
		Staff staff = this.staffDAO.getByID(stAssign.getStaffID());
		Team team = this.teamDAO.getByID(stAssign.getTeamID());
		if (!this.authHelper.isActionAuthorized(staff)
				|| !this.authHelper.isActionAuthorized(team)) {
			return;
		}
		this.staffDAO.assignTeam(stAssign);
	}

	@Override
	@Transactional
	public List<Staff> list(Long companyID) {
		return this.staffDAO.list(companyID);
	}

	@Override
	@Transactional
	public List<Staff> listUnassignedInProject(Long companyID, Project project) {
		if (this.authHelper.isActionAuthorized(project)) {
			List<Staff> companyStaffList = this.staffDAO.list(companyID);
			List<StaffWrapper> wrappedStaffList = StaffWrapper
					.wrap(companyStaffList);
			List<StaffWrapper> assignedStaffList = StaffWrapper.wrap(project
					.getManagerAssignments());
			wrappedStaffList.removeAll(assignedStaffList);
			return StaffWrapper.unwrap(wrappedStaffList);
		}
		return new ArrayList<Staff>();
	}

	@Override
	@Transactional
	public String getNameByID(long staffID) {
		return this.staffDAO.getNameByID(staffID);
	}

}
