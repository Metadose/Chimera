package com.cebedo.pmsys.photo.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.photo.dao.PhotoDAO;
import com.cebedo.pmsys.photo.model.Photo;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.Staff;

@Service
public class PhotoServiceImpl implements PhotoService {

	private PhotoDAO photoDAO;
	private ProjectDAO projectDAO;
	private StaffDAO staffDAO;

	public void setStaffDAO(StaffDAO staffDAO) {
		this.staffDAO = staffDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setPhotoDAO(PhotoDAO photoDAO) {
		this.photoDAO = photoDAO;
	}

	@Override
	@Transactional
	public void deleteProjectProfile(long projectID) {
		Project proj = this.projectDAO.getByID(projectID);

		// Delete the physical file.
		String path = proj.getThumbnailURL();
		File file = new File(path);
		file.delete();

		// Null the record.
		proj.setThumbnailURL(null);
		this.projectDAO.update(proj);
	}

	@Override
	@Transactional
	public void uploadStaffProfile(MultipartFile file, String fileLocation,
			long staffID) throws IOException {
		Staff staff = this.staffDAO.getByID(staffID);
		staff.setThumbnailURL(fileLocation);
		fileUpload(file, fileLocation);
		this.staffDAO.update(staff);
	}

	@Override
	@Transactional
	public void uploadProjectProfile(MultipartFile file, String fileLocation,
			long projectID) throws IOException {
		Project proj = this.projectDAO.getByID(projectID);
		proj.setThumbnailURL(fileLocation);
		fileUpload(file, fileLocation);
		this.projectDAO.update(proj);
	}

	@Override
	@Transactional
	public void create(MultipartFile file, String fileLocation, Photo photo)
			throws IOException {
		fileUpload(file, fileLocation);
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

	/**
	 * Delete actual physical file and delete photo record.
	 */
	@Override
	@Transactional
	public void delete(long id) {
		Photo photo = this.photoDAO.getByID(id);
		File photoFile = new File(photo.getLocation());
		photoFile.delete();
		this.photoDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Photo> list() {
		return this.photoDAO.list();
	}

	/**
	 * Upload a file.
	 * 
	 * @param file
	 * @param id
	 * @param objectName
	 * @throws IOException
	 */
	private void fileUpload(MultipartFile file, String fileLocation)
			throws IOException {
		// Prelims.
		byte[] bytes = file.getBytes();
		checkDirectoryExistence(fileLocation);

		// Upload file.
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(new File(fileLocation)));
		stream.write(bytes);
		stream.close();
	}

	/**
	 * Helper function to create non-existing folders.
	 * 
	 * @param fileLocation
	 */
	private void checkDirectoryExistence(String fileLocation) {
		File file = new File(fileLocation);
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

}
