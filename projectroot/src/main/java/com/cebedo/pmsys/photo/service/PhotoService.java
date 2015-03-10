package com.cebedo.pmsys.photo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.photo.model.Photo;

public interface PhotoService {

	public void create(MultipartFile file, long projectID, String description)
			throws IOException;

	public Photo getByID(long id);

	public void update(Photo photo);

	public void delete(long id);

	public List<Photo> list();

	public void uploadProjectProfile(MultipartFile file, long projectID)
			throws IOException;

	public void uploadStaffProfile(MultipartFile file, long staffID)
			throws IOException;

	public void deleteProjectProfile(long projectID);

}
