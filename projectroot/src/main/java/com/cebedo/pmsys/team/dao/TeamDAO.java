package com.cebedo.pmsys.team.dao;

import java.util.List;

import com.cebedo.pmsys.team.model.Team;

public interface TeamDAO {

	public void create(Team team);

	public Team getByID(long id);

	public void update(Team team);

	public void delete(long id);

	public List<Team> list();
}
