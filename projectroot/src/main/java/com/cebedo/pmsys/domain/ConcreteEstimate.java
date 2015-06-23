package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;

public class ConcreteEstimate implements IDomainObject {

    private static final long serialVersionUID = -3521975517764441834L;

    /**
     * Key parts.
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Basic specs.
     */
    private String name;
    private String remarks;
    private Date lastComputed;

    /**
     * Computational specs.
     */
    private Shape shape;
    private String[] formulaInputs;
    private ConcreteMixingRatio concreteMixingRatio;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

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

    public Date getLastComputed() {
	return lastComputed;
    }

    public void setLastComputed(Date lastComputed) {
	this.lastComputed = lastComputed;
    }

    public Shape getShape() {
	return shape;
    }

    public void setShape(Shape shape) {
	this.shape = shape;
    }

    public String[] getFormulaInputs() {
	return formulaInputs;
    }

    public void setFormulaInputs(String[] formulaInputs) {
	this.formulaInputs = formulaInputs;
    }

    public ConcreteMixingRatio getConcreteMixingRatio() {
	return concreteMixingRatio;
    }

    public void setConcreteMixingRatio(ConcreteMixingRatio concreteMixingRatio) {
	this.concreteMixingRatio = concreteMixingRatio;
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	// TODO Auto-generated method stub
	return null;
    }

}
