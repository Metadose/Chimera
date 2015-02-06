package com.cebedo.pmsys.project.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.cebedo.pmsys.project.model.Project;

public class ProjectDAOImpl implements ProjectDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Project p) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(p);
		tx.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public List<Project> list() {
		Session session = this.sessionFactory.openSession();
		List<Project> projectList = session.createQuery(
				"from " + Project.TABLE_NAME).list();
		session.close();
		return projectList;
	}

}