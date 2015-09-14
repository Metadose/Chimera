package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.TaskStaffAssignment;

@Repository
public class TaskDAOImpl implements TaskDAO {

    private SessionFactory sessionFactory;
    private DAOHelper daoHelper = new DAOHelper();

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Task task) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(task);
    }

    @Override
    public Task getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Task task = (Task) session.load(Task.class, new Long(id));
	return task;
    }

    @Override
    public Task getByIDWithAllCollections(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Task task = (Task) session.load(Task.class, new Long(id));
	Hibernate.initialize(task.getStaff());

	Project proj = task.getProject();
	Hibernate.initialize(proj);
	for (Staff projStaff : proj.getAssignedStaff()) {
	    Hibernate.initialize(projStaff);
	}
	return task;
    }

    @Override
    public void update(Task task) {
	Session session = this.sessionFactory.getCurrentSession();
	try {
	    session.update(task);
	} catch (Exception e) {
	    session.merge(task);
	}
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Task task = getByID(id);
	if (task != null) {
	    session.delete(task);
	}
    }

    @SuppressWarnings("unchecked")
    public List<Task> list(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Task> taskList = this.daoHelper.getSelectQueryFilterCompany(session, Task.class.getName(),
		companyID).list();
	return taskList;
    }

    @SuppressWarnings("unchecked")
    public List<Task> listWithAllCollections(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Task> taskList = this.daoHelper.getSelectQueryFilterCompany(session, Task.class.getName(),
		companyID).list();
	for (Task task : taskList) {
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
    public void unassignStaffTask(long taskID, long staffID) {
	Session session = this.sessionFactory.getCurrentSession();
	String queryStr = "DELETE FROM " + TaskStaffAssignment.OBJECT_NAME + " WHERE "
		+ TaskStaffAssignment.PROPERTY_TASK_ID + "=:" + TaskStaffAssignment.PROPERTY_TASK_ID
		+ " AND " + TaskStaffAssignment.PROPERTY_STAFF_ID + "=:"
		+ TaskStaffAssignment.PROPERTY_STAFF_ID;
	Query query = session.createQuery(queryStr);
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
	Query query = session.createQuery("DELETE FROM " + TaskStaffAssignment.OBJECT_NAME + " WHERE "
		+ TaskStaffAssignment.PROPERTY_TASK_ID + "=:" + TaskStaffAssignment.PROPERTY_TASK_ID);
	query.setParameter(TaskStaffAssignment.PROPERTY_TASK_ID, id);
	query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Staff> getStaffByTaskID(long taskID) {
	Session session = this.sessionFactory.getCurrentSession();
	Query query = session.createQuery("FROM " + TaskStaffAssignment.class.getName() + " WHERE "
		+ Task.COLUMN_PRIMARY_KEY + " =:" + Task.COLUMN_PRIMARY_KEY);
	query.setParameter(Task.COLUMN_PRIMARY_KEY, taskID);
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
	String result = this.daoHelper.getProjectionByID(session, Task.class, Task.PROPERTY_ID, taskID,
		Task.PROPERTY_TITLE);
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

    @Override
    public void unassignAllTasksByStaff(long staffID) {
	Session session = this.sessionFactory.getCurrentSession();
	String queryStr = "DELETE FROM " + TaskStaffAssignment.OBJECT_NAME + " WHERE "
		+ TaskStaffAssignment.PROPERTY_STAFF_ID + "=:" + TaskStaffAssignment.PROPERTY_STAFF_ID;
	Query query = session.createQuery(queryStr);
	query.setParameter(TaskStaffAssignment.PROPERTY_STAFF_ID, staffID);
	query.executeUpdate();
    }

}
