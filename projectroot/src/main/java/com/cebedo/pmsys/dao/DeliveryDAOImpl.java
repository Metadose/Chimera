package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Delivery;

@Repository
public class DeliveryDAOImpl implements DeliveryDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Delivery delivery) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(delivery);
    }

    @Override
    public Delivery getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Delivery delivery = (Delivery) this.daoHelper.criteriaGetObjByID(
		session, Delivery.class, Delivery.PROPERTY_ID, id)
		.uniqueResult();
	return delivery;
    }

    @Override
    public void update(Delivery delivery) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(delivery);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Delivery delivery = getByID(id);
	if (delivery != null) {
	    session.delete(delivery);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Delivery> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Delivery> list = session.createQuery(
		"FROM " + Delivery.class.getName()).list();
	return list;
    }

}
