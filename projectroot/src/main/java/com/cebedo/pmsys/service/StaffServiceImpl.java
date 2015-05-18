package com.cebedo.pmsys.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.dao.TeamDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.ProjectFile;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.model.assignment.StaffTeamAssignment;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.wrapper.StaffWrapper;

@Service
public class StaffServiceImpl implements StaffService {

	private AuthHelper authHelper = new AuthHelper();
	private StaffDAO staffDAO;
	private ProjectDAO projectDAO;
	private TeamDAO teamDAO;
	private CompanyDAO companyDAO;
	private SystemUserDAO systemUserDAO;
	private ProjectFileService projectFileService;

	public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
		this.systemUserDAO = systemUserDAO;
	}

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
		AuthenticationToken auth = this.authHelper.getAuth();
		Company authCompany = auth.getCompany();
		staff.setCompany(authCompany);
		this.staffDAO.create(staff);
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

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
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

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
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

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
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
			return StaffWrapper.unwrap(StaffWrapper
					.removeEmptyNames(wrappedStaffList));
		}
		return new ArrayList<Staff>();
	}

	@Override
	@Transactional
	public String getNameByID(long staffID) {
		return this.staffDAO.getNameByID(staffID);
	}

	@Override
	@Transactional
	public void createFromOrigin(Staff staff, String origin, String originID) {
		if (origin.equals(SystemUser.OBJECT_NAME)) {
			SystemUser user = this.systemUserDAO.getByID(Long
					.parseLong(originID));

			if (user == null) {
				staff.setCompany(this.authHelper.getAuth().getCompany());
				this.staffDAO.create(staff);
			} else {
				// Get the company from the user.
				// Update the staff.
				staff.setCompany(user.getCompany());
				this.staffDAO.create(staff);

				// If coming from the system user,
				// attach relationship with user.
				user.setStaff(staff);
				this.systemUserDAO.update(user);
			}

			return;
		}

		// Create the staff first since to attach it's relationship
		// with the company.
		AuthenticationToken auth = this.authHelper.getAuth();
		Company authCompany = auth.getCompany();
		staff.setCompany(authCompany);
		this.staffDAO.create(staff);
	}
}
