package com.cebedo.pmsys.photo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.common.DAOHelper;
import com.cebedo.pmsys.photo.model.Photo;

@Repository
public class PhotoDAOImpl implements PhotoDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(PhotoDAOImpl.class);
	private SessionFactory sessionFactory;
	private DAOHelper daoHelper = new DAOHelper();

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Photo photo) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(photo);
		logger.info("[Create] Photo: " + photo);
	}

	@Override
	public Photo getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Photo photo = (Photo) session.load(Photo.class.getName(), new Long(id));
		logger.info("[Get by ID] Photo: " + photo);
		return photo;
	}

	@Override
	public void update(Photo photo) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(photo);
		logger.info("[Update] Photo:" + photo);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Photo photo = getByID(id);
		if (photo != null) {
			session.delete(photo);
		}
		logger.info("[Delete] Photo: " + photo);
	}

	@SuppressWarnings("unchecked")
	public List<Photo> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = this.daoHelper.getSelectQueryFilterCompany(session,
				Photo.class.getName(), companyID);
		List<Photo> photoList = query.list();
		return photoList;
	}

	@Override
	public String getNameByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		String result = this.daoHelper.getProjectionByID(session, Photo.class,
				Photo.PROPERTY_ID, id, Photo.PROPERTY_NAME);
		return result;
	}
}
