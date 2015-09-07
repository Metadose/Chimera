package com.cebedo.pmsys.domain;

import java.util.Map;

import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class ProjectAux implements IDomainObject {

    private static final long serialVersionUID = 4250896962237506975L;

    private Company company;
    private Project project;

    /**
     * Grand totals of expenses.
     */
    private double grandTotalDelivery;
    private double grandTotalPayroll;

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
	return String.format(RegistryRedisKeys.KEY_PROJECT_AUX, this.company.getId(),
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
	return String.format(RegistryRedisKeys.KEY_PROJECT_AUX, company.getId(), project.getId());
    }

    public double getPlannedTotalProject() {
	return grandTotalCostsDirect + grandTotalCostsIndirect;
    }

    /**
     * TODO ESTIMATE-ACTUAL: for post-project analysis<br>
     * si OTHER EXPENSES, "current expenses". si actual di mabutang diri
     * 
     * @return
     */
    public double getCurrentTotalProject() {
	return grandTotalDelivery + grandTotalPayroll;
    }

    public double getRemainingBudget() {
	return getPlannedTotalProject() - getCurrentTotalProject();
    }

    public String getRemainingBudgetAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(getRemainingBudget());
    }

    public double getRemainingBudgetAsPercent() {
	return (getRemainingBudget() / getPlannedTotalProject()) * 100;
    }

    public String getRemainingBudgetAsPercentAsString() {
	return NumberFormatUtils.getQuantityFormatter().format(getRemainingBudgetAsPercent());
    }

    public double getCurrentTotalProjectAsPercent() {
	double currentTotal = getCurrentTotalProject();
	double plannedTotal = getPlannedTotalProject();
	return (currentTotal / plannedTotal) * 100;
    }

    public String getCurrentTotalProjectAsPercentAsString() {
	return NumberFormatUtils.getQuantityFormatter().format(getCurrentTotalProjectAsPercent());
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

    public String getGrandTotalCostsAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		getGrandTotalCostsDirect() + getGrandTotalCostsIndirect());
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

    public void setGrandTotalActualCostsDirect(double grandTotalActualCostsDirect) {
	this.grandTotalActualCostsDirect = grandTotalActualCostsDirect;
    }

    public double getGrandTotalActualCostsIndirect() {
	return grandTotalActualCostsIndirect;
    }

    public void setGrandTotalActualCostsIndirect(double grandTotalActualCostsIndirect) {
	this.grandTotalActualCostsIndirect = grandTotalActualCostsIndirect;
    }

}
