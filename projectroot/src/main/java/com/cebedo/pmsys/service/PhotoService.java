package com.cebedo.pmsys.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.model.Photo;
import com.cebedo.pmsys.ui.AlertBoxFactory;

public interface PhotoService {

	public AlertBoxFactory create(MultipartFile file, long projectID,
			String description) throws IOException;

	public Photo getByID(long id);

	public void update(Photo photo);

	public void delete(long id);

	public List<Photo> list();

	public void uploadProjectProfile(MultipartFile file, long projectID)
			throws IOException;

	public void uploadStaffProfile(MultipartFile file, long staffID)
			throws IOException;

	public void deleteProjectProfile(long projectID);

	public String getNameByID(long id);

}
