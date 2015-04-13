package com.cebedo.pmsys.project.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.system.helper.DAOHelper;
import com.cebedo.pmsys.task.model.Task;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(ProjectDAOImpl.class);
	private SessionFactory sessionFactory;
	private DAOHelper daoHelper = new DAOHelper();

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void create(Project project) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(project);
		logger.info("[Create] Project: " + project);
	}

	@SuppressWarnings("unchecked")
	public List<Project> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Project> projectList = this.daoHelper.getSelectQueryFilterCompany(
				session, Project.class.getName(), companyID).list();
		return projectList;
	}

	@Override
	public Project getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Project project = (Project) session.load(Project.class.getName(),
				new Long(id));
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
	public List<Project> listWithAllCollections(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Project> projectList = this.daoHelper.getSelectQueryFilterCompany(
				session, Project.class.getName(), companyID).list();
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
	public Project getByIDWithAllCollections(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Project project = (Project) session.load(Project.class, new Long(id));
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
			Hibernate.initialize(task.getTeams());
			Hibernate.initialize(task.getStaff());
		}
		logger.info("[Get by ID] Project: " + project);
		return project;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> listWithTasks(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Project> projectList = this.daoHelper.getSelectQueryFilterCompany(
				session, Project.class.getName(), id).list();
		for (Project project : projectList) {
			Hibernate.initialize(project.getAssignedTasks());
		}
		return projectList;
	}

	@Override
	public String getNameByID(long projectID) {
		Session session = this.sessionFactory.getCurrentSession();
		String result = this.daoHelper.getProjectionByID(session,
				Project.class, Project.PROPERTY_ID, projectID,
				Project.PROPERTY_NAME);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Cacheable(value = Project.OBJECT_NAME + ".search", key = "#companyID != null ? #companyID : 0", unless = "#result.isEmpty()")
	public List<Project> listProjectFromCache(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Project> list = this.daoHelper.getSelectQueryFilterCompany(
				session, Project.class.getName(), companyID).list();
		return list;
	}
}