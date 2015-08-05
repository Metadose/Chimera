package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;

@Repository
public class MilestoneDAOImpl implements MilestoneDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Milestone milestone) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(milestone);
    }

    @Override
    public Milestone getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Milestone milestone = (Milestone) this.daoHelper.criteriaGetObjByID(session, Milestone.class,
		Milestone.PROPERTY_ID, id).uniqueResult();
	return milestone;
    }

    @Override
    public void update(Milestone milestone) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(milestone);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Milestone milestone = getByID(id);
	if (milestone != null) {
	    session.delete(milestone);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Milestone> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Milestone> list = session.createQuery("FROM " + Milestone.class.getName()).list();
	return list;
    }

    @Override
    public void deleteAllByProject(long id) {
	// Construct hql.
	Session session = this.sessionFactory.getCurrentSession();
	String hql = "DELETE FROM " + Milestone.class.getName();
	hql += " WHERE ";
	hql += Project.COLUMN_PRIMARY_KEY + "=:" + Project.COLUMN_PRIMARY_KEY;

	// Create, set and update.
	Query query = session.createQuery(hql);
	query.setParameter(Project.COLUMN_PRIMARY_KEY, id);
	query.executeUpdate();
    }

}
