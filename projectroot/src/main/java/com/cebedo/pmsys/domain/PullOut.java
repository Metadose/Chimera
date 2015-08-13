package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

public class PullOut implements IDomainObject {

    private static final long serialVersionUID = 2087762852857215717L;

    /**
     * Key: company.fk:%s:project.fk:%s:delivery.fk:%s:material.fk:%s:pullout:%s
     */
    private Company company;
    private Project project;
    private Delivery delivery;
    private Material material;
    private UUID uuid;

    /**
     * Specs.
     */
    private Staff staff; // Who pulled it out?
    private Date datetime; // When was the pull-out done? And what time?
    private double quantity; // Used "double", 'cause what if non-integer units
			     // like kilos or liters.
    private String remarks;

    /**
     * Bean-backed form.
     */
    private long staffID;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public PullOut() {
	;
    }

    public PullOut(Material m) {
	setCompany(m.getCompany());
	setProject(m.getProject());
	setDelivery(m.getDelivery());
	setMaterial(m);
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	// company.fk:%s:project.fk:%s:delivery.fk:%s:material.fk:%s:pullout:%s
	return String.format(RegistryRedisKeys.KEY_PULL_OUT, this.company.getId(), this.project.getId(),
		this.delivery.getUuid(), this.material.getUuid(), this.uuid);
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

    public Delivery getDelivery() {
	return delivery;
    }

    public void setDelivery(Delivery delivery) {
	this.delivery = delivery;
    }

    public Material getMaterial() {
	return material;
    }

    public void setMaterial(Material material) {
	this.material = material;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public Staff getStaff() {
	return staff;
    }

    public void setStaff(Staff staff) {
	this.staff = staff;
    }

    public Date getDatetime() {
	return datetime;
    }

    public void setDatetime(Date datetime) {
	this.datetime = datetime;
    }

    public double getQuantity() {
	return quantity;
    }

    public void setQuantity(double quantity) {
	this.quantity = quantity;
    }

    public long getStaffID() {
	return staffID;
    }

    public void setStaffID(long staffID) {
	this.staffID = staffID;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public static String constructPattern(Material material2) {
	Company company = material2.getCompany();
	Project project = material2.getProject();
	Delivery delivery = material2.getDelivery();
	return String.format(RegistryRedisKeys.KEY_PULL_OUT, company.getId(), project.getId(),
		delivery.getUuid(), material2.getUuid(), "*");
    }

    public static String constructPattern(Project project) {
	Company company = project.getCompany();
	return String.format(RegistryRedisKeys.KEY_PULL_OUT, company.getId(), project.getId(), "*", "*",
		"*");
    }

    public static String constructPattern(Delivery delivery2) {
	Company company = delivery2.getCompany();
	Project project = delivery2.getProject();
	return String.format(RegistryRedisKeys.KEY_PULL_OUT, company.getId(), project.getId(),
		delivery2.getUuid(), "*", "*");
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof PullOut ? ((PullOut) obj).getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
