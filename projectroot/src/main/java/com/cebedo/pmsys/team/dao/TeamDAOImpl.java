package com.cebedo.pmsys.team.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.team.model.Team;

@Repository
public class TeamDAOImpl implements TeamDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(TeamDAOImpl.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Team team) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(team);
		logger.info("[Create] Team: " + team);
	}

	@Override
	public Team getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Team team = (Team) session.createQuery(
				"from " + Team.TABLE_NAME + " where " + Team.COLUMN_PRIMARY_KEY
						+ "=" + id).uniqueResult();
		logger.info("[Get by ID] Team: " + team);
		return team;
	}

	@Override
	public void update(Team team) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(team);
		logger.info("[Update] Team:" + team);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Team team = getByID(id);
		if (team != null) {
			session.delete(team);
		}
		logger.info("[Delete] Team: " + team);
	}

	@SuppressWarnings("unchecked")
	public List<Team> list() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Team> teamList = session.createQuery("from " + Team.CLASS_NAME)
				.list();
		for (Team team : teamList) {
			logger.info("[List] Team: " + team);
		}
		return teamList;
	}

}
