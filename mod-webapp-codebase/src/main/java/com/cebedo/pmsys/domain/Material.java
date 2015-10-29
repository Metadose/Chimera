package com.cebedo.pmsys.domain;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.CommonMassUnit;
import com.cebedo.pmsys.enums.CommonVolumeUnit;
import com.cebedo.pmsys.enums.MaterialCategory;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class Material implements IDomainObject {

    private static final long serialVersionUID = 2300406369596684113L;
    /**
     * Keys: company:%s:project:%s:delivery:%s:material:%s
     */
    private Company company;
    private Project project;
    private Delivery delivery;
    private UUID uuid;

    /**
     * Detail fields.
     */
    private String name;
    private String remarks;

    /**
     * Specifications.
     */
    private double quantity;
    private double used;
    private double available;

    /**
     * Bean-backed form.
     * 
     */
    private MaterialCategory materialCategory;
    private String unitOfMeasure;

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

    public Material() {
	;
    }

    public Material(Delivery delivery2) {
	setCompany(delivery2.getCompany());
	setProject(delivery2.getProject());
	setDelivery(delivery2);
    }

    @Override
    public String getKey() {
	// company:%s:project:%s:delivery:%s:material:%s
	return String.format(RegistryRedisKeys.KEY_MATERIAL, this.company.getId(), this.project.getId(),
		this.delivery.getUuid(), this.uuid);
    }

    public String getUnitName() {
	String unitStr = getUnitOfMeasure();
	// If Length.
	try {
	    CommonLengthUnit unitOfMeasure = CommonLengthUnit.valueOf(unitStr);
	    return unitOfMeasure.getLabel();
	} catch (IllegalArgumentException e) {

	    // If Mass.
	    try {
		CommonMassUnit unitOfMeasure = CommonMassUnit.valueOf(unitStr);
		return unitOfMeasure.getLabel();
	    } catch (IllegalArgumentException e2) {

		// If Volume.
		try {
		    CommonVolumeUnit unitOfMeasure = CommonVolumeUnit.valueOf(unitStr);
		    return unitOfMeasure.getLabel();
		}
		// Others.
		catch (IllegalArgumentException e3) {
		    return unitStr;
		}
	    }
	}
    }

    public String getUnitSymbol() {
	String unitStr = getUnitOfMeasure();
	// If Length.
	try {
	    CommonLengthUnit unitOfMeasure = CommonLengthUnit.valueOf(unitStr);
	    return unitOfMeasure.getSymbol();
	} catch (IllegalArgumentException e) {

	    // If Mass.
	    try {
		CommonMassUnit unitOfMeasure = CommonMassUnit.valueOf(unitStr);
		return unitOfMeasure.getSymbol();
	    } catch (IllegalArgumentException e2) {
		// If Volume.
		try {
		    CommonVolumeUnit unitOfMeasure = CommonVolumeUnit.valueOf(unitStr);
		    return unitOfMeasure.getSymbol();
		}
		// Others.
		catch (IllegalArgumentException e3) {
		    return org.springframework.util.StringUtils.uncapitalize(unitStr);
		}
	    }
	}
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
	this.name = StringUtils.trim(name);
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = StringUtils.trim(remarks);
    }

    public double getQuantity() {
	return quantity;
    }

    public void setQuantity(double quantity) {
	this.quantity = quantity;
    }

    public String getCostPerUnitMaterialAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(costPerUnitMaterial);
    }

    public double getCostPerUnitMaterial() {
	return costPerUnitMaterial;
    }

    public void setCostPerUnitMaterial(double costPerUnitMaterial) {
	this.costPerUnitMaterial = costPerUnitMaterial;
    }

    @Deprecated
    public double getCostPerUnitLabor() {
	return costPerUnitLabor;
    }

    public void setCostPerUnitLabor(double costPerUnitLabor) {
	this.costPerUnitLabor = costPerUnitLabor;
    }

    @Deprecated
    public double getCostPerUnitEquipment() {
	return costPerUnitEquipment;
    }

    public void setCostPerUnitEquipment(double costPerUnitEquipment) {
	this.costPerUnitEquipment = costPerUnitEquipment;
    }

    @Deprecated
    public double getCostPerUnitTotal() {
	return costPerUnitTotal;
    }

    public void setCostPerUnitTotal(double costPerUnitTotal) {
	this.costPerUnitTotal = costPerUnitTotal;
    }

    public String getTotalCostPerUnitMaterialAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(totalCostPerUnitMaterial);
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

    @Deprecated
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

    public double getUsed() {
	return used;
    }

    public void setUsed(double used) {
	this.used = used;
    }

    public double getAvailable() {
	return available;
    }

    public String getAvailableCSS() {
	int avail = getAvailableAsInt();

	if (avail >= 75 && avail <= 100) {
	    return "success";
	}

	else if (avail >= 50 && avail < 75) {
	    return "info";
	}

	else if (avail >= 25 && avail < 50) {
	    return "warning";
	}

	return "danger";
    }

    private int getAvailableAsInt() {
	return (int) ((available / quantity) * 100);
    }

    public String getAvailableAsPercentageForDisplay() {
	int percentDisplay = getAvailableAsInt();
	return percentDisplay + "%";
    }

    public String getAvailableAsPercentage() {
	int percentDisplay = getAvailableAsInt();
	if (percentDisplay <= 0) {
	    return "100%";
	}
	return percentDisplay + "%";
    }

    public void setAvailable(double available) {
	this.available = available;
    }

    public static String constructPattern(Delivery delivery2) {
	Company company = delivery2.getCompany();
	Project project = delivery2.getProject();
	return String.format(RegistryRedisKeys.KEY_MATERIAL, company.getId(), project.getId(),
		delivery2.getUuid(), "*");
    }

    public static String constructPattern(Project project) {
	Company company = project.getCompany();
	return String.format(RegistryRedisKeys.KEY_MATERIAL, company.getId(), project.getId(), "*", "*");
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Material ? ((Material) obj).getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    public MaterialCategory getMaterialCategory() {
	return materialCategory;
    }

    public void setMaterialCategory(MaterialCategory materialCategory) {
	this.materialCategory = materialCategory;
    }

    public String getUnitOfMeasure() {
	return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
	this.unitOfMeasure = unitOfMeasure;
    }

}
