package com.cebedo.pmsys.subcontractor.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cebedo.pmsys.common.DAOHelper;
import com.cebedo.pmsys.subcontractor.model.Subcontractor;

//@Repository
public class SubcontractorDAOImpl implements SubcontractorDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(SubcontractorDAOImpl.class);
	private DAOHelper daoHelper = new DAOHelper();
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Subcontractor subcontractor) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(subcontractor);
		logger.info("[Create] Subcontractor: " + subcontractor);
	}

	@Override
	public Subcontractor getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Subcontractor subcontractor = (Subcontractor) session.createQuery(
				"from " + Subcontractor.class.getName() + " where "
						+ Subcontractor.COLUMN_PRIMARY_KEY + "=" + id)
				.uniqueResult();
		logger.info("[Get by ID] Subcontractor: " + subcontractor);
		return subcontractor;
	}

	@Override
	public void update(Subcontractor subcontractor) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(subcontractor);
		logger.info("[Update] Subcontractor:" + subcontractor);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Subcontractor subcontractor = (Subcontractor) session.load(
				Subcontractor.class, new Long(id));
		if (subcontractor != null) {
			session.delete(subcontractor);
		}
		logger.info("[Delete] Subcontractor: " + subcontractor);
	}

	@SuppressWarnings("unchecked")
	public List<Subcontractor> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Subcontractor> subcontractorList = this.daoHelper
				.getSelectQueryFilterCompany(session,
						Subcontractor.class.getName(), companyID).list();
		return subcontractorList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Subcontractor> listWithAllCollections(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Subcontractor> subcontractorList = this.daoHelper
				.getSelectQueryFilterCompany(session,
						Subcontractor.class.getName(), companyID).list();
		for (Subcontractor subcontractor : subcontractorList) {
			Hibernate.initialize(subcontractor.getTasks());
			Hibernate.initialize(subcontractor.getProjects());
			Hibernate.initialize(subcontractor.getExpenses());
		}
		return subcontractorList;
	}
}
