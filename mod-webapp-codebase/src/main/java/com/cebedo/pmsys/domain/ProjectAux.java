package com.cebedo.pmsys.domain;

import java.util.Map;

import javax.persistence.Transient;

import com.cebedo.pmsys.base.IObjectDomain;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.enums.HTMLCSSDetails;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.utils.HTMLUtils;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class ProjectAux implements IObjectDomain {

    private static final long serialVersionUID = 4250896962237506975L;

    private Company company;
    private Project project;

    /**
     * Grand totals of expenses.
     */
    private double grandTotalDelivery;
    private double grandTotalPayroll;
    private double grandTotalOtherExpenses;
    private double grandTotalEquipmentExpenses;

    // Estimate costs.
    private double grandTotalCostsDirect;
    private double grandTotalCostsIndirect;
    private double grandTotalActualCostsDirect;
    private double grandTotalActualCostsIndirect;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public ProjectAux() {
	;
    }

    public ProjectAux(Project project2) {
	Company co = project2.getCompany();
	setCompany(co);
	setProject(project2);
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    /**
     * company.fk:%s:project:%s
     */
    @Override
    public String getKey() {
	return String.format(RegistryRedisKeys.KEY_AUX_PROJECT, this.company.getId(),
		this.project.getId());
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

    public static String constructKey(Project project) {
	Company company = project.getCompany();
	return String.format(RegistryRedisKeys.KEY_AUX_PROJECT, company.getId(), project.getId());
    }

    public double getPlannedTotalProject() {
	return grandTotalCostsDirect + grandTotalCostsIndirect;
    }

    public double getDiffEstimatedActualTotal() {
	return getPlannedTotalProject() - getActualTotalProject();
    }

    public String getDiffEstimatedActualTotalAsString() {
	double diff = getDiffEstimatedActualTotal();
	String diffStr = NumberFormatUtils.getCurrencyFormatter().format(diff);
	if (diff < 0) {
	    return diffStr.replace("&#8369;", "&#8369;-").replace("(", "").replace(")", "");
	}
	return diffStr;
    }

    public String getDiffEstimatedActualTotalAsHTML() {
	String label = getDiffEstimatedActualTotalAsString();
	double diff = getDiffEstimatedActualTotal();
	if (diff < 0) {
	    return HTMLUtils.getBadgeHTML(HTMLCSSDetails.DANGER, label);
	}
	return HTMLUtils.getBadgeHTML(HTMLCSSDetails.SUCCESS, label);
    }

    public double getDiffEstimatedActualDirect() {
	return getGrandTotalCostsDirect() - getGrandTotalActualCostsDirect();
    }

    public String getDiffEstimatedActualDirectAsString() {
	double diff = getDiffEstimatedActualDirect();
	String diffStr = NumberFormatUtils.getCurrencyFormatter().format(diff);
	if (diff < 0) {
	    return diffStr.replace("&#8369;", "&#8369;-").replace("(", "").replace(")", "");
	}
	return diffStr;
    }

    public String getDiffEstimatedActualDirectAsHTML() {
	String label = getDiffEstimatedActualDirectAsString();
	double diff = getDiffEstimatedActualDirect();
	if (diff < 0) {
	    return HTMLUtils.getBadgeHTML(HTMLCSSDetails.DANGER, label);
	}
	return HTMLUtils.getBadgeHTML(HTMLCSSDetails.SUCCESS, label);
    }

    public double getDiffEstimatedActualIndirect() {
	return getGrandTotalCostsIndirect() - getGrandTotalActualCostsIndirect();
    }

    public String getDiffEstimatedActualIndirectAsString() {
	double diff = getDiffEstimatedActualIndirect();
	String diffStr = NumberFormatUtils.getCurrencyFormatter().format(diff);
	if (diff < 0) {
	    return diffStr.replace("&#8369;", "&#8369;-").replace("(", "").replace(")", "");
	}
	return diffStr;
    }

    public String getDiffEstimatedActualIndirectAsHTML() {
	String label = getDiffEstimatedActualIndirectAsString();
	double diff = getDiffEstimatedActualIndirect();
	if (diff < 0) {
	    return HTMLUtils.getBadgeHTML(HTMLCSSDetails.DANGER, label);
	}
	return HTMLUtils.getBadgeHTML(HTMLCSSDetails.SUCCESS, label);
    }

    public double getActualTotalProject() {
	return grandTotalActualCostsDirect + grandTotalActualCostsIndirect;
    }

    public String getActualTotalProjectAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getActualTotalProject());
    }

    public double getCurrentTotalProject() {
	return grandTotalDelivery + grandTotalPayroll + grandTotalOtherExpenses
		+ grandTotalEquipmentExpenses;
    }

    @Transient
    public HTMLCSSDetails getCSSofOverspent() {
	if (getRemainingBudget() < 0) {
	    return HTMLCSSDetails.OVERSPENT;
	}
	return HTMLCSSDetails.SPENT;
    }

    public double getRemainingBudget() {
	return getPlannedTotalProject() - getCurrentTotalProject();
    }

    public String getRemainingBudgetAsString() {
	double budget = getRemainingBudget();
	String budgetStr = NumberFormatUtils.getCurrencyFormatter().format(budget);
	if (budget < 0) {
	    return budgetStr.replace("&#8369;", "&#8369;-").replace("(", "").replace(")", "");
	}
	return budgetStr;
    }

    public double getRemainingBudgetAsPercent() {
	return (getRemainingBudget() / getPlannedTotalProject()) * 100;
    }

    public String getRemainingBudgetAsPercentAsString() {
	return getPlannedTotalProject() == 0 ? ""
		: "(" + NumberFormatUtils.getQuantityFormatter().format(getRemainingBudgetAsPercent())
			+ "%)";
    }

    public double getCurrentTotalProjectAsPercent() {
	double currentTotal = getCurrentTotalProject();
	double plannedTotal = getPlannedTotalProject();
	return (currentTotal / plannedTotal) * 100;
    }

    public String getCurrentTotalProjectAsPercentAsString() {
	return getPlannedTotalProject() == 0 ? ""
		: "(" + NumberFormatUtils.getQuantityFormatter()
			.format(getCurrentTotalProjectAsPercent()) + "%)";
    }

    public String getPlannedTotalProjectAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getPlannedTotalProject());
    }

    public String getCurrentTotalProjectAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getCurrentTotalProject());
    }

    public double getGrandTotalDelivery() {
	return grandTotalDelivery;
    }

    public String getGrandTotalDeliveryAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(this.grandTotalDelivery);
    }

    public void setGrandTotalDelivery(double grandTotalDelivery) {
	this.grandTotalDelivery = grandTotalDelivery;
    }

    public double getGrandTotalPayroll() {
	return grandTotalPayroll;
    }

    public String getGrandTotalPayrollAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(this.grandTotalPayroll);
    }

    public void setGrandTotalPayroll(double grandTotalPayroll) {
	this.grandTotalPayroll = grandTotalPayroll;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof ProjectAux ? ((ProjectAux) obj).getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    public String getGrandTotalCostsDirectAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getGrandTotalCostsDirect());
    }

    public double getGrandTotalCostsDirect() {
	return grandTotalCostsDirect;
    }

    public void setGrandTotalCostsDirect(double grandTotalCostsDirect) {
	this.grandTotalCostsDirect = grandTotalCostsDirect;
    }

    public double getGrandTotalCostsIndirect() {
	return grandTotalCostsIndirect;
    }

    public String getGrandTotalCostsIndirectAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getGrandTotalCostsIndirect());
    }

    public void setGrandTotalCostsIndirect(double grandTotalCostsIndirect) {
	this.grandTotalCostsIndirect = grandTotalCostsIndirect;
    }

    public double getGrandTotalActualCostsDirect() {
	return grandTotalActualCostsDirect;
    }

    public String getGrandTotalActualCostsDirectAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getGrandTotalActualCostsDirect());
    }

    public void setGrandTotalActualCostsDirect(double grandTotalActualCostsDirect) {
	this.grandTotalActualCostsDirect = grandTotalActualCostsDirect;
    }

    public double getGrandTotalActualCostsIndirect() {
	return grandTotalActualCostsIndirect;
    }

    public String getGrandTotalActualCostsIndirectAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getGrandTotalActualCostsIndirect());
    }

    public void setGrandTotalActualCostsIndirect(double grandTotalActualCostsIndirect) {
	this.grandTotalActualCostsIndirect = grandTotalActualCostsIndirect;
    }

    public double getGrandTotalOtherExpenses() {
	return grandTotalOtherExpenses;
    }

    public String getGrandTotalOtherExpensesAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getGrandTotalOtherExpenses());
    }

    public void setGrandTotalOtherExpenses(double grandTotalOtherExpenses) {
	this.grandTotalOtherExpenses = grandTotalOtherExpenses;
    }

    public double getGrandTotalEquipmentExpenses() {
	return grandTotalEquipmentExpenses;
    }

    public String getGrandTotalEquipmentExpensesAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getGrandTotalEquipmentExpenses());
    }

    public void setGrandTotalEquipmentExpenses(double grandTotalEquipmentExpenses) {
	this.grandTotalEquipmentExpenses = grandTotalEquipmentExpenses;
    }

    @Override
    public String getName() {
	return this.project.getName();
    }

    @Override
    public String getObjectName() {
	return ConstantsRedis.OBJECT_AUX_PROJECT;
    }

}
