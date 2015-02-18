package com.cebedo.pmsys.staff.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;

@Repository
public class StaffDAOImpl implements StaffDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(StaffDAOImpl.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Staff staff) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(staff);
		logger.info("[Create] Staff: " + staff);
	}

	@Override
	public Staff getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Staff staff = (Staff) session.createQuery(
				"from " + Staff.CLASS_NAME + " where "
						+ Staff.COLUMN_PRIMARY_KEY + "=" + id).uniqueResult();
		logger.info("[Get by ID] Staff: " + staff);
		return staff;
	}

	@Override
	public void update(Staff staff) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(staff);
		logger.info("[Update] Staff:" + staff);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Staff staff = getByID(id);
		if (staff != null) {
			session.delete(staff);
		}
		logger.info("[Delete] Staff: " + staff);
	}

	@SuppressWarnings("unchecked")
	public List<Staff> list() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Staff> staffList = session.createQuery("from " + Staff.CLASS_NAME)
				.list();
		for (Staff staff : staffList) {
			logger.info("[List] Staff: " + staff);
		}
		return staffList;
	}

	@SuppressWarnings("unchecked")
	public List<Staff> listWithAllCollections() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Staff> staffList = session.createQuery("from " + Staff.CLASS_NAME)
				.list();
		for (Staff staff : staffList) {
			Hibernate.initialize(staff.getAssignedManagers());
			Hibernate.initialize(staff.getTasks());
			logger.info("[List] Staff: " + staff);
		}
		return staffList;
	}

	@Override
	public void assignProjectManager(ManagerAssignment assignment) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(assignment);
		logger.info("[Create] Manager: " + assignment);
	}

	@Override
	public void unassignProjectManager(long projectID, long staffID) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM "
				+ ManagerAssignment.TABLE_NAME + " WHERE "
				+ Project.COLUMN_PRIMARY_KEY + " = " + projectID + " AND "
				+ Staff.COLUMN_PRIMARY_KEY + " = " + staffID);
		query.executeUpdate();
	}

	@Override
	public void unassignAllProjectManagers(long projectID) {
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("DELETE FROM "
				+ ManagerAssignment.TABLE_NAME + " WHERE "
				+ Project.COLUMN_PRIMARY_KEY + " = " + projectID);
		query.executeUpdate();
	}

}
