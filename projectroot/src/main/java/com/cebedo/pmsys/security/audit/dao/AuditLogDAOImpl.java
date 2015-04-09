package com.cebedo.pmsys.security.audit.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.security.audit.model.AuditLog;
import com.cebedo.pmsys.system.helper.DAOHelper;

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
		AuditLog auditLog = (AuditLog) this.daoHelper.criteriaGetObjByID(
				session, AuditLog.class, AuditLog.PROPERTY_ID, id)
				.uniqueResult();
		return auditLog;
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		AuditLog auditLog = (AuditLog) session.load(AuditLog.class,
				new Long(id));
		if (auditLog != null) {
			session.delete(auditLog);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AuditLog> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<AuditLog> auditLogList = this.daoHelper
				.getSelectQueryFilterCompany(session, AuditLog.class.getName(),
						companyID).list();
		return auditLogList;
	}

}
