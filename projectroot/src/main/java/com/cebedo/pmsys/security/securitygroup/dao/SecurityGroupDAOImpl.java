package com.cebedo.pmsys.security.securitygroup.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.common.QueryUtils;
import com.cebedo.pmsys.security.securitygroup.model.SecurityGroup;

@Repository
public class SecurityGroupDAOImpl implements SecurityGroupDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(SecurityGroupDAOImpl.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(SecurityGroup securityGroup) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(securityGroup);
		logger.info("[Create] SecurityGroup: " + securityGroup);
	}

	@Override
	public SecurityGroup getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		SecurityGroup securityGroup = (SecurityGroup) session.createQuery(
				"from " + SecurityGroup.class.getName() + " where "
						+ SecurityGroup.COLUMN_PRIMARY_KEY + "=" + id)
				.uniqueResult();
		logger.info("[Get by ID] SecurityGroup: " + securityGroup);
		return securityGroup;
	}

	@Override
	public void update(SecurityGroup securityGroup) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(securityGroup);
		logger.info("[Update] SecurityGroup:" + securityGroup);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		SecurityGroup securityGroup = (SecurityGroup) session.load(
				SecurityGroup.class, new Long(id));
		if (securityGroup != null) {
			session.delete(securityGroup);
		}
		logger.info("[Delete] SecurityGroup: " + securityGroup);
	}

	@SuppressWarnings("unchecked")
	public List<SecurityGroup> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<SecurityGroup> securityGroupList = QueryUtils
				.getSelectQueryFilterCompany(session,
						SecurityGroup.class.getName(), companyID).list();
		for (SecurityGroup securityGroup : securityGroupList) {
			logger.info("[List] SecurityGroup: " + securityGroup);
		}
		return securityGroupList;
	}

}
