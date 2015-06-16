package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.MaterialToRemove;
import com.cebedo.pmsys.model.Project;

@Repository
public class MaterialDAOImpl implements MaterialDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(MaterialToRemove material) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(material);
    }

    @Override
    public MaterialToRemove getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	MaterialToRemove material = (MaterialToRemove) this.daoHelper.criteriaGetObjByID(
		session, MaterialToRemove.class, MaterialToRemove.PROPERTY_ID, id)
		.uniqueResult();
	return material;
    }

    @Override
    public void update(MaterialToRemove material) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(material);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	MaterialToRemove material = getByID(id);
	if (material != null) {
	    session.delete(material);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MaterialToRemove> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<MaterialToRemove> list = session.createQuery(
		"FROM " + MaterialToRemove.class.getName()).list();
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MaterialToRemove> list(Project project) {
	Session session = this.sessionFactory.getCurrentSession();
	List<MaterialToRemove> list = this.daoHelper.criteriaGetObjByID(session,
		MaterialToRemove.class, MaterialToRemove.PROPERTY_PROJECT, project).list();
	return list;
    }

}
