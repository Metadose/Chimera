package com.cebedo.pmsys.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.company.dao.CompanyDAO;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.team.dao.TeamDAO;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.model.TeamAssignment;
import com.cebedo.pmsys.team.model.TeamWrapper;

@Service
public class TeamServiceImpl implements TeamService {

	private TeamDAO teamDAO;
	private ProjectDAO projectDAO;
	private CompanyDAO companyDAO;

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

	@Override
	@Transactional
	public void create(Team team) {
		this.teamDAO.create(team);
		AuthenticationToken auth = AuthUtils.getAuth();
		Company authCompany = auth.getCompany();
		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			team.setCompany(authCompany);
			this.teamDAO.update(team);
		}
	}

	@Override
	@Transactional
	public Team getByID(long id) {
		Team team = this.teamDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(team)) {
			return team;
		}
		return new Team();
	}

	@Override
	@Transactional
	public void update(Team team) {
		Company company = this.companyDAO.getCompanyByObjID(Team.TABLE_NAME,
				Team.COLUMN_PRIMARY_KEY, team.getId());
		team.setCompany(company);
		if (AuthUtils.isActionAuthorized(team)) {
			this.teamDAO.update(team);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Team team = this.teamDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(team)) {
			this.teamDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Team> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.teamDAO.list(null);
		}
		return this.teamDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public void assignProjectTeam(long projectID, long teamID) {
		Project project = this.projectDAO.getByID(projectID);
		Team team = this.teamDAO.getByID(teamID);
		if (AuthUtils.isActionAuthorized(project)
				&& AuthUtils.isActionAuthorized(team)) {
			TeamAssignment assignment = new TeamAssignment();
			assignment.setProjectID(projectID);
			assignment.setTeamID(teamID);
			this.teamDAO.assignProjectTeam(assignment);
		}
	}

	@Override
	@Transactional
	public void unassignProjectTeam(long projectID, long teamID) {
		Project project = this.projectDAO.getByID(projectID);
		Team team = this.teamDAO.getByID(teamID);
		if (AuthUtils.isActionAuthorized(project)
				&& AuthUtils.isActionAuthorized(team)) {
			this.teamDAO.unassignProjectTeam(projectID, teamID);
		}
	}

	@Override
	@Transactional
	public void unassignAllProjectTeams(long projectID) {
		Project project = this.projectDAO.getByID(projectID);
		if (AuthUtils.isActionAuthorized(project)) {
			this.teamDAO.unassignAllProjectTeams(projectID);
		}
	}

	@Override
	@Transactional
	public Team getWithAllCollectionsByID(long id) {
		Team team = this.teamDAO.getWithAllCollectionsByID(id);
		if (AuthUtils.isActionAuthorized(team)) {
			return team;
		}
		return new Team();
	}

	@Override
	@Transactional
	public void unassignAllMembers(long teamID) {
		Team team = this.teamDAO.getByID(teamID);
		if (AuthUtils.isActionAuthorized(team)) {
			this.teamDAO.unassignAllMembers(teamID);
		}
	}

	@Override
	@Transactional
	public void unassignAllTeamsFromProject(long teamID) {
		Team team = this.teamDAO.getByID(teamID);
		if (AuthUtils.isActionAuthorized(team)) {
			this.teamDAO.unassignAllTeamsFromProject(teamID);
		}
	}

	@Override
	@Transactional
	public List<Team> list(Long companyID) {
		return this.teamDAO.list(companyID);
	}

	@Override
	@Transactional
	public List<Team> listUnassignedInProject(Long companyID, Project project) {
		if (AuthUtils.isActionAuthorized(project)) {
			List<Team> companyTeamList = this.teamDAO.list(companyID);
			List<TeamWrapper> wrappedTeamList = TeamWrapper
					.wrap(companyTeamList);
			List<TeamWrapper> wrappedAssignedList = TeamWrapper.wrap(project
					.getAssignedTeams());
			wrappedTeamList.removeAll(wrappedAssignedList);
			return TeamWrapper.unwrap(wrappedTeamList);
		}
		return new ArrayList<Team>();
	}

	@Override
	@Transactional
	public List<Team> listWithTasks() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.teamDAO.listWithTasks(null);
		}
		return this.teamDAO.listWithTasks(token.getCompany().getId());
	}

}
