package com.cebedo.pmsys.team.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.team.dao.TeamDAO;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.model.TeamAssignment;

@Service
public class TeamServiceImpl implements TeamService {

	private TeamDAO teamDAO;

	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

	@Override
	@Transactional
	public void create(Team team) {
		this.teamDAO.create(team);
	}

	@Override
	@Transactional
	public Team getByID(long id) {
		return this.teamDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(Team team) {
		this.teamDAO.update(team);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.teamDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Team> list() {
		return this.teamDAO.list();
	}

	@Override
	@Transactional
	public void assignProjectTeam(long projectID, long teamID) {
		TeamAssignment assignment = new TeamAssignment();
		assignment.setProjectID(projectID);
		assignment.setTeamID(teamID);
		this.teamDAO.assignProjectTeam(assignment);
	}

	@Override
	@Transactional
	public void unassignProjectTeam(long projectID, long teamID) {
		this.teamDAO.unassignProjectTeam(projectID, teamID);
	}

	@Override
	@Transactional
	public void unassignAllProjectTeams(long projectID) {
		this.teamDAO.unassignAllProjectTeams(projectID);
	}

	@Override
	@Transactional
	public Team getWithAllCollectionsByID(int id) {
		return this.teamDAO.getWithAllCollectionsByID(id);
	}

	@Override
	@Transactional
	public void unassignAllMembers(long teamID) {
		this.teamDAO.unassignAllMembers(teamID);
	}

	@Override
	@Transactional
	public void unassignAllTeamsFromProject(long teamID) {
		this.teamDAO.unassignAllTeamsFromProject(teamID);
	}

}
