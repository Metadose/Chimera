package com.cebedo.pmsys.staff.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;
import com.cebedo.pmsys.system.helper.DAOHelper;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.team.model.Team;

@Repository
public class StaffDAOImpl implements StaffDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(StaffDAOImpl.class);
	private DAOHelper daoHelper = new DAOHelper();
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
		Staff staff = (Staff) this.daoHelper.criteriaGetObjByID(session,
				Staff.class, Staff.PROPERTY_ID, id).uniqueResult();
		return staff;
	}

	@Override
	public Staff getWithAllCollectionsByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Staff staff = (Staff) session.load(Staff.class, new Long(id));
		Hibernate.initialize(staff.getTeams());
		Hibernate.initialize(staff.getFieldAssignments());

		Set<ManagerAssignment> managerAssignment = staff.getAssignedManagers();
		for (ManagerAssignment assignment : managerAssignment) {
			Hibernate.initialize(assignment.getProject());
			Hibernate.initialize(assignment.getManager());
		}

		Set<Task> taskList = staff.getTasks();
		for (Task task : taskList) {
			Hibernate.initialize(task.getTeams());
			Hibernate.initialize(task.getProject());
			Hibernate.initialize(task.getStaff());
			logger.info("[List] Task: " + task);
		}
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
		Staff staff = (Staff) session.load(Staff.class, new Long(id));
		if (staff != null) {
			session.delete(staff);
		}
		logger.info("[Delete] Staff: " + staff);
	}

	@SuppressWarnings("unchecked")
	public List<Staff> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM " + Staff.class.getName();
		if (companyID != null) {
			hql += " WHERE ";
			hql += Company.COLUMN_PRIMARY_KEY + "=:"
					+ Company.COLUMN_PRIMARY_KEY;
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
			hql += Company.COLUMN_PRIMARY_KEY + "=:"
					+ Company.COLUMN_PRIMARY_KEY;
		}

		// Set params.
		Query query = session.createQuery(hql);
		if (companyID != null) {
			query.setParameter(Company.COLUMN_PRIMARY_KEY, companyID);
		}

		List<Staff> staffList = query.list();
		for (Staff staff : staffList) {
			Hibernate.initialize(staff.getAssignedManagers());
			Hibernate.initialize(staff.getTasks());
			Hibernate.initialize(staff.getFieldAssignments());
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

	@Override
	public void unassignTeam(long teamID, long staffID) {
		Session session = this.sessionFactory.getCurrentSession();
		// TODO Make the others reference Object.COLUMN_PRIMARY_KEY
		// Rather than ObjectAssignment.COLUMN_NAME.
		Query query = session.createQuery("DELETE FROM "
				+ StaffTeamAssignment.class.getName() + " WHERE "
				+ Team.COLUMN_PRIMARY_KEY + "=:" + Team.COLUMN_PRIMARY_KEY
				+ " AND " + Staff.COLUMN_PRIMARY_KEY + "=:"
				+ Staff.COLUMN_PRIMARY_KEY);
		query.setParameter(Team.COLUMN_PRIMARY_KEY, teamID);
		query.setParameter(Staff.COLUMN_PRIMARY_KEY, staffID);
		query.executeUpdate();
	}

	@Override
	public void unassignAllTeams(long staffID) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM "
				+ StaffTeamAssignment.class.getName() + " WHERE "
				+ Staff.COLUMN_PRIMARY_KEY + "=:" + Staff.COLUMN_PRIMARY_KEY);
		query.setParameter(Staff.COLUMN_PRIMARY_KEY, staffID);
		query.executeUpdate();
	}

	@Override
	public void assignTeam(StaffTeamAssignment stAssign) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(stAssign);
	}

	@Override
	public String getNameByID(long staffID) {
		Session session = this.sessionFactory.getCurrentSession();

		// Create a criteria for the staff.
		Criteria criteria = session.createCriteria(Staff.class).add(
				Restrictions.eq(Staff.PROPERTY_ID, staffID));

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
		String output = staffName[0] + " " + staffName[1] + " " + staffName[2]
				+ " " + staffName[3] + " " + staffName[4];
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Cacheable(value = "searchStaffCache", key = "#root.methodName")
	public List<Staff> listStaffFromCache(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Staff> list = this.daoHelper.getSelectQueryFilterCompany(session,
				Staff.class.getName(), companyID).list();
		return list;
	}
}
