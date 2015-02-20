package com.cebedo.pmsys.project.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.task.model.Task;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(ProjectDAOImpl.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void create(Project project) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(project);
		logger.info("[Create] Project: " + project);
	}

	@SuppressWarnings("unchecked")
	public List<Project> list() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Project> projectList = session.createQuery(
				"from " + Project.CLASS_NAME).list();
		for (Project project : projectList) {
			logger.info("[List] Project: " + project);
		}
		return projectList;
	}

	@Override
	public Project getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Project project = (Project) session.createQuery(
				"from " + Project.CLASS_NAME + " where "
						+ Project.COLUMN_PRIMARY_KEY + "=" + id).uniqueResult();
		logger.info("[Get by ID] Project: " + project);
		return project;
	}

	@Override
	public void update(Project project) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(project);
		logger.info("[Update] Project:" + project);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Project project = getByID(id);
		if (project != null) {
			session.delete(project);
		}
		logger.info("[Delete] Project: " + project);
	}

	@SuppressWarnings("unchecked")
	public List<Project> listWithAllCollections() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Project> projectList = session.createQuery(
				"from " + Project.CLASS_NAME).list();
		for (Project project : projectList) {
			Hibernate.initialize(project.getManagerAssignments());
			Hibernate.initialize(project.getAssignedTeams());
			Hibernate.initialize(project.getAssignedFields());
			Hibernate.initialize(project.getAssignedTasks());
			logger.info("[List] Project: " + project);
		}
		return projectList;
	}

	@Override
	public Project getByIDWithAllCollections(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Project project = (Project) session.createQuery(
				"from " + Project.CLASS_NAME + " where "
						+ Project.COLUMN_PRIMARY_KEY + "=" + id).uniqueResult();
		Hibernate.initialize(project.getManagerAssignments());
		Hibernate.initialize(project.getAssignedTeams());
		Hibernate.initialize(project.getAssignedFields());
		Hibernate.initialize(project.getFiles());
		Hibernate.initialize(project.getPhotos());

		// Initialize all tasks.
		// And all teams and staff of each task.
		Set<Task> assignedTasks = project.getAssignedTasks();
		Hibernate.initialize(assignedTasks);
		for (Task task : assignedTasks) {
			Hibernate.initialize(task.getTeam());
			Hibernate.initialize(task.getStaff());
		}
		logger.info("[Get by ID] Project: " + project);
		return project;
	}
}