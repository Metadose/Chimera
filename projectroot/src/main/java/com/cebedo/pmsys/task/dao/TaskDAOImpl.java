package com.cebedo.pmsys.task.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.common.DAOHelper;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.model.TaskFieldAssignment;
import com.cebedo.pmsys.task.model.TaskStaffAssignment;
import com.cebedo.pmsys.task.model.TaskTeamAssignment;
import com.cebedo.pmsys.team.model.Team;

@Repository
public class TaskDAOImpl implements TaskDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(TaskDAOImpl.class);
	private SessionFactory sessionFactory;
	private DAOHelper daoHelper = new DAOHelper();

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Task task) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(task);
		logger.info("[Create] Task: " + task);
	}

	@Override
	public Task getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Task task = (Task) session.load(Task.class, new Long(id));
		logger.info("[Get by ID] Task: " + task);
		return task;
	}

	@Override
	public Task getByIDWithAllCollections(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Task task = (Task) session.load(Task.class, new Long(id));
		Hibernate.initialize(task.getTeams());
		Hibernate.initialize(task.getProject());
		Hibernate.initialize(task.getStaff());
		Hibernate.initialize(task.getFields());
		logger.info("[Get by ID] Task: " + task);
		return task;
	}

	@Override
	public void update(Task task) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(task);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Task task = getByID(id);
		if (task != null) {
			session.delete(task);
		}
		logger.info("[Delete] Task: " + task);
	}

	@SuppressWarnings("unchecked")
	public List<Task> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Task> taskList = daoHelper.getSelectQueryFilterCompany(session,
				Task.class.getName(), companyID).list();
		return taskList;
	}

	@SuppressWarnings("unchecked")
	public List<Task> listWithAllCollections(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Task> taskList = daoHelper.getSelectQueryFilterCompany(session,
				Task.class.getName(), companyID).list();
		for (Task task : taskList) {
			Hibernate.initialize(task.getTeams());
			Hibernate.initialize(task.getProject());
			Hibernate.initialize(task.getStaff());
		}
		return taskList;
	}

	@Override
	public void assignStaffTask(TaskStaffAssignment taskStaffAssign) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(taskStaffAssign);
	}

	@Override
	public void assignTeamTask(TaskTeamAssignment taskTeamAssign) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(taskTeamAssign);
	}

	@Override
	public void unassignTeamTask(long taskID, long teamID) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM "
				+ TaskTeamAssignment.OBJECT_NAME + " WHERE "
				+ TaskTeamAssignment.PROPERTY_TASK_ID + "=:"
				+ TaskTeamAssignment.PROPERTY_TASK_ID + " AND "
				+ TaskTeamAssignment.PROPERTY_TEAM_ID + "=:"
				+ TaskTeamAssignment.PROPERTY_TEAM_ID);
		query.setParameter(TaskTeamAssignment.PROPERTY_TASK_ID, taskID);
		query.setParameter(TaskTeamAssignment.PROPERTY_TEAM_ID, teamID);
		query.executeUpdate();
	}

	/**
	 * Delete all team assignments in a specific task.
	 */
	@Override
	public void unassignAllTeamTasks(long taskID) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM "
				+ TaskTeamAssignment.OBJECT_NAME + " WHERE "
				+ TaskTeamAssignment.PROPERTY_TASK_ID + "=:"
				+ TaskTeamAssignment.PROPERTY_TASK_ID);
		query.setParameter(TaskTeamAssignment.PROPERTY_TASK_ID, taskID);
		query.executeUpdate();
	}

	@Override
	public void unassignStaffTask(long taskID, long staffID) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM "
				+ TaskStaffAssignment.OBJECT_NAME + " WHERE "
				+ TaskStaffAssignment.PROPERTY_TASK_ID + "=:"
				+ TaskStaffAssignment.PROPERTY_TASK_ID + " AND "
				+ TaskStaffAssignment.PROPERTY_STAFF_ID + "=:"
				+ TaskStaffAssignment.PROPERTY_STAFF_ID);
		query.setParameter(TaskStaffAssignment.PROPERTY_TASK_ID, taskID);
		query.setParameter(TaskStaffAssignment.PROPERTY_STAFF_ID, staffID);
		query.executeUpdate();
	}

	/**
	 * Unassign all staff assignments given a task ID. Remove all staff linked
	 * to a task.
	 */
	@Override
	public void unassignAllStaffTasks(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM "
				+ TaskStaffAssignment.OBJECT_NAME + " WHERE "
				+ TaskStaffAssignment.PROPERTY_TASK_ID + "=:"
				+ TaskStaffAssignment.PROPERTY_TASK_ID);
		query.setParameter(TaskStaffAssignment.PROPERTY_TASK_ID, id);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskFieldAssignment> getFieldsByTaskID(long taskID) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM "
				+ TaskFieldAssignment.TABLE_NAME + " WHERE "
				+ Task.COLUMN_PRIMARY_KEY + " = " + taskID);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Staff> getStaffByTaskID(long taskID) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM "
				+ TaskStaffAssignment.CLASS_NAME + " WHERE "
				+ Task.COLUMN_PRIMARY_KEY + " = " + taskID);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getTeamByTaskID(long taskID) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM "
				+ TaskTeamAssignment.CLASS_NAME + " WHERE "
				+ Task.COLUMN_PRIMARY_KEY + " = " + taskID);
		return query.list();
	}

	/**
	 * Delete all tasks linked to a project.
	 */
	@Override
	public void deleteAllTasksByProject(long projectID) {
		// Construct hql.
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "DELETE FROM " + Task.class.getName();
		hql += " WHERE ";
		hql += Project.COLUMN_PRIMARY_KEY + "=:" + Project.COLUMN_PRIMARY_KEY;

		// Create, set and update.
		Query query = session.createQuery(hql);
		query.setParameter(Project.COLUMN_PRIMARY_KEY, projectID);
		query.executeUpdate();
	}

	@Override
	public void merge(Task task) {
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(task);
	}

	@Override
	public String getTitleByID(long taskID) {
		Session session = this.sessionFactory.getCurrentSession();
		String result = daoHelper.getProjectionByID(session, Task.class,
				Task.PROPERTY_ID, taskID, Task.PROPERTY_TITLE);
		return result;
	}

	/**
	 * Since we don't have a map of project to task assignment, simply set the
	 * project id to null when unassigning.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void unassignAllTasksByProject(Project project) {
		// Load the task.
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Task.class);
		criteria.add(Restrictions.eq(Task.PROPERTY_PROJECT, project));
		List<Task> taskList = criteria.list();

		// To unassign, set the project to null.
		for (Task task : taskList) {
			task.setProject(null);
			session.update(task);
		}
	}

	@Override
	public void unassignTaskByProject(long taskID, Project project) {
		// Load the task.
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Task.class);
		criteria.add(Restrictions.eq(Task.PROPERTY_ID, taskID));
		criteria.add(Restrictions.eq(Task.PROPERTY_PROJECT, project));
		Task task = (Task) criteria.uniqueResult();

		// Null the project and update.
		task.setProject(null);
		session.update(task);
	}
}
