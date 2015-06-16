package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Delivery;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

public class Material implements IDomainObject {

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("Material");

    /**
     * Keys:
     * company:2321:project:1123:inventory:delivery:1123:material:12d1-123sd12
     * -sad123-asd
     */
    private Company company;
    private Project project;
    private Delivery delivery;
    private UUID uuid;
    // Keys also include an auto-increment "material".

    /**
     * Detail fields.
     */
    private String name;
    private String remarks;

    /**
     * Specifications.
     */
    private double quantity;
    private String unit;

    /**
     * Cost per unit.
     */
    private double costPerUnitMaterial;
    private double costPerUnitLabor;
    private double costPerUnitEquipment;
    private double costPerUnitTotal;

    /**
     * Total cost per unit (costPerUnit * quantity).
     */
    private double totalCostPerUnitMaterial;
    private double totalCostPerUnitLabor;
    private double totalCostPerUnitEquipment;
    private double totalCost;

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

    public static String constructKey(Company company, Project project,
	    Delivery delivery, UUID uuid) {
	String companyPart = Company.OBJECT_NAME + ":" + company.getId() + ":";
	String projectPart = Project.OBJECT_NAME + ":" + project.getId() + ":";
	String inventoryPart = "inventory:";
	String deliveryPart = Delivery.OBJECT_NAME + ":" + delivery.getId()
		+ ":";
	String materialPart = RedisConstants.OBJECT_MATERIAL + ":" + uuid;
	String key = companyPart + projectPart + inventoryPart + deliveryPart
		+ materialPart;
	return key;
    }

    @Override
    public String getKey() {
	// company:2321:project:1123:inventory:delivery:1123:material:123-123
	String companyPart = Company.OBJECT_NAME + ":" + this.company.getId()
		+ ":";
	String projectPart = Project.OBJECT_NAME + ":" + this.project.getId()
		+ ":";
	String inventoryPart = "inventory:";
	String deliveryPart = Delivery.OBJECT_NAME + ":"
		+ this.delivery.getId() + ":";
	String materialPart = RedisConstants.OBJECT_MATERIAL + ":" + this.uuid;
	String key = companyPart + projectPart + inventoryPart + deliveryPart
		+ materialPart;
	return key;
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

    public double getQuantity() {
	return quantity;
    }

    public void setQuantity(double quantity) {
	this.quantity = quantity;
    }

    public String getUnit() {
	return unit;
    }

    public void setUnit(String unit) {
	this.unit = unit;
    }

    public double getCostPerUnitMaterial() {
	return costPerUnitMaterial;
    }

    public void setCostPerUnitMaterial(double costPerUnitMaterial) {
	this.costPerUnitMaterial = costPerUnitMaterial;
    }

    public double getCostPerUnitLabor() {
	return costPerUnitLabor;
    }

    public void setCostPerUnitLabor(double costPerUnitLabor) {
	this.costPerUnitLabor = costPerUnitLabor;
    }

    public double getCostPerUnitEquipment() {
	return costPerUnitEquipment;
    }

    public void setCostPerUnitEquipment(double costPerUnitEquipment) {
	this.costPerUnitEquipment = costPerUnitEquipment;
    }

    public double getCostPerUnitTotal() {
	return costPerUnitTotal;
    }

    public void setCostPerUnitTotal(double costPerUnitTotal) {
	this.costPerUnitTotal = costPerUnitTotal;
    }

    public double getTotalCostPerUnitMaterial() {
	return totalCostPerUnitMaterial;
    }

    public void setTotalCostPerUnitMaterial(double totalCostPerUnitMaterial) {
	this.totalCostPerUnitMaterial = totalCostPerUnitMaterial;
    }

    public double getTotalCostPerUnitLabor() {
	return totalCostPerUnitLabor;
    }

    public void setTotalCostPerUnitLabor(double totalCostPerUnitLabor) {
	this.totalCostPerUnitLabor = totalCostPerUnitLabor;
    }

    public double getTotalCostPerUnitEquipment() {
	return totalCostPerUnitEquipment;
    }

    public void setTotalCostPerUnitEquipment(double totalCostPerUnitEquipment) {
	this.totalCostPerUnitEquipment = totalCostPerUnitEquipment;
    }

    public double getTotalCost() {
	return totalCost;
    }

    public void setTotalCost(double totalCost) {
	this.totalCost = totalCost;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

}
