package com.cebedo.pmsys.team.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.model.TeamAssignment;

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
				"from " + Team.CLASS_NAME + " where " + Team.COLUMN_PRIMARY_KEY
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

	@Override
	public void assignProjectTeam(TeamAssignment assignment) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(assignment);
	}

	@Override
	public void unassignProjectTeam(long projectID, long teamID) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM "
				+ TeamAssignment.TABLE_NAME + " WHERE "
				+ Project.COLUMN_PRIMARY_KEY + " = " + projectID + " AND "
				+ Team.COLUMN_PRIMARY_KEY + " = " + teamID);
		query.executeUpdate();
	}

	@Override
	public void unassignAllProjectTeams(long projectID) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM "
				+ TeamAssignment.TABLE_NAME + " WHERE "
				+ Project.COLUMN_PRIMARY_KEY + " = " + projectID);
		query.executeUpdate();
	}

	@Override
	public Team getWithAllCollectionsByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Team team = (Team) session.load(Team.class, new Long(id));
		Hibernate.initialize(team.getTasks());
		Hibernate.initialize(team.getMembers());
		Hibernate.initialize(team.getProjects());
		return team;
	}

	@Override
	public void unassignAllMembers(long teamID) {
		Session session = this.sessionFactory.getCurrentSession();

		String hql = "DELETE FROM " + StaffTeamAssignment.CLASS_NAME
				+ " WHERE " + Team.COLUMN_PRIMARY_KEY + "=:"
				+ Team.COLUMN_PRIMARY_KEY;
		Query query = session.createQuery(hql);
		query.setParameter(Team.COLUMN_PRIMARY_KEY, teamID);
		query.executeUpdate();
	}

	@Override
	public void unassignAllTeamsFromProject(long teamID) {
		Session session = this.sessionFactory.getCurrentSession();

		String hql = "DELETE FROM " + TeamAssignment.class.getName();
		hql += " WHERE ";
		hql += Team.COLUMN_PRIMARY_KEY + "=:" + Team.COLUMN_PRIMARY_KEY;

		Query query = session.createQuery(hql);
		query.setParameter(Team.COLUMN_PRIMARY_KEY, teamID);
		query.executeUpdate();
	}
}
