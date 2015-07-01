package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class EstimationAllowance implements IDomainObject {

    private static final long serialVersionUID = 2095896472605992484L;

    /**
     * Key parts.
     */
    private Company company;
    private UUID uuid;

    /**
     * Basic details.
     */
    private String name;
    private String description;

    /**
     * Specs.
     */
    private double estimationAllowance; // Percent in JSP. Stored as double in
					// Java.

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public EstimationAllowance() {
	;
    }

    public EstimationAllowance(Company company2) {
	setCompany(company2);
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
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

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public double getEstimationAllowance() {
	return estimationAllowance;
    }

    public String getEstimationAllowanceAsString() {
	return (estimationAllowance * 100) + "%";
    }

    public void setEstimationAllowance(double estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_ESTIMATION_ALLOWANCE,
		this.company.getId(), this.uuid);
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_ESTIMATION_ALLOWANCE,
		company2.getId(), "*");
    }

}
