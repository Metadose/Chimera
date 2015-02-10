package com.cebedo.pmsys.team.service;

import java.util.List;

import com.cebedo.pmsys.team.model.Team;

public interface TeamService {

	public void create(Team team);

	public Team getByID(long id);

	public void update(Team team);

	public void delete(long id);

	public List<Team> list();

}
