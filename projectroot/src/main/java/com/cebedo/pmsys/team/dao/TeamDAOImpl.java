package com.cebedo.pmsys.team.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.common.DAOHelper;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.model.TeamAssignment;

@Repository
public class TeamDAOImpl implements TeamDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(TeamDAOImpl.class);
	private SessionFactory sessionFactory;
	private DAOHelper daoHelper = new DAOHelper();

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
		Team team = (Team) session.load(Team.class, new Long(id));
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
	public List<Team> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Team> teamList = this.daoHelper.getSelectQueryFilterCompany(
				session, Team.class.getName(), companyID).list();
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

	/**
	 * Delete all teams inside the project.
	 */
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

		Set<Project> projects = team.getProjects();
		for (Project project : projects) {
			Hibernate.initialize(project);
		}
		return team;
	}

	@Override
	public void unassignAllMembers(long teamID) {
		Session session = this.sessionFactory.getCurrentSession();

		String hql = "DELETE FROM " + StaffTeamAssignment.class.getName()
				+ " WHERE " + Team.COLUMN_PRIMARY_KEY + "=:"
				+ Team.COLUMN_PRIMARY_KEY;
		Query query = session.createQuery(hql);
		query.setParameter(Team.COLUMN_PRIMARY_KEY, teamID);
		query.executeUpdate();
	}

	/**
	 * Delete all team assignments of the specified team.
	 */
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> listWithTasks(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Team> teamList = this.daoHelper.getSelectQueryFilterCompany(
				session, Team.class.getName(), companyID).list();
		for (Team team : teamList) {
			Hibernate.initialize(team.getTasks());
		}
		return teamList;
	}

	@Override
	public String getNameByID(long teamID) {
		Session session = this.sessionFactory.getCurrentSession();
		String result = this.daoHelper.getProjectionByID(session, Team.class,
				Team.PROPERTY_ID, teamID, Team.PROPERTY_NAME);
		return result;
	}

	@Override
	public List<String> listNames() {
		Session session = this.sessionFactory.getCurrentSession();
		return this.daoHelper.getProjectionList(session, Team.class,
				Team.PROPERTY_NAME);
	}
}
