package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class Expense extends AbstractExpense implements IDomainObject, IExpense {

    private static final long serialVersionUID = -7013450034228364135L;

    // Keys.
    protected Company company;
    protected Project project;
    protected UUID uuid;

    // Attributes.
    private String name;
    private double cost;
    private Staff staff;
    private Date date;

    // Bean-backed form.
    private long staffID;

    public Expense() {
	;
    }

    public Expense(Project proj) {
	setProject(proj);
	setCompany(proj.getCompany());
    }

    @Override
    public String getKey() {
	return String.format(RegistryRedisKeys.KEY_EXPENSE, this.company.getId(), this.project.getId(),
		this.uuid);
    }

    public static String constructPattern(Project proj) {
	return String.format(RegistryRedisKeys.KEY_EXPENSE, proj.getCompany().getId(), proj.getId(),
		"*");
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Expense ? ((Expense) obj).getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
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

    public Staff getStaff() {
	return staff;
    }

    public void setStaff(Staff staff) {
	this.staff = staff;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public long getStaffID() {
	return staffID;
    }

    public void setStaffID(long staffID) {
	this.staffID = staffID;
    }

    @Override
    public String getObjectName() {
	return ConstantsRedis.DISPLAY_OTHER_EXPENSE;
    }

    @Override
    public String toString() {
	return String.format("[%s = %s]", getName(), getCost());
    }

}
