package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Milestone;

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
	Milestone milestone = (Milestone) this.daoHelper.criteriaGetObjByID(
		session, Milestone.class, Milestone.PROPERTY_ID, id)
		.uniqueResult();
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
	List<Milestone> list = session.createQuery(
		"FROM " + Milestone.class.getName()).list();
	return list;
    }

}
