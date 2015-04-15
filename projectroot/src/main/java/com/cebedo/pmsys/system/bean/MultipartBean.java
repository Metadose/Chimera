package com.cebedo.pmsys.system.bean;

import org.springframework.web.multipart.MultipartFile;

public class MultipartBean {

	private MultipartFile file;
	private long projectID;

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
