package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Reminder;

@Repository
public class ReminderDAOImpl implements ReminderDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Reminder reminder) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(reminder);
    }

    @Override
    public Reminder getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Reminder reminder = (Reminder) this.daoHelper.criteriaGetObjByID(
		session, Reminder.class, Reminder.PROPERTY_ID, id)
		.uniqueResult();
	return reminder;
    }

    @Override
    public void update(Reminder reminder) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(reminder);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Reminder reminder = getByID(id);
	if (reminder != null) {
	    session.delete(reminder);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Reminder> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Reminder> list = session.createQuery(
		"FROM " + Reminder.class.getName()).list();
	return list;
    }

}
