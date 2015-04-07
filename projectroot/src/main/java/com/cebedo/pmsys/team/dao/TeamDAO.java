package com.cebedo.pmsys.team.dao;

import java.util.List;

import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.model.TeamAssignment;

public interface TeamDAO {

	public void create(Team team);

	public Team getByID(long id);

	public void update(Team team);

	public void delete(long id);

	public List<Team> list(Long companyID);

	public void assignProjectTeam(TeamAssignment assignment);

	public void unassignProjectTeam(long projectID, long teamID);

	public void unassignAllProjectTeams(long projectID);

	public Team getWithAllCollectionsByID(long id);

	public void unassignAllMembers(long teamID);

	public void unassignAllTeamsFromProject(long teamID);

	public List<Team> listWithTasks(Long companyID);

	public String getNameByID(long teamID);
}
