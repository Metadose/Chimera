package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;

public class EstimateCost implements IDomainObject {

    private static final long serialVersionUID = 2824011225211953462L;

    // Keys.
    private Company company;
    private Project project;
    private UUID uuid;

    // Attributes.
    private String name;
    private double cost;
    private double actualCost;
    private EstimateCostType costType;

    private Map<String, Object> extMap;

    public EstimateCost() {
	;
    }

    public EstimateCost(Project proj) {
	setProject(proj);
	setCompany(proj.getCompany());
    }

    @Override
    public Map<String, Object> getExtMap() {
	return extMap;
    }

    @Override
    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(RegistryRedisKeys.KEY_ESTIMATED_COST, this.company.getId(),
		this.project.getId(), this.uuid);
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

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public double getCost() {
	return cost;
    }

    public void setCost(double cost) {
	this.cost = cost;
    }

    public EstimateCostType getCostType() {
	return costType;
    }

    public void setCostType(EstimateCostType costType) {
	this.costType = costType;
    }

    public double getActualCost() {
	return actualCost;
    }

    public void setActualCost(double actualCost) {
	this.actualCost = actualCost;
    }

    public static String constructPattern(Project proj) {
	return String.format(RegistryRedisKeys.KEY_ESTIMATED_COST, proj.getCompany().getId(),
		proj.getId(), "*");
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof EstimateCost ? ((EstimateCost) obj).getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
