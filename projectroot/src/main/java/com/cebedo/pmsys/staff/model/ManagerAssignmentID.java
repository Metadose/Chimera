package com.cebedo.pmsys.staff.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.cebedo.pmsys.project.model.Project;

@Embeddable
public class ManagerAssignmentID implements Serializable {

	private Project project;
	private Staff manager;
	private static final long serialVersionUID = 1L;

	@ManyToOne
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne
	public Staff getManager() {
		return manager;
	}

	public void setManager(Staff manager) {
		this.manager = manager;
	}

}
