package com.cebedo.pmsys.dao.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.LazyCollection;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.ProjectStaffAssignment;
import com.cebedo.pmsys.model.assignment.TaskStaffAssignment;

@Repository
public class CompanyDAOImpl implements CompanyDAO {

    private AuthHelper authHelper = new AuthHelper();
    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Company company) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(company);
    }

    /**
     * Load all lazy collections.<br>
     * Reference:
     * http://stackoverflow.com/questions/24327353/initialize-all-lazy-loaded-
     * collections-in-hibernate
     * 
     * @param tClass
     * @param entity
     */
    private <T> void forceLoadLazyCollections(Class<T> tClass, T entity) {
	if (entity == null) {
	    return;
	}
	for (Field field : tClass.getDeclaredFields()) {
	    LazyCollection annotation = field.getAnnotation(LazyCollection.class);
	    if (annotation != null) {
		try {
		    field.setAccessible(true);
		    Hibernate.initialize(field.get(entity));
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    @Override
    public Company getByIDWithLazyCollections(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Criteria criteria = this.daoHelper.criteriaGetObjByID(session, Company.class,
		Company.PROPERTY_ID, id);
	Company company = (Company) criteria.uniqueResult();
	forceLoadLazyCollections(Company.class, company);
	return company;
    }

    @Override
    public Company getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Criteria criteria = this.daoHelper.criteriaGetObjByID(session, Company.class,
		Company.PROPERTY_ID, id);
	Company company = (Company) criteria.uniqueResult();
	Hibernate.initialize(company.getProjects());
	return company;
    }

    @Override
    public void update(Company company) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(company);
    }

    @Override
    public void delete(Company company) {
	Session session = this.sessionFactory.getCurrentSession();
	if (company != null) {

	    // Delete all staff assignments.
	    for (Project proj : company.getProjects()) {

		// Delete project assignments.
		deleteProjectAssignments(session, proj, ProjectStaffAssignment.TABLE_NAME);
		deleteProjectAssignments(session, proj, FieldAssignment.TABLE_NAME);

		// Delete tasks.
		for (Task task : proj.getAssignedTasks()) {
		    deleteTaskAssignments(session, task);
		}
	    }

	    // Delete all staff.
	    for (Staff staff : company.getStaff()) {
		deleteStaffAssignments(session, staff, ProjectStaffAssignment.TABLE_NAME);
		deleteStaffAssignments(session, staff, TaskStaffAssignment.TABLE_NAME);
	    }

	    // Unreferenced objects.
	    executeDelete(session, AuditLog.TABLE_NAME, company.getId());
	    executeDelete(session, SystemConfiguration.TABLE_NAME, company.getId());

	    // Referenced objects.
	    executeDelete(session, Task.TABLE_NAME, company.getId());
	    executeDelete(session, SystemUser.TABLE_NAME, company.getId());
	    executeDelete(session, Project.TABLE_NAME, company.getId());
	    executeDelete(session, Staff.TABLE_NAME, company.getId());
	    executeDelete(session, Company.TABLE_NAME, company.getId());
	}
    }

    /**
     * Delete staff assignments.
     * 
     * @param session
     * @param staff
     * @param tableName
     */
    private void deleteStaffAssignments(Session session, Staff staff, String tableName) {
	String queryStr = String.format("DELETE FROM %s WHERE %s=:%s", tableName,
		Staff.COLUMN_PRIMARY_KEY, Staff.COLUMN_PRIMARY_KEY);
	SQLQuery query = session.createSQLQuery(queryStr);
	query.setParameter(Staff.COLUMN_PRIMARY_KEY, staff.getId());
	query.executeUpdate();
    }

    /**
     * Delete assignment of tasks.
     * 
     * @param session
     * @param task
     */
    private void deleteTaskAssignments(Session session, Task task) {
	String queryStr = String.format("DELETE FROM %s WHERE %s=:%s", TaskStaffAssignment.TABLE_NAME,
		Task.COLUMN_PRIMARY_KEY, Task.COLUMN_PRIMARY_KEY);
	SQLQuery query = session.createSQLQuery(queryStr);
	query.setParameter(Task.COLUMN_PRIMARY_KEY, task.getId());
	query.executeUpdate();
    }

    /**
     * Execute a delete query.
     * 
     * @param session
     * @param tableName
     * @param primaryKey
     * @param companyId
     */
    private void executeDelete(Session session, String tableName, long companyId) {
	String queryStr = String.format("DELETE FROM %s WHERE %s=:%s", tableName,
		Company.COLUMN_PRIMARY_KEY, Company.COLUMN_PRIMARY_KEY);
	SQLQuery query = session.createSQLQuery(queryStr);
	query.setParameter(Company.COLUMN_PRIMARY_KEY, companyId);
	query.executeUpdate();
    }

    /**
     * Delete project assignments.
     * 
     * @param session
     * @param proj
     * @param tableName
     */
    private void deleteProjectAssignments(Session session, Project proj, String tableName) {
	String queryStr = String.format("DELETE FROM %s WHERE %s=:%s", tableName,
		Project.COLUMN_PRIMARY_KEY, Project.COLUMN_PRIMARY_KEY);
	SQLQuery query = session.createSQLQuery(queryStr);
	query.setParameter(Project.COLUMN_PRIMARY_KEY, proj.getId());
	query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Company> list(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Company> companyList = this.daoHelper
		.getSelectQueryFilterCompany(session, Company.class.getName(), companyID).list();
	return companyList;
    }

    @Override
    public long getCompanyIDByObjID(String objTable, String objKeyCol, long objID) {
	Session session = this.sessionFactory.getCurrentSession();
	String qStr = "SELECT " + Company.COLUMN_PRIMARY_KEY + " FROM " + objTable + " WHERE "
		+ objKeyCol + " =:" + objKeyCol + " LIMIT 1";
	SQLQuery query = session.createSQLQuery(qStr);
	query.setParameter(objKeyCol, objID);
	String resultStr = query.uniqueResult().toString();
	return Long.parseLong(resultStr);
    }

    @Override
    public Company getCompanyByObjID(String objTable, String objKeyCol, long objID) {
	long companyID = getCompanyIDByObjID(objTable, objKeyCol, objID);
	return getByID(companyID);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AuditLog> logs(Long companyID) {
	if (!this.authHelper.isCompanyAdmin() && !this.authHelper.isSuperAdmin()) {
	    return new ArrayList<AuditLog>();
	}
	Session session = this.sessionFactory.getCurrentSession();
	List<AuditLog> logs = this.daoHelper
		.getSelectQueryFilterCompany(session, AuditLog.class.getName(), companyID).list();
	return (List<AuditLog>) logs;
    }

}
