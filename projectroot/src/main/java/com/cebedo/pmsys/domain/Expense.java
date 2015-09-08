package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

public class Expense implements IDomainObject {

    private static final long serialVersionUID = -7013450034228364135L;

    // Keys.
    private Company company;
    private Project project;
    private UUID uuid;

    // Attributes.
    private String name;
    private double cost;
    private Staff staff;
    private Date date;

    private Map<String, Object> extMap;

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
	return String.format(RegistryRedisKeys.KEY_EXPENSE, this.company.getId(), this.project.getId(),
		this.uuid);
    }

    public static String constructPattern(Project proj) {
	return String
		.format(RegistryRedisKeys.KEY_EXPENSE, proj.getCompany().getId(), proj.getId(), "*");
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Expense ? ((Expense) obj).getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
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
	this.name = StringUtils.trim(name);
    }

    public double getCost() {
	return cost;
    }

    public void setCost(double cost) {
	this.cost = cost;
    }

    public Staff getStaff() {
	return staff;
    }

    public void setStaff(Staff staff) {
	this.staff = staff;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

}
