package com.cebedo.pmsys.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;

@Repository
public class StaffDAOImpl implements StaffDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Staff staff) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(staff);
    }

    @Override
    public Staff getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Staff staff = (Staff) this.daoHelper
		.criteriaGetObjByID(session, Staff.class, Staff.PROPERTY_ID, id).uniqueResult();
	return staff;
    }

    @Override
    public Staff getWithAllCollectionsByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Staff staff = (Staff) session.load(Staff.class, new Long(id));

	Set<Task> taskList = staff.getTasks();
	for (Task task : taskList) {
	    Hibernate.initialize(task.getStaff());

	    Project proj = task.getProject();
	    Hibernate.initialize(proj);
	}
	return staff;
    }

    @Override
    public void update(Staff staff) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(staff);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Staff staff = (Staff) session.load(Staff.class, new Long(id));
	if (staff != null) {
	    session.delete(staff);
	}
    }

    @SuppressWarnings("unchecked")
    public List<Staff> list(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	String hql = "FROM " + Staff.class.getName();
	if (companyID != null) {
	    hql += " WHERE ";
	    hql += Company.COLUMN_PRIMARY_KEY + "=:" + Company.COLUMN_PRIMARY_KEY;
	}

	Query query = session.createQuery(hql);
	if (companyID != null) {
	    query.setParameter(Company.COLUMN_PRIMARY_KEY, companyID);
	}

	List<Staff> staffList = query.list();
	return staffList;
    }

    /**
     * Get the list of all staff members, filter by a specific company. If no
     * company is supplied, get all staff members.
     */
    @SuppressWarnings("unchecked")
    public List<Staff> listWithAllCollections(Long companyID) {
	// Setup the query.
	Session session = this.sessionFactory.getCurrentSession();
	String hql = "FROM " + Staff.class.getName();
	if (companyID != null) {
	    hql += " WHERE ";
	    hql += Company.COLUMN_PRIMARY_KEY + "=:" + Company.COLUMN_PRIMARY_KEY;
	}

	// Set params.
	Query query = session.createQuery(hql);
	if (companyID != null) {
	    query.setParameter(Company.COLUMN_PRIMARY_KEY, companyID);
	}

	List<Staff> staffList = query.list();
	for (Staff staff : staffList) {
	    Hibernate.initialize(staff.getTasks());
	}
	return staffList;
    }

    @Override
    public String getNameByID(long staffID) {
	Session session = this.sessionFactory.getCurrentSession();

	// Create a criteria for the staff.
	Criteria criteria = session.createCriteria(Staff.class)
		.add(Restrictions.eq(Staff.PROPERTY_ID, staffID));

	// Set all projections.
	ProjectionList projList = Projections.projectionList();
	projList.add(Projections.property(Staff.PROPERTY_PREFIX));
	projList.add(Projections.property(Staff.PROPERTY_FIRST_NAME));
	projList.add(Projections.property(Staff.PROPERTY_MIDDLE_NAME));
	projList.add(Projections.property(Staff.PROPERTY_LAST_NAME));
	projList.add(Projections.property(Staff.PROPERTY_SUFFIX));

	// Assign projection criteria.
	criteria.setProjection(Projections.distinct(projList));
	Object[] staffName = (Object[]) criteria.uniqueResult();
	String output = staffName[0] + " " + staffName[1] + " " + staffName[2] + " " + staffName[3] + " "
		+ staffName[4];
	return output;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Staff getStaffByName(Staff staff) {
	String queryStr = "FROM " + Staff.class.getName() + " ";
	queryStr += "WHERE ";
	queryStr += Staff.PROPERTY_FIRST_NAME + " =:" + Staff.PROPERTY_FIRST_NAME + " AND ";
	queryStr += Staff.PROPERTY_MIDDLE_NAME + " =:" + Staff.PROPERTY_MIDDLE_NAME + " AND ";
	queryStr += Staff.PROPERTY_LAST_NAME + " =:" + Staff.PROPERTY_LAST_NAME + " AND ";
	queryStr += Staff.PROPERTY_SUFFIX + " =:" + Staff.PROPERTY_SUFFIX;

	Session session = this.sessionFactory.getCurrentSession();
	Query query = session.createQuery(queryStr);
	query.setParameter(Staff.PROPERTY_FIRST_NAME, staff.getFirstName());
	query.setParameter(Staff.PROPERTY_MIDDLE_NAME, staff.getMiddleName());
	query.setParameter(Staff.PROPERTY_LAST_NAME, staff.getLastName());
	query.setParameter(Staff.PROPERTY_SUFFIX, staff.getSuffix());

	List<Staff> staffList = (List<Staff>) query.list();
	if (staffList != null && staffList.size() > 1) {

	    // Get the staff with the same company as me.
	    AuthHelper authHelper = new AuthHelper();
	    Company myCompany = authHelper.getAuth().getCompany();
	    for (Staff resultStaff : staffList) {
		if (myCompany.getId() == resultStaff.getCompany().getId()) {
		    return resultStaff;
		}
	    }
	    return null;
	}
	return (Staff) query.uniqueResult();
    }
}
