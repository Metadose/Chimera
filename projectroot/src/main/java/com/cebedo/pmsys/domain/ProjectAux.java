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

    @Override
    public boolean equals(Object obj) {
	return obj instanceof ProjectAux ? ((ProjectAux) obj).getKey().equals(
		getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
