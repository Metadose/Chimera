package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.StaffFieldAssignment;
import com.cebedo.pmsys.model.assignment.TaskFieldAssignment;

@Repository
public class FieldDAOImpl implements FieldDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Field field) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(field);
    }

    @Override
    public Field getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Field field = (Field) this.daoHelper.criteriaGetObjByID(session,
		Field.class, Field.PROPERTY_ID, id).uniqueResult();
	return field;
    }

    @Override
    public void update(Field field) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(field);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Field field = getByID(id);
	if (field != null) {
	    session.delete(field);
	}
    }

    @SuppressWarnings("unchecked")
    public List<Field> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Field> fieldList = session.createQuery(
		"FROM " + Field.class.getName()).list();
	return fieldList;
    }

    @SuppressWarnings("unchecked")
    public List<Field> listWithAllCollections() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Field> fieldList = session.createQuery(
		"FROM " + Field.class.getName()).list();
	for (Field field : fieldList) {
	    Hibernate.initialize(field.getFieldAssignments());
	}
	return fieldList;
    }

    @Override
    public void assignFieldToProject(FieldAssignment fieldAssignment) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(fieldAssignment);
    }

    @Override
    public void unassignFieldFromProject(long fieldID, long projID, String label,
	    String value) {
	Session session = this.sessionFactory.getCurrentSession();
	SQLQuery query = session.createSQLQuery("DELETE FROM "
		+ FieldAssignment.TABLE_NAME + " WHERE "
		+ Project.COLUMN_PRIMARY_KEY + " =:"
		+ Project.COLUMN_PRIMARY_KEY + " AND "
		+ Field.COLUMN_PRIMARY_KEY + " =:" + Field.COLUMN_PRIMARY_KEY
		+ " AND " + Field.COLUMN_LABEL + " =:" + Field.COLUMN_LABEL
		+ " AND " + Field.COLUMN_VALUE + " =:" + Field.COLUMN_VALUE);
	query.setParameter(Project.COLUMN_PRIMARY_KEY, projID);
	query.setParameter(Field.COLUMN_PRIMARY_KEY, fieldID);
	query.setParameter(Field.COLUMN_LABEL, label);
	query.setParameter(Field.COLUMN_VALUE, value);
	query.executeUpdate();
    }

    @Override
    public void unassignAllFieldsFromProject(long projectID) {
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

	String hql = "FROM " + FieldAssignment.class.getName();
	hql += " WHERE " + Project.COLUMN_PRIMARY_KEY + " =:"
		+ Project.COLUMN_PRIMARY_KEY;
	hql += " AND " + Field.COLUMN_PRIMARY_KEY + " =:"
		+ Field.COLUMN_PRIMARY_KEY;
	hql += " AND " + Field.COLUMN_LABEL + " =:" + Field.COLUMN_LABEL;
	hql += " AND " + Field.COLUMN_VALUE + " =:" + Field.COLUMN_VALUE;

	Query query = session.createQuery(hql);
	query.setParameter(Project.COLUMN_PRIMARY_KEY, projectID);
	query.setParameter(Field.COLUMN_PRIMARY_KEY, fieldID);
	query.setParameter(Field.COLUMN_LABEL, label);
	query.setParameter(Field.COLUMN_VALUE, value);

	FieldAssignment fieldAssignment = (FieldAssignment) query
		.uniqueResult();
	return fieldAssignment;
    }

    @Override
    public void deleteAssignedField(long projectID, long fieldID, String label,
	    String value) {
	Session session = this.sessionFactory.getCurrentSession();
	SQLQuery query = session.createSQLQuery("DELETE FROM "
		+ FieldAssignment.TABLE_NAME + " WHERE "
		+ Project.COLUMN_PRIMARY_KEY + " =:"
		+ Project.COLUMN_PRIMARY_KEY + " AND "
		+ Field.COLUMN_PRIMARY_KEY + " =:" + Field.COLUMN_PRIMARY_KEY
		+ " AND  " + Field.COLUMN_LABEL + " =:" + Field.COLUMN_LABEL
		+ " AND " + Field.COLUMN_VALUE + " =:" + Field.COLUMN_VALUE);

	query.setParameter(Project.COLUMN_PRIMARY_KEY, projectID);
	query.setParameter(Field.COLUMN_PRIMARY_KEY, fieldID);
	query.setParameter(Field.COLUMN_LABEL, label);
	query.setParameter(Field.COLUMN_VALUE, value);

	query.executeUpdate();
    }

    @Override
    public void assignFieldToTask(TaskFieldAssignment taskField) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(taskField);
    }

    @Override
    public void unassignAllFieldsFromTask(long taskID) {
	Session session = this.sessionFactory.getCurrentSession();
	Query query = session.createQuery("DELETE FROM "
		+ TaskFieldAssignment.class.getName() + " WHERE "
		+ Task.COLUMN_PRIMARY_KEY + "=:" + Task.COLUMN_PRIMARY_KEY);
	query.setParameter(Task.COLUMN_PRIMARY_KEY, taskID);
	query.executeUpdate();
    }

    @Override
    public void unassignFieldFromTask(long fieldID, long taskID, String label,
	    String value) {
	// TODO Convert to Query, not SQLQuery.
	Session session = this.sessionFactory.getCurrentSession();
	SQLQuery query = session.createSQLQuery("DELETE FROM "
		+ TaskFieldAssignment.TABLE_NAME + " WHERE "
		+ Task.COLUMN_PRIMARY_KEY + " =:" + Task.COLUMN_PRIMARY_KEY
		+ " AND " + Field.COLUMN_PRIMARY_KEY + " =:"
		+ Field.COLUMN_PRIMARY_KEY + " AND  " + Field.COLUMN_LABEL
		+ " =:" + Field.COLUMN_LABEL + " AND " + Field.COLUMN_VALUE
		+ " =:" + Field.COLUMN_VALUE);

	query.setParameter(Task.COLUMN_PRIMARY_KEY, taskID);
	query.setParameter(Field.COLUMN_PRIMARY_KEY, fieldID);
	query.setParameter(Field.COLUMN_LABEL, label);
	query.setParameter(Field.COLUMN_VALUE, value);

	query.executeUpdate();
    }

    @Override
    public void unassignFieldFromStaff(long fieldID, long staffID, String label,
	    String value) {
	Session session = this.sessionFactory.getCurrentSession();

	String queryStr = "DELETE FROM " + StaffFieldAssignment.class.getName();
	queryStr += " WHERE ";
	queryStr += Field.COLUMN_PRIMARY_KEY + "=:" + Field.COLUMN_PRIMARY_KEY;
	queryStr += " AND ";
	queryStr += Staff.COLUMN_PRIMARY_KEY + "=:" + Staff.COLUMN_PRIMARY_KEY;
	queryStr += " AND ";
	queryStr += Field.COLUMN_LABEL + "=:" + Field.COLUMN_LABEL;
	queryStr += " AND ";
	queryStr += Field.COLUMN_VALUE + "=:" + Field.COLUMN_VALUE;

	Query query = session.createQuery(queryStr);
	query.setParameter(Field.COLUMN_PRIMARY_KEY, fieldID);
	query.setParameter(Staff.COLUMN_PRIMARY_KEY, staffID);
	query.setParameter(Field.COLUMN_LABEL, label);
	query.setParameter(Field.COLUMN_VALUE, value);
	query.executeUpdate();
    }

    @Override
    public void assignFieldToStaff(StaffFieldAssignment fieldAssignment) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(fieldAssignment);
    }

    @Override
    public void unassignAllFieldsFromStaff(long staffID) {
	Session session = this.sessionFactory.getCurrentSession();
	Query query = session.createQuery("DELETE FROM "
		+ StaffFieldAssignment.class.getName() + " WHERE "
		+ Staff.COLUMN_PRIMARY_KEY + "=:" + Staff.COLUMN_PRIMARY_KEY);
	query.setParameter(Staff.COLUMN_PRIMARY_KEY, staffID);
	query.executeUpdate();
    }

    @Override
    public void updateAssignedProjectField(FieldAssignment assignment) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(assignment.getAssignmentID());
    }

    @Override
    public void updateAssignedField(String table, String objectKeyCol,
	    long objectID, long fieldID, String oldLabel, String oldValue,
	    String label, String value) {
	Session session = this.sessionFactory.getCurrentSession();
	String sql = "UPDATE " + table;
	sql += " SET ";
	sql += Field.COLUMN_LABEL + " =:" + Field.COLUMN_LABEL + ",";
	sql += Field.COLUMN_VALUE + " =:" + Field.COLUMN_VALUE + " ";
	sql += "WHERE " + Field.COLUMN_LABEL + " =:" + "old"
		+ Field.COLUMN_LABEL + " ";
	sql += "AND " + Field.COLUMN_VALUE + " =:" + "old" + Field.COLUMN_VALUE
		+ " ";
	sql += "AND " + objectKeyCol + " =:" + objectKeyCol + " ";
	sql += "AND " + Field.COLUMN_PRIMARY_KEY + " =:"
		+ Field.COLUMN_PRIMARY_KEY + " ";

	SQLQuery query = session.createSQLQuery(sql);
	query.setParameter(Field.COLUMN_LABEL, label);
	query.setParameter(Field.COLUMN_VALUE, value);
	query.setParameter("old" + Field.COLUMN_LABEL, oldLabel);
	query.setParameter("old" + Field.COLUMN_VALUE, oldValue);
	query.setParameter(objectKeyCol, objectID);
	query.setParameter(Field.COLUMN_PRIMARY_KEY, fieldID);

	query.executeUpdate();
    }
}
