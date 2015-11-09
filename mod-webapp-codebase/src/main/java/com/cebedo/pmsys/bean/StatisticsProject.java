package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.base.IObjectExpense;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

public class StatisticsProject extends SummaryStatistics {

    public static final int DESCRIPTIVE_MAX = 0;
    public static final int DESCRIPTIVE_MIN = 1;
    public static final int DESCRIPTIVE_MEAN = 2;

    private static final long serialVersionUID = 1330741496424321113L;

    private List<IObjectExpense> payrolls;
    private List<IObjectExpense> deliveries;
    private List<IObjectExpense> equipment;
    private List<IObjectExpense> otherExpenses;

    public StatisticsProject() {
	;
    }

    public StatisticsProject(List<IObjectExpense> payrolls, List<IObjectExpense> deliveries,
	    List<IObjectExpense> equipmentExpenses, List<IObjectExpense> otherExpenses) {
	this.payrolls = payrolls;
	this.deliveries = deliveries;
	this.equipment = equipmentExpenses;
	this.otherExpenses = otherExpenses;
    }

    private void addValuesPayroll() {
	for (IObjectExpense payroll : this.payrolls) {
	    PayrollResultComputation result = ((ProjectPayroll) payroll).getPayrollComputationResult();
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
	for (IObjectExpense obj : this.deliveries) {
	    addValue(obj.getCost());
	}
    }

    public double getMeanEquipment() {
	addValuesEquipment();
	double mean = getMean();
	clear();
	return mean;
    }

    private void addValuesEquipment() {
	for (IObjectExpense obj : this.equipment) {
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
	for (IObjectExpense obj : this.otherExpenses) {
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

    /**
     * Get max payrolls.
     * 
     * @return
     */
    // public List<ProjectPayroll> getMaxPayrolls() {
    public List<IObjectExpense> getMaxPayrolls() {

	// Get the greatest value.
	addValuesPayroll();
	double comparator = getMax();
	clear();

	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = getMatchingPayrolls(comparator);
	return returnList;
    }

    public ImmutableList<IObjectExpense> getAllSortedByCostPayrolls(SortOrder order) {
	return getLimitedSortedByCostPayrolls(null, order);
    }

    public ImmutableList<IObjectExpense> getAllSortedByCostDeliveries(SortOrder order) {
	return getLimitedSortedByCostDeliveries(null, order);
    }

    public ImmutableList<IObjectExpense> getAllSortedByCostEquipment(SortOrder order) {
	return getLimitedSortedByCostEquipment(null, order);
    }

    public ImmutableList<IObjectExpense> getAllSortedByCostOtherExpenses(SortOrder order) {
	return getLimitedSortedByCostOtherExpenses(null, order);
    }

    public ImmutableList<IObjectExpense> getLimitedSortedByCostOtherExpenses(Integer limit, SortOrder order) {
	return sortByCostInterface(this.otherExpenses, limit, order);
    }

    public ImmutableList<IObjectExpense> getLimitedSortedByCostEquipment(Integer limit, SortOrder order) {
	return sortByCostInterface(this.equipment, limit, order);
    }

    public ImmutableList<IObjectExpense> getLimitedSortedByCostDeliveries(Integer limit, SortOrder order) {
	return sortByCostInterface(this.deliveries, limit, order);
    }

    public ImmutableList<IObjectExpense> getLimitedSortedByCostPayrolls(Integer maxCount, SortOrder order) {
	return sortByCostInterface(this.payrolls, maxCount, order);
    }

    public ImmutableList<IObjectExpense> getAllSortedByCostProject(SortOrder order) {
	return sortByCostInterface(collectExpenses(), null, order);
    }

    public ImmutableList<IObjectExpense> getLimitedSortedByCostProject(Integer maxCount, SortOrder order) {
	return sortByCostInterface(collectExpenses(), maxCount, order);
    }

    private List<IObjectExpense> collectExpenses() {
	List<IObjectExpense> expenses = new ArrayList<IObjectExpense>();
	expenses.addAll(this.deliveries);
	expenses.addAll(this.equipment);
	expenses.addAll(this.otherExpenses);
	expenses.addAll(this.payrolls);
	return expenses;
    }

    /**
     * Sort and limit the given list.
     * 
     * @param objList
     * @param maxCount
     * @param order
     * @return
     */
    private ImmutableList<IObjectExpense> sortByCostInterface(List<IObjectExpense> objList, Integer maxCount,
	    SortOrder order) {
	Collections.sort(objList, new OrderingIObjectExpense(order));
	if (maxCount != null) {
	    return FluentIterable.from(objList).limit(maxCount).toList();
	}
	return FluentIterable.from(objList).toList();
    }

    /**
     * Get min payrolls.
     * 
     * @return
     */
    public List<IObjectExpense> getMinPayrolls() {

	// Get the greatest value.
	addValuesPayroll();
	double comparator = getMin();
	clear();

	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = getMatchingPayrolls(comparator);
	return returnList;
    }

    /**
     * Get objects match a specific value.
     * 
     * @param comparator
     * @return
     */
    private List<IObjectExpense> getMatchingPayrolls(double comparator) {
	List<IObjectExpense> max = new ArrayList<IObjectExpense>();
	for (IObjectExpense payroll : this.payrolls) {
	    PayrollResultComputation result = ((ProjectPayroll) payroll).getPayrollComputationResult();
	    if (result != null) {
		double total = result.getOverallTotalOfStaff();
		if (total == comparator) {
		    max.add(payroll);
		}
	    }
	}
	return max;
    }

    /**
     * Get min deliveries.
     * 
     * @return
     */
    public List<IObjectExpense> getMinDeliveries() {

	// Get the greatest value.
	addValuesDelivery();
	double comparator = getMin();
	clear();

	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = getMatchingDeliveries(comparator);
	return returnList;
    }

    /**
     * Get max deliveries.
     * 
     * @return
     */
    public List<IObjectExpense> getMaxDelivery() {

	// Get the greatest value.
	addValuesDelivery();
	double comparator = getMax();
	clear();

	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = getMatchingDeliveries(comparator);
	return returnList;
    }

    /**
     * Get deliveries matching a value.
     * 
     * @param comparator
     * @return
     */
    private List<IObjectExpense> getMatchingDeliveries(double comparator) {
	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = new ArrayList<IObjectExpense>();
	// Get all objects that are equal with the greatest value.
	for (IObjectExpense obj : this.deliveries) {
	    double total = obj.getCost();
	    if (total == comparator) {
		returnList.add(obj);
	    }
	}
	return returnList;
    }

    /**
     * Get min equipment.
     * 
     * @return
     */
    public List<IObjectExpense> getMinEquipment() {

	// Get the greatest value.
	addValuesEquipment();
	double comparator = getMin();
	clear();

	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = getMatchingEquipment(comparator);
	return returnList;
    }

    /**
     * Get max equipment.
     * 
     * @return
     */
    public List<IObjectExpense> getMaxEquipment() {

	// Get the greatest value.
	addValuesEquipment();
	double comparator = getMax();
	clear();

	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = getMatchingEquipment(comparator);
	return returnList;
    }

    private List<IObjectExpense> getMatchingEquipment(double comparator) {
	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = new ArrayList<IObjectExpense>();

	// Get all objects that are equal with the greatest value.
	for (IObjectExpense obj : this.equipment) {
	    double total = obj.getCost();
	    if (total == comparator) {
		returnList.add(obj);
	    }
	}
	return returnList;
    }

    public List<IObjectExpense> getMinOtherExpenses() {
	// Get the greatest value.
	addValuesOtherExpenses();
	double comparator = getMin();
	clear();

	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = getMatchingOtherExpenses(comparator);
	return returnList;
    }

    /**
     * Get max of other expenses.
     * 
     * @return
     */
    public List<IObjectExpense> getMaxOtherExpenses() {
	// Get the greatest value.
	addValuesOtherExpenses();
	double comparator = getMax();
	clear();

	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = getMatchingOtherExpenses(comparator);
	return returnList;
    }

    private List<IObjectExpense> getMatchingOtherExpenses(double comparator) {
	// Get all objects that are equal with the greatest value.
	List<IObjectExpense> returnList = new ArrayList<IObjectExpense>();

	// Get all objects that are equal with the greatest value.
	for (IObjectExpense obj : this.otherExpenses) {
	    double total = obj.getCost();
	    if (total == comparator) {
		returnList.add(obj);
	    }
	}
	return returnList;
    }

}
