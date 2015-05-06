package com.cebedo.pmsys.system.helper;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.cebedo.pmsys.company.model.Company;

public class DAOHelper {

	/**
	 * FIXME Almost all classes don't have the property:
	 * Company.COLUMN_PRIMARY_KEY<br>
	 * Get a select all query, filtered by company id.
	 * 
	 * @param session
	 * @param className
	 * @param companyID
	 * @return
	 */
	public Query getSelectQueryFilterCompany(Session session, String className,
			Long companyID) {
		String hql = "FROM " + className;
		if (companyID != null) {
			hql += " WHERE ";
			hql += Company.COLUMN_PRIMARY_KEY + "=:"
					+ Company.COLUMN_PRIMARY_KEY;
		}
		Query query = session.createQuery(hql);
		if (companyID != null) {
			query.setParameter(Company.COLUMN_PRIMARY_KEY, companyID);
		}
		return query;
	}

	public SQLQuery getSelectSQLFilterCompany(Session session,
			String tableName, Long companyID) {
		String sql = "SELECT * FROM " + tableName;
		if (companyID != null) {
			sql += " WHERE ";
			sql += Company.COLUMN_PRIMARY_KEY + "=:"
					+ Company.COLUMN_PRIMARY_KEY;
		}
		SQLQuery query = session.createSQLQuery(sql);
		if (companyID != null) {
			query.setParameter(Company.COLUMN_PRIMARY_KEY, companyID);
		}
		return query;
	}

	@SuppressWarnings("rawtypes")
	public Criteria criteriaGetObjByID(Session session, Class clazz,
			String propertyID, String objID) {
		return session.createCriteria(clazz).add(
				Restrictions.eq(propertyID, objID));
	}

	@SuppressWarnings("rawtypes")
	public Criteria criteriaGetObjByID(Session session, Class clazz,
			String propertyID, long objID) {
		return session.createCriteria(clazz).add(
				Restrictions.eq(propertyID, objID));
	}

	@SuppressWarnings("rawtypes")
	public String getProjectionByID(Session session, Class clazz,
			String restrictionID, long objectID, String projectionName) {
		String result = (String) session.createCriteria(clazz)
				.add(Restrictions.eq(restrictionID, objectID))
				.setProjection(Property.forName(projectionName)).uniqueResult();
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> getProjectionList(Session session, Class clazz,
			String projectionName) {
		return (List<String>) session.createCriteria(clazz)
				.setProjection(Property.forName(projectionName)).list();
	}

	@SuppressWarnings("rawtypes")
	public Criteria getCriteriaWithProjectionList(Session session, Class clazz,
			ProjectionList projList) {
		return session.createCriteria(clazz).setProjection(
				Projections.distinct(projList));
	}

}
