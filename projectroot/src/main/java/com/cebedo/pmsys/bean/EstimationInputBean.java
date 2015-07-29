package com.cebedo.pmsys.bean;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.enums.TableEstimationAllowance;
import com.cebedo.pmsys.model.Project;

public class EstimationInputBean {

    private Project project;
    private TableEstimationAllowance estimationAllowance;
    private MultipartFile estimationFile;

    public EstimationInputBean() {
	;
    }

    public EstimationInputBean(Project proj) {
	setProject(proj);
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public TableEstimationAllowance getEstimationAllowance() {
	return estimationAllowance;
    }

    public void setEstimationAllowance(
	    TableEstimationAllowance estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    public MultipartFile getEstimationFile() {
	return estimationFile;
    }

    public void setEstimationFile(MultipartFile estimationFile) {
	this.estimationFile = estimationFile;
    }

}
