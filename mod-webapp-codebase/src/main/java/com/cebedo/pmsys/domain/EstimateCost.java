package com.cebedo.pmsys.domain;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.base.IObjectDomain;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.enums.HTMLCSSDetails;
import com.cebedo.pmsys.enums.TypeEstimateCost;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.utils.HTMLUtils;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class EstimateCost implements IObjectDomain {

    private static final long serialVersionUID = 2824011225211953462L;

    // Keys.
    private Company company;
    private Project project;
    private UUID uuid;

    // Attributes.
    private String name;
    private double cost;
    private double actualCost;
    private TypeEstimateCost costType;

    public EstimateCost() {
	;
    }

    public EstimateCost(Project proj) {
	setProject(proj);
	setCompany(proj.getCompany());
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
	this.name = StringUtils.trim(name);
    }

    public double getCost() {
	return cost;
    }

    public String getCostAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getCost());
    }

    public void setCost(double cost) {
	this.cost = cost;
    }

    public TypeEstimateCost getCostType() {
	return costType;
    }

    public void setCostType(TypeEstimateCost costType) {
	this.costType = costType;
    }

    public double getActualCost() {
	return actualCost;
    }

    public double getDiffEstimatedActual() {
	return getCost() - getActualCost();
    }

    public String getDiffEstimatedActualAsString() {
	double diff = getDiffEstimatedActual();
	String diffStr = NumberFormatUtils.getCurrencyFormatter().format(diff);
	if (diff < 0) {
	    return diffStr.replace("&#8369;", "&#8369;-").replace("(", "").replace(")", "");
	}
	return diffStr;
    }

    public String getDiffEstimatedActualAsHTML() {
	String label = getDiffEstimatedActualAsString();
	double diff = getDiffEstimatedActual();
	if (diff < 0) {
	    return HTMLUtils.getBadgeHTML(HTMLCSSDetails.DANGER, label);
	}
	return HTMLUtils.getBadgeHTML(HTMLCSSDetails.SUCCESS, label);
    }

    public String getActualCostAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getActualCost());
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

    @Override
    public String toString() {
	return String.format("[(%s) %s: planned = %s, actual = %s]", this.costType, this.name, this.cost,
		this.actualCost);
    }

    @Override
    public String getObjectName() {
	return ConstantsRedis.OBJECT_ESTIMATE_COST;
    }
}
