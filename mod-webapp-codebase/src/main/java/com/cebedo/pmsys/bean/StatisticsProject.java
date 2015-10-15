package com.cebedo.pmsys.bean;

import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.model.Staff;

public class StatisticsProject extends SummaryStatistics {

    private static final long serialVersionUID = 1330741496424321113L;

    private List<ProjectPayroll> payrolls;
    private List<Delivery> deliveries;
    private List<EquipmentExpense> equipment;
    private List<Expense> otherExpenses;

    public StatisticsProject() {
	;
    }

    public StatisticsProject(List<ProjectPayroll> payrolls, List<Delivery> deliveries,
	    List<EquipmentExpense> equipmentExpenses, List<Expense> otherExpenses) {
	this.payrolls = payrolls;
	this.deliveries = deliveries;
	this.equipment = equipmentExpenses;
	this.otherExpenses = otherExpenses;
    }

    private void addValuesPayroll() {
	for (ProjectPayroll payroll : this.payrolls) {
	    PayrollResultComputation result = payroll.getPayrollComputationResult();
	    if (result != null) {
		addValue(result.getOverallTotalOfStaff());
	    }
	}
    }

    /**
     * Get the mean then clear the list.
     * 
     * @param payrolls
     * @return
     */
    public double getMeanPayroll() {
	addValuesPayroll();
	double mean = getMean();
	clear();
	return mean;
    }

    public double getMeanDelivery() {
	addValuesDelivery();
	double mean = getMean();
	clear();
	return mean;
    }

    private void addValuesDelivery() {
	for (Delivery obj : this.deliveries) {
	    addValue(obj.getGrandTotalOfMaterials());
	}
    }

    public double getMeanEquipment() {
	addValuesEquipment();
	double mean = getMean();
	clear();
	return mean;
    }

    private void addValuesEquipment() {
	for (EquipmentExpense obj : this.equipment) {
	    addValue(obj.getCost());
	}
    }

    public double getMeanOtherExpenses() {
	addValuesOtherExpenses();
	double mean = getMean();
	clear();
	return mean;
    }

    private void addValuesOtherExpenses() {
	for (Expense obj : this.otherExpenses) {
	    addValue(obj.getCost());
	}
    }

    public double getMeanProject() {
	addValuesProject();
	double mean = getMean();
	clear();
	return mean;
    }

    private void addValuesProject() {
	addValuesPayroll();
	addValuesDelivery();
	addValuesEquipment();
	addValuesOtherExpenses();
    }

    public double getSumPayroll() {
	addValuesPayroll();
	double sum = getSum();
	clear();
	return sum;
    }

    public double getSumDelivery() {
	addValuesDelivery();
	double sum = getSum();
	clear();
	return sum;
    }

    public double getSumEquipment() {
	addValuesEquipment();
	double sum = getSum();
	clear();
	return sum;
    }

    public double getSumOtherExpenses() {
	addValuesOtherExpenses();
	double sum = getSum();
	clear();
	return sum;
    }

    @Deprecated
    public double getMeanWageAndClear(Set<Staff> assignedStaff) {
	addValuesWage(assignedStaff);
	double mean = getMean();
	clear();
	return mean;
    }

    @Deprecated
    public void addValuesWage(Set<Staff> assignedStaff) {
	for (Staff staff : assignedStaff) {
	    addValue(staff.getWage());
	}
    }

    @Deprecated
    public double getMeanSumAndClear(Set<Staff> assignedStaff) {
	addValuesWage(assignedStaff);
	double sum = getSum();
	clear();
	return sum;
    }

}
