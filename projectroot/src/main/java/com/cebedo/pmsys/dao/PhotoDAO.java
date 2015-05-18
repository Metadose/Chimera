package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Photo;

public interface PhotoDAO {

	public void create(Photo photo);

	public Photo getByID(long id);

	public void update(Photo photo);

	public void delete(long id);

	public List<Photo> list(Long companyID);

	public String getNameByID(long id);

}
