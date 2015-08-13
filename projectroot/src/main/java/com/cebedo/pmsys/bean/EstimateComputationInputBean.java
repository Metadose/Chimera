package com.cebedo.pmsys.bean;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.enums.TableEstimationAllowance;
import com.cebedo.pmsys.model.Project;

public class EstimateComputationInputBean implements Serializable {

    private static final long serialVersionUID = -5059743520903240466L;

    private String name;
    private String remarks;
    private Project project;
    private TableEstimationAllowance estimationAllowance;
    private MultipartFile estimationFile;

    public EstimateComputationInputBean() {
	;
    }

    public EstimateComputationInputBean(Project proj) {
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

    public void setEstimationAllowance(TableEstimationAllowance estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    public MultipartFile getEstimationFile() {
	return estimationFile;
    }

    public void setEstimationFile(MultipartFile estimationFile) {
	this.estimationFile = estimationFile;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

}
