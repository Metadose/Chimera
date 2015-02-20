package com.cebedo.pmsys.photo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.photo.dao.PhotoDAO;
import com.cebedo.pmsys.photo.model.Photo;

@Service
public class PhotoServiceImpl implements PhotoService {

	private PhotoDAO photoDAO;

	public void setPhotoDAO(PhotoDAO photoDAO) {
		this.photoDAO = photoDAO;
	}

	@Override
	@Transactional
	public void create(Photo photo) {
		this.photoDAO.create(photo);
	}

	@Override
	@Transactional
	public Photo getByID(long id) {
		return this.photoDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(Photo photo) {
		this.photoDAO.update(photo);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.photoDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Photo> list() {
		return this.photoDAO.list();
	}

}
