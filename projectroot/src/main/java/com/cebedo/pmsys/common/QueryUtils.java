package com.cebedo.pmsys.common;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.cebedo.pmsys.company.model.Company;

public abstract class QueryUtils {

	/**
	 * Get a select all query, filtered by company id.
	 * 
	 * @param session
	 * @param className
	 * @param companyID
	 * @return
	 */
	public static Query getSelectQueryFilterCompany(Session session,
			String className, Long companyID) {
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

	@SuppressWarnings("rawtypes")
	public static String getProjectionByID(Session session, Class clazz,
			String restrictionID, long objectID, String projectionName) {
		String result = (String) session.createCriteria(clazz)
				.add(Restrictions.eq(restrictionID, objectID))
				.setProjection(Property.forName(projectionName)).uniqueResult();
		return result;
	}

}
