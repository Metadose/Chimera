package com.cebedo.pmsys.domain;

import java.util.Map;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
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

    /**
     * Estimation.
     */
    private double totalCement40kg;
    private double totalCement50kg;
    private double totalSand;
    private double totalGravel;

    private Unit unitCement40kg;
    private Unit unitCement50kg;
    private Unit unitSand;
    private Unit unitGravel;

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

    public boolean unitsAreSet() {

	// If any of the following are null,
	// return false.
	if (getUnitCement40kg() == null || getUnitCement50kg() == null
		|| getUnitSand() == null || getUnitGravel() == null) {
	    return false;
	}
	return true;
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
	return String.format(RedisKeyRegistry.KEY_PROJECT_AUX,
		this.company.getId(), this.project.getId());
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
	return String.format(RedisKeyRegistry.KEY_PROJECT_AUX, company.getId(),
		project.getId());
    }

    public double getGrandTotalDelivery() {
	return grandTotalDelivery;
    }

    public String getGrandTotalDeliveryAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		this.grandTotalDelivery);
    }

    public void setGrandTotalDelivery(double grandTotalDelivery) {
	this.grandTotalDelivery = grandTotalDelivery;
    }

    public double getGrandTotalPayroll() {
	return grandTotalPayroll;
    }

    public String getGrandTotalPayrollAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		this.grandTotalPayroll);
    }

    public void setGrandTotalPayroll(double grandTotalPayroll) {
	this.grandTotalPayroll = grandTotalPayroll;
    }

    public double getTotalCement40kg() {
	return totalCement40kg;
    }

    public void setTotalCement40kg(double totalCement40kg) {
	this.totalCement40kg = totalCement40kg;
    }

    public double getTotalCement50kg() {
	return totalCement50kg;
    }

    public void setTotalCement50kg(double totalCement50kg) {
	this.totalCement50kg = totalCement50kg;
    }

    public double getTotalSand() {
	return totalSand;
    }

    public void setTotalSand(double totalSand) {
	this.totalSand = totalSand;
    }

    public double getTotalGravel() {
	return totalGravel;
    }

    public void setTotalGravel(double totalGravel) {
	this.totalGravel = totalGravel;
    }

    public Unit getUnitCement40kg() {
	return unitCement40kg;
    }

    public void setUnitCement40kg(Unit unitCement40kg) {
	this.unitCement40kg = unitCement40kg;
    }

    public Unit getUnitCement50kg() {
	return unitCement50kg;
    }

    public void setUnitCement50kg(Unit unitCement50kg) {
	this.unitCement50kg = unitCement50kg;
    }

    public Unit getUnitSand() {
	return unitSand;
    }

    public void setUnitSand(Unit unitSand) {
	this.unitSand = unitSand;
    }

    public Unit getUnitGravel() {
	return unitGravel;
    }

    public void setUnitGravel(Unit unitGravel) {
	this.unitGravel = unitGravel;
    }

}
