package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class Delivery implements IDomainObject {

    private static final long serialVersionUID = 2539632179017470796L;
    /**
     * Key: "company:%s:project:%s:delivery:uuid:%s"
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Details.
     */
    private String name;
    private String description;
    private Date datetime;

    /**
     * More details.
     */
    private double grandTotalOfMaterials;
    private Set<Material> materials;
    private Set<Staff> staff;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Delivery() {
	;
    }

    public Delivery(Project proj) {
	setCompany(proj.getCompany());
	setProject(proj);
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
	// "company:%s:project:%s:delivery:uuid:%s"
	return String.format(RedisKeyRegistry.KEY_DELIVERY,
		this.company.getId(), this.project.getId(), this.uuid);
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

    public String getGrandTotalOfMaterialsAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		grandTotalOfMaterials);
    }

    public double getGrandTotalOfMaterials() {
	return grandTotalOfMaterials;
    }

    public void setGrandTotalOfMaterials(double grandTotalOfMaterials) {
	this.grandTotalOfMaterials = grandTotalOfMaterials;
    }

    public Set<Staff> getStaff() {
	return staff;
    }

    public void setStaff(Set<Staff> staff) {
	this.staff = staff;
    }

    /**
     * company:%s:project:%s:delivery:uuid:%s
     * 
     * @param project
     * @return
     */
    public static String constructPattern(Project project) {
	Company company = project.getCompany();
	return String.format(RedisKeyRegistry.KEY_DELIVERY, company.getId(),
		project.getId(), "*");
    }

}