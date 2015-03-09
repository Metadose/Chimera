package com.cebedo.pmsys.staff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;
import com.cebedo.pmsys.team.dao.TeamDAO;
import com.cebedo.pmsys.team.model.Team;

@Service
public class StaffServiceImpl implements StaffService {

	private StaffDAO staffDAO;
	private ProjectDAO projectDAO;
	private TeamDAO teamDAO;

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
		AuthenticationToken auth = AuthUtils.getAuth();
		Company authCompany = auth.getCompany();

		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			staff.setCompany(authCompany);
			this.staffDAO.update(staff);
		}
	}

	@Override
	@Transactional
	public Staff getByID(long id) {
		Staff stf = this.staffDAO.getByID(id);
		if (AuthUtils.isStaffActionAuthorized(stf)) {
			return stf;
		}
		return new Staff();
	}

	@Override
	@Transactional
	public void update(Staff staff) {
		if (AuthUtils.isStaffActionAuthorized(staff)) {
			this.staffDAO.update(staff);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Staff stf = this.staffDAO.getByID(id);
		if (AuthUtils.isStaffActionAuthorized(stf)) {
			this.staffDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Staff> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.staffDAO.list(null);
		}
		return this.staffDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public List<Staff> listWithAllCollections() {
		AuthenticationToken token = AuthUtils.getAuth();
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
		if (!AuthUtils.isStaffActionAuthorized(staff)
				|| !AuthUtils.isProjectActionAuthorized(project)) {
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
		if (!AuthUtils.isStaffActionAuthorized(staff)
				|| !AuthUtils.isProjectActionAuthorized(project)) {
			return;
		}
		// If authorized, continue with the action.
		this.staffDAO.unassignProjectManager(projectID, staffID);
	}

	@Override
	@Transactional
	public void unassignAllProjectManagers(long projectID) {
		Project project = this.projectDAO.getByID(projectID);
		if (!AuthUtils.isProjectActionAuthorized(project)) {
			return;
		}
		this.staffDAO.unassignAllProjectManagers(projectID);
	}

	@Override
	@Transactional
	public Staff getWithAllCollectionsByID(int id) {
		Staff stf = this.staffDAO.getWithAllCollectionsByID(id);
		if (AuthUtils.isStaffActionAuthorized(stf)) {
			return stf;
		}
		return new Staff();
	}

	@Override
	@Transactional
	public void unassignTeam(long teamID, long staffID) {
		Team team = this.teamDAO.getByID(teamID);
		Staff staff = this.staffDAO.getByID(staffID);
		if (!AuthUtils.isStaffActionAuthorized(staff)
				|| !AuthUtils.isTeamActionAuthorized(team)) {
			return;
		}
		this.staffDAO.unassignTeam(teamID, staffID);
	}

	@Override
	@Transactional
	public void unassignAllTeams(long staffID) {
		Staff staff = this.staffDAO.getByID(staffID);
		if (!AuthUtils.isStaffActionAuthorized(staff)) {
			return;
		}
		this.staffDAO.unassignAllTeams(staffID);
	}

	@Override
	@Transactional
	public void assignTeam(StaffTeamAssignment stAssign) {
		Staff staff = this.staffDAO.getByID(stAssign.getStaffID());
		Team team = this.teamDAO.getByID(stAssign.getTeamID());
		if (!AuthUtils.isStaffActionAuthorized(staff)
				|| !AuthUtils.isTeamActionAuthorized(team)) {
			return;
		}
		this.staffDAO.assignTeam(stAssign);
	}

}
