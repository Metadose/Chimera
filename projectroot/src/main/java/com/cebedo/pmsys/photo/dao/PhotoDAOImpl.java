package com.cebedo.pmsys.photo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.photo.model.Photo;

@Repository
public class PhotoDAOImpl implements PhotoDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(PhotoDAOImpl.class);
	private SessionFactory sessionFactory;

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
	public List<Photo> list() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Photo> photoList = session.createQuery("from " + Photo.CLASS_NAME)
				.list();
		for (Photo photo : photoList) {
			logger.info("[List] Photo: " + photo);
		}
		return photoList;
	}

}
