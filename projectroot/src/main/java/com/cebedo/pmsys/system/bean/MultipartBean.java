package com.cebedo.pmsys.system.bean;

import org.springframework.web.multipart.MultipartFile;

public class MultipartBean {

	private MultipartFile file;
	private long projectID;
	private String description;

	public MultipartBean() {
		;
	}

	public MultipartBean(int id) {
		setProjectID(id);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public long getProjectID() {
		return projectID;
	}

	public void setProjectID(long projectID) {
		this.projectID = projectID;
	}

}
