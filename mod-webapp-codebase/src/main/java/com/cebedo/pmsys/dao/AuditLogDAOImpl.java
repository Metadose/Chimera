package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.SystemUser;

@Repository
public class AuditLogDAOImpl implements AuditLogDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(AuditLog auditLog) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(auditLog);
    }

    @Override
    public AuditLog getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	AuditLog auditLog = (AuditLog) this.daoHelper
		.criteriaGetObjByID(session, AuditLog.class, AuditLog.PROPERTY_ID, id).uniqueResult();
	return auditLog;
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	AuditLog auditLog = (AuditLog) session.load(AuditLog.class, new Long(id));
	if (auditLog != null) {
	    session.delete(auditLog);
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AuditLog> list(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	List<AuditLog> auditLogList = this.daoHelper
		.getSelectQueryFilterCompany(session, AuditLog.class.getName(), companyID).list();
	return auditLogList;
    }

    @Override
    public void deleteAll(long userID) {
	// TODO Use Hibernate, rather than SQLQuery.
	Session session = this.sessionFactory.getCurrentSession();
	String queryStr = String.format("DELETE FROM %s WHERE %s=:%s", AuditLog.TABLE_NAME,
		SystemUser.COLUMN_PRIMARY_KEY, SystemUser.COLUMN_PRIMARY_KEY);
	SQLQuery query = session.createSQLQuery(queryStr);
	query.setParameter(SystemUser.COLUMN_PRIMARY_KEY, userID);
	query.executeUpdate();
    }

}
