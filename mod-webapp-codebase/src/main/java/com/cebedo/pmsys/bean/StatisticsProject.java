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

    public void addValuesPayroll(final List<ProjectPayroll> payrolls) {
	for (ProjectPayroll payroll : payrolls) {
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
    public double getMeanAndClearPayroll(final List<ProjectPayroll> payrolls) {
	addValuesPayroll(payrolls);
	double mean = getMean();
	clear();
	return mean;
    }

    public double getMeanAndClearDelivery(List<Delivery> deliveries) {
	addValuesDelivery(deliveries);
	double mean = getMean();
	clear();
	return mean;
    }

    public void addValuesDelivery(List<Delivery> deliveries) {
	for (Delivery obj : deliveries) {
	    addValue(obj.getGrandTotalOfMaterials());
	}
    }

    public double getMeanAndClearEquipment(List<EquipmentExpense> equipmentExpenses) {
	addValuesEquipment(equipmentExpenses);
	double mean = getMean();
	clear();
	return mean;
    }

    public void addValuesEquipment(List<EquipmentExpense> objs) {
	for (EquipmentExpense obj : objs) {
	    addValue(obj.getCost());
	}
    }

    public double getMeanAndClearOtherExpenses(List<Expense> otherExpenses) {
	addValuesOtherExpenses(otherExpenses);
	double mean = getMean();
	clear();
	return mean;
    }

    public void addValuesOtherExpenses(List<Expense> objs) {
	for (Expense obj : objs) {
	    addValue(obj.getCost());
	}
    }

    public double getMeanAndClearProject(List<ProjectPayroll> payrolls, List<Delivery> deliveries,
	    List<EquipmentExpense> equipmentExpenses, List<Expense> otherExpenses) {
	addValuesPayroll(payrolls);
	addValuesDelivery(deliveries);
	addValuesEquipment(equipmentExpenses);
	addValuesOtherExpenses(otherExpenses);
	double mean = getMean();
	clear();
	return mean;
    }

    public double getSumAndClearPayroll(List<ProjectPayroll> payrolls) {
	addValuesPayroll(payrolls);
	double sum = getSum();
	clear();
	return sum;
    }

    public double getSumAndClearDelivery(List<Delivery> deliveries) {
	addValuesDelivery(deliveries);
	double sum = getSum();
	clear();
	return sum;
    }

    public double getSumAndClearEquipment(List<EquipmentExpense> equipmentExpenses) {
	addValuesEquipment(equipmentExpenses);
	double sum = getSum();
	clear();
	return sum;
    }

    public double getSumAndClearOtherExpenses(List<Expense> otherExpenses) {
	addValuesOtherExpenses(otherExpenses);
	double sum = getSum();
	clear();
	return sum;
    }

    public double getMeanWageAndClear(Set<Staff> assignedStaff) {
	addValuesWage(assignedStaff);
	double mean = getMean();
	clear();
	return mean;
    }

    public void addValuesWage(Set<Staff> assignedStaff) {
	for (Staff staff : assignedStaff) {
	    addValue(staff.getWage());
	}
    }

    public double getMeanSumAndClear(Set<Staff> assignedStaff) {
	addValuesWage(assignedStaff);
	double sum = getSum();
	clear();
	return sum;
    }

}
