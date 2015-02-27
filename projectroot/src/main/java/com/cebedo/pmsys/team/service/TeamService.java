package com.cebedo.pmsys.team.service;

import java.util.List;

import com.cebedo.pmsys.team.model.Team;

public interface TeamService {

	public void create(Team team);

	public Team getByID(long id);

	public void update(Team team);

	public void delete(long id);

	public List<Team> list();

	public void assignProjectTeam(long projectID, long teamID);

	public void unassignProjectTeam(long projectID, long teamID);

	public void unassignAllProjectTeams(long projectID);

	public Team getWithAllCollectionsByID(int id);

}
