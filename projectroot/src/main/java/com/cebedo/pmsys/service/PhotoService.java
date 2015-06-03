package com.cebedo.pmsys.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.model.Photo;

public interface PhotoService {

    /**
     * Upload a new photo.
     * 
     * @param file
     * @param projectID
     * @param description
     * @return
     * @throws IOException
     */
    public String create(MultipartFile file, long projectID, String description)
	    throws IOException;

    public Photo getByID(long id);

    /**
     * Update the photo.
     * 
     * @param photo
     * @return
     */
    public String update(Photo photo);

    /**
     * Delete a photo.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

    public List<Photo> list();

    /**
     * Upload a project profile pic.
     * 
     * @param file
     * @param projectID
     * @return
     * @throws IOException
     */
    public String uploadProjectProfile(MultipartFile file, long projectID)
	    throws IOException;

    /**
     * Upload a staff profile pic.
     * 
     * @param file
     * @param staffID
     * @return
     * @throws IOException
     */
    public String uploadStaffProfile(MultipartFile file, long staffID)
	    throws IOException;

    /**
     * Delete a profile pic of a project.
     * 
     * @param projectID
     * @return
     */
    public String deleteProjectProfile(long projectID);

    public String getNameByID(long id);

}
