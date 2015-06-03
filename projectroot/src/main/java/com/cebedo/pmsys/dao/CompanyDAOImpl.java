package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Company;

@Repository
public class CompanyDAOImpl implements CompanyDAO {

    private static final Logger logger = LoggerFactory
	    .getLogger(CompanyDAOImpl.class);
    private DAOHelper daoHelper = new DAOHelper();
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
	Criteria criteria = this.daoHelper.criteriaGetObjByID(session,
		Company.class, Company.PROPERTY_ID, id);
	Company company = (Company) criteria.uniqueResult();
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
	List<Company> companyList = this.daoHelper.getSelectQueryFilterCompany(
		session, Company.class.getName(), companyID).list();
	return companyList;
    }

    @Override
    public long getCompanyIDByObjID(String objTable, String objKeyCol,
	    long objID) {
	Session session = this.sessionFactory.getCurrentSession();
	String qStr = "SELECT " + Company.COLUMN_PRIMARY_KEY + " FROM "
		+ objTable + " WHERE " + objKeyCol + " =:" + objKeyCol
		+ " LIMIT 1";
	SQLQuery query = session.createSQLQuery(qStr);
	query.setParameter(objKeyCol, objID);
	String resultStr = query.uniqueResult().toString();
	return Long.parseLong(resultStr);
    }

    @Override
    public Company getCompanyByObjID(String objTable, String objKeyCol,
	    long objID) {
	long companyID = getCompanyIDByObjID(objTable, objKeyCol, objID);
	return getByID(companyID);
    }
}
