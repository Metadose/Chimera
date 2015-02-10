package com.cebedo.pmsys.team.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.team.dao.TeamDAO;
import com.cebedo.pmsys.team.model.Team;

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

}
