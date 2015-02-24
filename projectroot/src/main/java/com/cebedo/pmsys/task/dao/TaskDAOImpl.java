package com.cebedo.pmsys.task.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.model.TaskStaffAssignment;
import com.cebedo.pmsys.task.model.TaskTeamAssignment;

@Repository
public class TaskDAOImpl implements TaskDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(TaskDAOImpl.class);
	private SessionFactory sessionFactory;

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
		Task task = (Task) session.get(Task.class, new Long(id));
		logger.info("[Get by ID] Task: " + task);
		return task;
	}

	@Override
	public Task getByIDWithAllCollections(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Task task = (Task) session.get(Task.class, new Long(id));
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
		logger.info("[Update] Task:" + task);
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
	public List<Task> list() {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from " + Task.CLASS_NAME);
		List<Task> taskList = query.list();
		for (Task task : taskList) {
			logger.info("[List] Task: " + task);
		}
		return taskList;
	}

	@SuppressWarnings("unchecked")
	public List<Task> listWithAllCollections() {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from " + Task.CLASS_NAME);
		List<Task> taskList = query.list();
		for (Task task : taskList) {
			Hibernate.initialize(task.getTeams());
			Hibernate.initialize(task.getProject());
			Hibernate.initialize(task.getStaff());
			logger.info("[List] Task: " + task);
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
}
