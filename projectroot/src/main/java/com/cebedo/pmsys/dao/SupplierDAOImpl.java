package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Supplier;

@Repository
public class SupplierDAOImpl implements SupplierDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Supplier supplier) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(supplier);
    }

    @Override
    public Supplier getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Supplier supplier = (Supplier) this.daoHelper.criteriaGetObjByID(
		session, Supplier.class, Supplier.PROPERTY_ID, id)
		.uniqueResult();
	return supplier;
    }

    @Override
    public void update(Supplier supplier) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(supplier);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Supplier supplier = getByID(id);
	if (supplier != null) {
	    session.delete(supplier);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Supplier> list = session.createQuery(
		"FROM " + Supplier.class.getName()).list();
	return list;
    }

}
