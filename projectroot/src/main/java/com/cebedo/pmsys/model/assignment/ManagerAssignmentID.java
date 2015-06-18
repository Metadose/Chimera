package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

@Embeddable
public class ManagerAssignmentID implements Serializable {

    private static final long serialVersionUID = -6221696169093772976L;
    private Project project;
    private Staff manager;

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
