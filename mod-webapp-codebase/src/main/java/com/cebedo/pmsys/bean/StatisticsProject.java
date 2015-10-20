package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.domain.AbstractExpense;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.IExpense;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

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

    /**
     * Get max payrolls.
     * 
     * @return
     */
    public List<ProjectPayroll> getMaxPayrolls() {

	// Get the greatest value.
	addValuesPayroll();
	double comparator = getMax();
	clear();

	// Get all objects that are equal with the greatest value.
	List<ProjectPayroll> returnList = getMatchingPayrolls(comparator);
	return returnList;
    }

    public ImmutableList<ProjectPayroll> getAllSortedByCostPayrolls(SortOrder order) {
	return getLimitedSortedByCostPayrolls(null, order);
    }

    public ImmutableList<Delivery> getAllSortedByCostDeliveries(SortOrder order) {
	return getLimitedSortedByCostDeliveries(null, order);
    }

    public ImmutableList<EquipmentExpense> getAllSortedByCostEquipment(SortOrder order) {
	return getLimitedSortedByCostEquipment(null, order);
    }

    public ImmutableList<Expense> getAllSortedByCostOtherExpenses(SortOrder order) {
	return getLimitedSortedByCostOtherExpenses(null, order);
    }

    @SuppressWarnings("unchecked")
    public ImmutableList<Expense> getLimitedSortedByCostOtherExpenses(Integer limit, SortOrder order) {
	return (ImmutableList<Expense>) sortByCostAbstract(this.otherExpenses, limit, order);
    }

    @SuppressWarnings("unchecked")
    public ImmutableList<EquipmentExpense> getLimitedSortedByCostEquipment(Integer limit,
	    SortOrder order) {
	return (ImmutableList<EquipmentExpense>) sortByCostAbstract(this.equipment, limit, order);
    }

    @SuppressWarnings("unchecked")
    public ImmutableList<Delivery> getLimitedSortedByCostDeliveries(Integer limit, SortOrder order) {
	return (ImmutableList<Delivery>) sortByCostAbstract(this.deliveries, limit, order);
    }

    @SuppressWarnings("unchecked")
    public ImmutableList<ProjectPayroll> getLimitedSortedByCostPayrolls(Integer maxCount,
	    SortOrder order) {
	return (ImmutableList<ProjectPayroll>) sortByCostAbstract(this.payrolls, maxCount, order);
    }

    public ImmutableList<IExpense> getAllSortedByCostProject(SortOrder order) {
	return sortByCostInterface(collectExpenses(), null, order);
    }

    public ImmutableList<IExpense> getLimitedSortedByCostProject(Integer maxCount, SortOrder order) {
	return sortByCostInterface(collectExpenses(), maxCount, order);
    }

    private List<IExpense> collectExpenses() {
	List<IExpense> expenses = new ArrayList<IExpense>();
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
    private ImmutableList<IExpense> sortByCostInterface(List<IExpense> objList, Integer maxCount,
	    SortOrder order) {
	Collections.sort(objList, new ComparatorExpenseI(order));
	if (maxCount != null) {
	    return FluentIterable.from(objList).limit(maxCount).toList();
	}
	return FluentIterable.from(objList).toList();
    }

    /**
     * Sort and limit the given list.
     * 
     * @param objList
     * @param maxCount
     * @param order
     * @return
     */
    private ImmutableList<? extends Object> sortByCostAbstract(List<? extends AbstractExpense> objList,
	    Integer maxCount, SortOrder order) {
	Collections.sort(objList, new ComparatorExpenseA(order));
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
    public List<ProjectPayroll> getMinPayrolls() {

	// Get the greatest value.
	addValuesPayroll();
	double comparator = getMin();
	clear();

	// Get all objects that are equal with the greatest value.
	List<ProjectPayroll> returnList = getMatchingPayrolls(comparator);
	return returnList;
    }

    /**
     * Get objects match a specific value.
     * 
     * @param comparator
     * @return
     */
    private List<ProjectPayroll> getMatchingPayrolls(double comparator) {
	List<ProjectPayroll> max = new ArrayList<ProjectPayroll>();
	for (ProjectPayroll payroll : this.payrolls) {
	    PayrollResultComputation result = payroll.getPayrollComputationResult();
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
    public List<Delivery> getMinDeliveries() {

	// Get the greatest value.
	addValuesDelivery();
	double comparator = getMin();
	clear();

	// Get all objects that are equal with the greatest value.
	List<Delivery> returnList = getMatchingDeliveries(comparator);
	return returnList;
    }

    /**
     * Get max deliveries.
     * 
     * @return
     */
    public List<Delivery> getMaxDelivery() {

	// Get the greatest value.
	addValuesDelivery();
	double comparator = getMax();
	clear();

	// Get all objects that are equal with the greatest value.
	List<Delivery> returnList = getMatchingDeliveries(comparator);
	return returnList;
    }

    /**
     * Get deliveries matching a value.
     * 
     * @param comparator
     * @return
     */
    private List<Delivery> getMatchingDeliveries(double comparator) {
	// Get all objects that are equal with the greatest value.
	List<Delivery> returnList = new ArrayList<Delivery>();
	// Get all objects that are equal with the greatest value.
	for (Delivery obj : this.deliveries) {
	    double total = obj.getGrandTotalOfMaterials();
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
    public List<EquipmentExpense> getMinEquipment() {

	// Get the greatest value.
	addValuesEquipment();
	double comparator = getMin();
	clear();

	// Get all objects that are equal with the greatest value.
	List<EquipmentExpense> returnList = getMatchingEquipment(comparator);
	return returnList;
    }

    /**
     * Get max equipment.
     * 
     * @return
     */
    public List<EquipmentExpense> getMaxEquipment() {

	// Get the greatest value.
	addValuesEquipment();
	double comparator = getMax();
	clear();

	// Get all objects that are equal with the greatest value.
	List<EquipmentExpense> returnList = getMatchingEquipment(comparator);
	return returnList;
    }

    private List<EquipmentExpense> getMatchingEquipment(double comparator) {
	// Get all objects that are equal with the greatest value.
	List<EquipmentExpense> returnList = new ArrayList<EquipmentExpense>();

	// Get all objects that are equal with the greatest value.
	for (EquipmentExpense obj : this.equipment) {
	    double total = obj.getCost();
	    if (total == comparator) {
		returnList.add(obj);
	    }
	}
	return returnList;
    }

    public List<Expense> getMinOtherExpenses() {
	// Get the greatest value.
	addValuesOtherExpenses();
	double comparator = getMin();
	clear();

	// Get all objects that are equal with the greatest value.
	List<Expense> returnList = getMatchingOtherExpenses(comparator);
	return returnList;
    }

    /**
     * Get max of other expenses.
     * 
     * @return
     */
    public List<Expense> getMaxOtherExpenses() {
	// Get the greatest value.
	addValuesOtherExpenses();
	double comparator = getMax();
	clear();

	// Get all objects that are equal with the greatest value.
	List<Expense> returnList = getMatchingOtherExpenses(comparator);
	return returnList;
    }

    private List<Expense> getMatchingOtherExpenses(double comparator) {
	// Get all objects that are equal with the greatest value.
	List<Expense> returnList = new ArrayList<Expense>();

	// Get all objects that are equal with the greatest value.
	for (Expense obj : this.otherExpenses) {
	    double total = obj.getCost();
	    if (total == comparator) {
		returnList.add(obj);
	    }
	}
	return returnList;
    }

}
