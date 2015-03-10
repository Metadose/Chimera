package com.cebedo.pmsys.company.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.common.QueryUtils;
import com.cebedo.pmsys.company.model.Company;

@Repository
public class CompanyDAOImpl implements CompanyDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(CompanyDAOImpl.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Company company) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(company);
		logger.info("[Create] Company: " + company);
	}

	@Override
	public Company getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Company company = (Company) session.createQuery(
				"from " + Company.class.getName() + " where "
						+ Company.COLUMN_PRIMARY_KEY + "=" + id).uniqueResult();
		logger.info("[Get by ID] Company: " + company);
		return company;
	}

	@Override
	public void update(Company company) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(company);
		logger.info("[Update] Company:" + company);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Company company = getByID(id);
		if (company != null) {
			session.delete(company);
		}
		logger.info("[Delete] Company: " + company);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Company> companyList = QueryUtils.getSelectQueryFilterCompany(
				session, Company.class.getName(), companyID).list();
		return companyList;
	}

}
