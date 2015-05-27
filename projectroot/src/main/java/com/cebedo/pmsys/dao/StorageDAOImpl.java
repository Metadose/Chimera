package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Storage;

@Repository
public class StorageDAOImpl implements StorageDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Storage storage) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(storage);
    }

    @Override
    public Storage getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Storage storage = (Storage) this.daoHelper.criteriaGetObjByID(session,
		Storage.class, Storage.PROPERTY_ID, id).uniqueResult();
	return storage;
    }

    @Override
    public void update(Storage storage) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(storage);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Storage storage = getByID(id);
	if (storage != null) {
	    session.delete(storage);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Storage> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Storage> list = session.createQuery(
		"FROM " + Storage.class.getName()).list();
	return list;
    }

}
