package com.cebedo.pmsys.field.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.model.FieldAssignment;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.model.TaskFieldAssignment;

@Repository
public class FieldDAOImpl implements FieldDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(FieldDAOImpl.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Field field) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(field);
		logger.info("[Create] Field: " + field);
	}

	@Override
	public Field getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Field field = (Field) session.createQuery(
				"from " + Field.CLASS_NAME + " where "
						+ Field.COLUMN_PRIMARY_KEY + "=" + id).uniqueResult();
		logger.info("[Get by ID] Field: " + field);
		return field;
	}

	@Override
	public void update(Field field) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(field);
		logger.info("[Update] Field:" + field);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Field field = getByID(id);
		if (field != null) {
			session.delete(field);
		}
		logger.info("[Delete] Field: " + field);
	}

	@SuppressWarnings("unchecked")
	public List<Field> list() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Field> fieldList = session.createQuery("from " + Field.CLASS_NAME)
				.list();
		for (Field field : fieldList) {
			logger.info("[List] Field: " + field);
		}
		return fieldList;
	}

	@SuppressWarnings("unchecked")
	public List<Field> listWithAllCollections() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Field> fieldList = session.createQuery("from " + Field.CLASS_NAME)
				.list();
		for (Field field : fieldList) {
			Hibernate.initialize(field.getFieldAssignments());
			logger.info("[List] Field: " + field);
		}
		return fieldList;
	}

	@Override
	public void assignProject(FieldAssignment fieldAssignment) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(fieldAssignment);
		logger.info("[Create] Field Assignment: " + fieldAssignment);
	}

	@Override
	public void unassignProject(long fieldID, long projID, String label,
			String value) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM "
				+ FieldAssignment.TABLE_NAME + " WHERE "
				+ Project.COLUMN_PRIMARY_KEY + " = " + projID + " AND "
				+ Field.COLUMN_PRIMARY_KEY + " = " + fieldID + " AND "
				+ Field.COLUMN_LABEL + " = '" + label + "' AND "
				+ Field.COLUMN_VALUE + " = '" + value + "'");
		query.executeUpdate();
	}

	@Override
	public void unassignAllProjects(long projectID) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM "
				+ FieldAssignment.TABLE_NAME + " WHERE "
				+ Project.COLUMN_PRIMARY_KEY + " = " + projectID);
		query.executeUpdate();
	}

	@Override
	public FieldAssignment getFieldByKeys(long projectID, long fieldID,
			String label, String value) {
		Session session = this.sessionFactory.getCurrentSession();
		FieldAssignment fieldAssignment = (FieldAssignment) session
				.createQuery(
						"FROM " + FieldAssignment.CLASS_NAME + " WHERE "
								+ Project.COLUMN_PRIMARY_KEY + " = "
								+ projectID + " AND "
								+ Field.COLUMN_PRIMARY_KEY + " = " + fieldID
								+ " AND " + Field.COLUMN_LABEL + " = '" + label
								+ "' and " + Field.COLUMN_VALUE + " =  '"
								+ value + "'").uniqueResult();
		logger.info("[Get by Keys] Field Assignment: " + fieldAssignment);
		return fieldAssignment;
	}

	@Override
	public void deleteAssignedField(long projectID, long fieldID, String label,
			String value) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM "
				+ FieldAssignment.TABLE_NAME + " WHERE "
				+ Project.COLUMN_PRIMARY_KEY + " = " + projectID + " AND "
				+ Field.COLUMN_PRIMARY_KEY + " = " + fieldID + " AND  "
				+ Field.COLUMN_LABEL + " = '" + label + "' and "
				+ Field.COLUMN_VALUE + " =  '" + value + "'");
		query.executeUpdate();
	}

	@Override
	public void assignTask(TaskFieldAssignment taskField) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(taskField);
	}

	@Override
	public void unassignAllTasks(long taskID) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM "
				+ TaskFieldAssignment.CLASS_NAME + " WHERE "
				+ Task.COLUMN_PRIMARY_KEY + "=:" + Task.COLUMN_PRIMARY_KEY);
		query.setParameter(Task.COLUMN_PRIMARY_KEY, taskID);
		query.executeUpdate();
	}

	@Override
	public void unassignTask(long fieldID, long taskID, String label,
			String value) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM "
				+ TaskFieldAssignment.TABLE_NAME + " WHERE "
				+ Task.COLUMN_PRIMARY_KEY + " = " + taskID + " AND "
				+ Field.COLUMN_PRIMARY_KEY + " = " + fieldID + " AND  "
				+ Field.COLUMN_LABEL + " = '" + label + "' AND "
				+ Field.COLUMN_VALUE + " =  '" + value + "'");
		query.executeUpdate();
	}
}
