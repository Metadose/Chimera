package com.cebedo.pmsys.security.securityrole.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.system.helper.DAOHelper;

@Repository
public class SecurityRoleDAOImpl implements SecurityRoleDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(SecurityRoleDAOImpl.class);
	private DAOHelper daoHelper = new DAOHelper();
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(SecurityRole securityRole) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(securityRole);
		logger.info("[Create] SecurityRole: " + securityRole);
	}

	@Override
	public SecurityRole getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		SecurityRole securityRole = (SecurityRole) this.daoHelper
				.criteriaGetObjByID(session, SecurityRole.class,
						SecurityRole.PROPERTY_ID, id).uniqueResult();
		return securityRole;
	}

	@Override
	public void update(SecurityRole securityRole) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(securityRole);
		logger.info("[Update] SecurityRole:" + securityRole);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		SecurityRole securityRole = (SecurityRole) session.load(
				SecurityRole.class, new Long(id));
		if (securityRole != null) {
			session.delete(securityRole);
		}
		logger.info("[Delete] SecurityRole: " + securityRole);
	}

	@SuppressWarnings("unchecked")
	public List<SecurityRole> list() {
		Session session = this.sessionFactory.getCurrentSession();
		List<SecurityRole> securityRoleList = this.daoHelper
				.getSelectQueryFilterCompany(session,
						SecurityRole.class.getName(), null).list();
		return securityRoleList;
	}

}
