package com.cebedo.pmsys.bean;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.model.Project;

public class MassUploadStaffBean {

    private Project project;
    private MultipartFile staffFile;

    public MassUploadStaffBean() {
	;
    }

    public MassUploadStaffBean(Project proj) {
	setProject(proj);
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public MultipartFile getStaffFile() {
	return staffFile;
    }

    public void setStaffFile(MultipartFile staffFile) {
	this.staffFile = staffFile;
    }

}
