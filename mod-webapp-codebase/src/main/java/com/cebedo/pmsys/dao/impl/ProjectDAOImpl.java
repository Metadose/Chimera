package com.cebedo.pmsys.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Task;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

    private SessionFactory sessionFactory;
    private DAOHelper daoHelper = new DAOHelper();

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    public void create(Project project) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(project);
    }

    @SuppressWarnings("unchecked")
    public List<Project> list(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Project> projectList = this.daoHelper
		.getSelectQueryFilterCompany(session, Project.class.getName(), companyID).list();
	return projectList;
    }

    @Override
    public Project getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Project project = (Project) session.load(Project.class.getName(), new Long(id));
	return project;
    }

    @Override
    public void update(Project project) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(project);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Project project = getByID(id);
	if (project != null) {
	    session.delete(project);
	}
    }

    @SuppressWarnings("unchecked")
    public List<Project> listWithAllCollections(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Project> projectList = this.daoHelper
		.getSelectQueryFilterCompany(session, Project.class.getName(), companyID).list();
	for (Project project : projectList) {
	    Hibernate.initialize(project.getAssignedTasks());
	}
	return projectList;
    }

    private class HibernateInitializer extends Thread {

	private Set<Task> assignedTasks;

	public HibernateInitializer(Set<Task> aT) {
	    this.assignedTasks = aT;
	}

	@Override
	public void run() {
	    Hibernate.initialize(assignedTasks);
	    for (Task task : assignedTasks) {
		Hibernate.initialize(task.getStaff());
	    }
	}
    }

    @Override
    public Project getByIDWithAllCollections(long id) {

	Session session = this.sessionFactory.getCurrentSession();
	Project project = (Project) session.load(Project.class, new Long(id));

	// Initialize all tasks.
	// And all teams and staff of each task.
	HibernateInitializer initer = new HibernateInitializer(project.getAssignedTasks());
	initer.start();

	// Init company.
	// and company admins.
	Hibernate.initialize(project.getCompany());
	Hibernate.initialize(project.getAssignedFields());
	Hibernate.initialize(project.getAssignedStaff());

	// Wait for the initializer to finish.
	while (initer.isAlive()) {
	    ;
	}
	return project;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Project> listWithTasks(Long id) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Project> projectList = this.daoHelper
		.getSelectQueryFilterCompany(session, Project.class.getName(), id).list();
	for (Project project : projectList) {
	    Hibernate.initialize(project.getAssignedTasks());
	}
	return projectList;
    }

    @Override
    public String getNameByID(long projectID) {
	Session session = this.sessionFactory.getCurrentSession();
	String result = this.daoHelper.getProjectionByID(session, Project.class, Project.PROPERTY_ID,
		projectID, Project.PROPERTY_NAME);
	return result;
    }

    @Override
    public void merge(Project project) {
	Session session = this.sessionFactory.getCurrentSession();
	session.merge(project);
    }

    @Override
    public Set<AuditLog> logs(long projID) {
	Session session = this.sessionFactory.getCurrentSession();
	Project project = (Project) session.load(Project.class, new Long(projID));
	Hibernate.initialize(project.getAuditLogs());
	return project.getAuditLogs();
    }
}