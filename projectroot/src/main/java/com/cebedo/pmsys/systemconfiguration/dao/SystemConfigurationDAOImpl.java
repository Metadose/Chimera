package com.cebedo.pmsys.systemconfiguration.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.common.DAOHelper;
import com.cebedo.pmsys.systemconfiguration.model.SystemConfiguration;

@Repository
public class SystemConfigurationDAOImpl implements SystemConfigurationDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(SystemConfigurationDAOImpl.class);
	private DAOHelper daoHelper = new DAOHelper();
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(SystemConfiguration systemConfiguration) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(systemConfiguration);
		logger.info("[Create] SystemConfiguration: " + systemConfiguration);
	}

	@Override
	public SystemConfiguration getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		SystemConfiguration systemConfiguration = (SystemConfiguration) session
				.createQuery(
						"from " + SystemConfiguration.CLASS_NAME + " where "
								+ SystemConfiguration.COLUMN_PRIMARY_KEY + "="
								+ id).uniqueResult();
		logger.info("[Get by ID] SystemConfiguration: " + systemConfiguration);
		return systemConfiguration;
	}

	@Override
	public void update(SystemConfiguration systemConfiguration) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(systemConfiguration);
		logger.info("[Update] SystemConfiguration:" + systemConfiguration);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		SystemConfiguration systemConfiguration = (SystemConfiguration) session
				.load(SystemConfiguration.class, new Long(id));
		if (systemConfiguration != null) {
			session.delete(systemConfiguration);
		}
		logger.info("[Delete] SystemConfiguration: " + systemConfiguration);
	}

	@SuppressWarnings("unchecked")
	public List<SystemConfiguration> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<SystemConfiguration> systemConfigurationList = this.daoHelper
				.getSelectQueryFilterCompany(session,
						SystemConfiguration.class.getName(), companyID).list();
		for (SystemConfiguration systemConfiguration : systemConfigurationList) {
			logger.info("[List] SystemConfiguration: " + systemConfiguration);
		}
		return systemConfigurationList;
	}

	@Override
	public String getValueByName(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		SystemConfiguration systemConfiguration = (SystemConfiguration) session
				.createQuery(
						"from " + SystemConfiguration.CLASS_NAME + " where "
								+ SystemConfiguration.COLUMN_NAME + "='" + name
								+ "'").uniqueResult();
		return systemConfiguration.getValue();
	}

	@Override
	public SystemConfiguration getByName(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		SystemConfiguration systemConfiguration = (SystemConfiguration) session
				.createQuery(
						"from " + SystemConfiguration.CLASS_NAME + " where "
								+ SystemConfiguration.COLUMN_NAME + "='" + name
								+ "'").uniqueResult();
		return systemConfiguration;
	}

}
