package com.cebedo.pmsys.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.model.ProjectFile;

public interface ProjectFileService {

    /**
     * Upload a file to project.
     * 
     * @param file
     * @param projectID
     * @param description
     * @return
     * @throws IOException
     */
    public String uploadFileToProject(MultipartFile file, long projectID,
	    String description) throws IOException;

    /**
     * Upload a file to staff.
     * 
     * @param file
     * @param description
     * @return
     * @throws IOException
     */
    public String uploadFileToStaff(MultipartFile file, String description)
	    throws IOException;

    public ProjectFile getByID(long id);

    /**
     * Update a project file.
     * 
     * @param projectFile
     * @return
     */
    public String update(ProjectFile projectFile);

    /**
     * Delete a project file.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

    public List<ProjectFile> list();

    public List<ProjectFile> listWithAllCollections();

    /**
     * Update description of a file.
     * 
     * @param fileID
     * @param description
     * @return
     */
    public String updateDescription(long fileID, String description);

    public File getPhysicalFileByID(long fileID);

    public String getNameByID(long fileID);
}
