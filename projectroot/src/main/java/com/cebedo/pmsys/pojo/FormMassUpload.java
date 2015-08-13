package com.cebedo.pmsys.pojo;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.model.Project;

public class FormMassUpload {

    private Project project;
    private MultipartFile file;

    public FormMassUpload() {
	;
    }

    public FormMassUpload(Project proj) {
	setProject(proj);
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public MultipartFile getFile() {
	return file;
    }

    public void setFile(MultipartFile staffFile) {
	this.file = staffFile;
    }

}
