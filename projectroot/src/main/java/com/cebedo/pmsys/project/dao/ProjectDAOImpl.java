package com.cebedo.pmsys.project.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.project.model.Project;

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
	public Project getByID(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Project project = (Project) session
				.load(Project.class, new Integer(id));
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
	public void delete(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Project project = (Project) session
				.load(Project.class, new Integer(id));
		if (project != null) {
			session.delete(project);
		}
		logger.info("[Delete] Project: " + project);
	}

}