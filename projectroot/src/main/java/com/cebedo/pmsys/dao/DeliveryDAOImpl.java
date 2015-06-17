package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.DeliveryToDelete;

@Repository
public class DeliveryDAOImpl implements DeliveryDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(DeliveryToDelete delivery) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(delivery);
    }

    @Override
    public DeliveryToDelete getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	DeliveryToDelete delivery = (DeliveryToDelete) this.daoHelper.criteriaGetObjByID(
		session, DeliveryToDelete.class, DeliveryToDelete.PROPERTY_ID, id)
		.uniqueResult();
	return delivery;
    }

    @Override
    public void update(DeliveryToDelete delivery) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(delivery);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	DeliveryToDelete delivery = getByID(id);
	if (delivery != null) {
	    session.delete(delivery);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DeliveryToDelete> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<DeliveryToDelete> list = session.createQuery(
		"FROM " + DeliveryToDelete.class.getName()).list();
	return list;
    }

}
