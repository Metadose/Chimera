package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.bean.EstimationInputBean;
import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.TableEstimationAllowance;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;

public class EstimationOutput implements IDomainObject {

    private static final long serialVersionUID = -4949844838990090245L;

    /**
     * Keys.
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Inputs.
     */
    private String name;
    private String remarks;

    /**
     * Output.
     */
    private List<Estimate> estimates;
    private String estimatesAsJson;
    private TableEstimationAllowance estimationAllowance;
    private Date lastComputed;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public EstimationOutput() {
	;
    }

    public EstimationOutput(EstimationInputBean estimateInput) {
	Project proj = estimateInput.getProject();
	setCompany(proj.getCompany());
	setProject(proj);
	setEstimationAllowance(estimateInput.getEstimationAllowance());
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public TableEstimationAllowance getEstimationAllowance() {
	return estimationAllowance;
    }

    public void setEstimationAllowance(
	    TableEstimationAllowance estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    public List<Estimate> getEstimates() {
	return estimates;
    }

    public void setEstimates(List<Estimate> estimates) {
	this.estimates = estimates;
    }

    public Date getLastComputed() {
	return lastComputed;
    }

    public void setLastComputed(Date lastComputed) {
	this.lastComputed = lastComputed;
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_ESTIMATION_OUTPUT,
		this.company.getId(), this.project.getId(), this.uuid);
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof EstimationOutput ? ((EstimationOutput) obj)
		.getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    public static String constructPattern(Project proj) {
	return String.format(RedisKeyRegistry.KEY_ESTIMATION_OUTPUT, proj
		.getCompany().getId(), proj.getId(), "*");
    }

    public String getEstimatesAsJson() {
	return estimatesAsJson;
    }

    public void setEstimatesAsJson(String estimatesAsJson) {
	this.estimatesAsJson = estimatesAsJson;
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
