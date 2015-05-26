package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Material;
import com.cebedo.pmsys.model.Project;

@Repository
public class MaterialDAOImpl implements MaterialDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Material material) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(material);
    }

    @Override
    public Material getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Material material = (Material) this.daoHelper.criteriaGetObjByID(
		session, Material.class, Material.PROPERTY_ID, id)
		.uniqueResult();
	return material;
    }

    @Override
    public void update(Material material) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(material);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Material material = getByID(id);
	if (material != null) {
	    session.delete(material);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Material> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Material> list = session.createQuery(
		"FROM " + Material.class.getName()).list();
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Material> list(Project project) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Material> list = this.daoHelper.criteriaGetObjByID(session,
		Material.class, Material.PROPERTY_PROJECT, project).list();
	return list;
    }

}
