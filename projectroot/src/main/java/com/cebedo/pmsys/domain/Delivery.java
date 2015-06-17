package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

public class Delivery implements IDomainObject {

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("Delivery");

    /**
     * Key:
     */
    private Company company;
    private Project project;
    private Date datetime;
    private UUID uuid;

    /**
     * Details.
     */
    private String name;
    private String description;

    /**
     * More details.
     */
    private Set<Material> materials;
    private Set<Staff> staff;

    /**
     * Extension map.
     */
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
	// TODO Auto-generated method stub
	return null;
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

    public Date getDatetime() {
	return datetime;
    }

    public void setDatetime(Date datetime) {
	this.datetime = datetime;
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

    public Set<Material> getMaterials() {
	return materials;
    }

    public void setMaterials(Set<Material> materials) {
	this.materials = materials;
    }

    public Set<Staff> getStaff() {
	return staff;
    }

    public void setStaff(Set<Staff> staff) {
	this.staff = staff;
    }

}
