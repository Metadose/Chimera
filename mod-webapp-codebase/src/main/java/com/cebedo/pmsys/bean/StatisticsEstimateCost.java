package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.enums.TypeEstimateCost;
import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class StatisticsEstimateCost extends SummaryStatistics {

    private static final long serialVersionUID = -8828578865525649789L;

    private List<EstimateCost> estimates = new ArrayList<EstimateCost>();
    private List<EstimateCost> estimatesDirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> estimatesIndirect = new ArrayList<EstimateCost>();

    private List<EstimateCost> maxPlannedDirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> minPlannedDirect = new ArrayList<EstimateCost>();

    private List<EstimateCost> maxActualDirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> minActualDirect = new ArrayList<EstimateCost>();

    private List<EstimateCost> maxPlannedIndirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> minPlannedIndirect = new ArrayList<EstimateCost>();

    private List<EstimateCost> maxActualIndirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> minActualIndirect = new ArrayList<EstimateCost>();

    private Map<EstimateCost, Double> differencesDirect = new HashMap<EstimateCost, Double>();
    private Map<EstimateCost, Double> differencesIndirect = new HashMap<EstimateCost, Double>();
    private Map<EstimateCost, Double> differencesOverall = new HashMap<EstimateCost, Double>();

    private Map<EstimateCost, Double> absDiffDirect = new HashMap<EstimateCost, Double>();
    private Map<EstimateCost, Double> absDiffIndirect = new HashMap<EstimateCost, Double>();
    private Map<EstimateCost, Double> absDiffOverall = new HashMap<EstimateCost, Double>();

    private double meanPlannedDirect;
    private double meanPlannedIndirect;
    private double meanPlannedOverall;
    private double meanActualDirect;
    private double meanActualIndirect;
    private double meanActualOverall;

    public StatisticsEstimateCost() {
	;
    }

    public StatisticsEstimateCost(List<EstimateCost> exp) {
	this.estimates = exp;
	for (EstimateCost est : this.estimates) {

	    double difference = est.getCost() - est.getActualCost();
	    if (est.getCostType() == TypeEstimateCost.DIRECT) {
		this.estimatesDirect.add(est);
		this.differencesDirect.put(est, difference);
		this.absDiffDirect.put(est, Math.abs(difference));
	    } else {
		this.estimatesIndirect.add(est);
		this.differencesIndirect.put(est, difference);
		this.absDiffIndirect.put(est, Math.abs(difference));
	    }
	    this.differencesOverall.put(est, difference);
	    this.absDiffOverall.put(est, Math.abs(difference));
	}
	initPlannedDirect();
	initPlannedIndirect();
	initPlannedOverall();
	initActualDirect();
	initActualIndirect();
	initActualOverall();
    }

    private void initActualOverall() {
	initValues(TypeEstimateCost.SUB_TYPE_ACTUAL);
	this.meanActualOverall = getMean();
	clear();
    }

    /**
     * Get the difference between the planned and the actual, then sort the
     * differences.
     * 
     * @param limit
     * @param order
     * @return
     */
    public ImmutableList<Entry<EstimateCost, Double>> getSortedDifferencesIndirect(Integer limit,
	    SortOrder order) {
	return sortDifferences(this.differencesIndirect, limit, order);
    }

    /**
     * Get the difference between the planned and the actual, then sort the
     * differences.
     * 
     * @param limit
     * @param order
     * @return
     */
    public ImmutableList<Entry<EstimateCost, Double>> getSortedAbsDiffOverall(Integer limit,
	    SortOrder order) {
	return sortDifferences(this.absDiffOverall, limit, order);
    }

    /**
     * Get the difference between the planned and the actual, then sort the
     * differences.
     * 
     * @param limit
     * @param order
     * @return
     */
    public ImmutableList<Entry<EstimateCost, Double>> getSortedAbsDiffIndirect(Integer limit,
	    SortOrder order) {
	return sortDifferences(this.absDiffIndirect, limit, order);
    }

    /**
     * Get the difference between the planned and the actual, then sort the
     * differences.
     * 
     * @param limit
     * @param order
     * @return
     */
    public ImmutableList<Entry<EstimateCost, Double>> getSortedAbsDiffDirect(Integer limit,
	    SortOrder order) {
	return sortDifferences(this.absDiffDirect, limit, order);
    }

    /**
     * Get the difference between the planned and the actual, then sort the
     * differences.
     * 
     * @param limit
     * @param order
     * @return
     */
    public ImmutableList<Entry<EstimateCost, Double>> getSortedDifferencesDirect(Integer limit,
	    SortOrder order) {
	return sortDifferences(this.differencesDirect, limit, order);
    }

    /**
     * Get the difference between the planned and the actual, then sort the
     * differences.
     * 
     * @param limit
     * @param order
     * @return
     */
    public ImmutableList<Entry<EstimateCost, Double>> getSortedDifferencesOverall(Integer limit,
	    SortOrder order) {
	return sortDifferences(this.differencesOverall, limit, order);
    }

    /**
     * Get the difference between the planned and the actual, then sort the
     * differences.
     * 
     * @param toSort
     * @param limit
     * @param order
     * @return
     */
    public ImmutableList<Entry<EstimateCost, Double>> sortDifferences(Map<EstimateCost, Double> toSort,
	    Integer limit, SortOrder order) {
	ArrayList<Entry<EstimateCost, Double>> entryList = Lists.newArrayList(toSort.entrySet());
	Collections.sort(entryList, new OrderingObjectNumberMap(order));
	if (limit != null) {
	    return FluentIterable.from(entryList).limit(limit).toList();
	}
	return FluentIterable.from(entryList).toList();
    }

    /**
     * Initialize data.
     */
    private void initActualIndirect() {
	TypeEstimateCost costType = TypeEstimateCost.INDIRECT;
	int subType = TypeEstimateCost.SUB_TYPE_ACTUAL;
	initValues(costType, subType);
	double max = getMax();
	double min = getMin();
	this.meanActualIndirect = getMean();
	this.maxActualIndirect = matchingList(max, costType, subType);
	this.minActualIndirect = matchingList(min, costType, subType);
	clear();
    }

    /**
     * Initialize data.
     */
    private void initPlannedIndirect() {
	TypeEstimateCost costType = TypeEstimateCost.INDIRECT;
	int subType = TypeEstimateCost.SUB_TYPE_PLANNED;
	initValues(costType, subType);
	double max = getMax();
	double min = getMin();
	this.meanPlannedIndirect = getMean();
	this.maxPlannedIndirect = matchingList(max, costType, subType);
	this.minPlannedIndirect = matchingList(min, costType, subType);
	clear();
    }

    /**
     * Initialize data.
     */
    private void initActualDirect() {
	TypeEstimateCost costType = TypeEstimateCost.DIRECT;
	int subType = TypeEstimateCost.SUB_TYPE_ACTUAL;
	initValues(costType, subType);
	double max = getMax();
	double min = getMin();
	this.meanActualDirect = getMean();
	this.maxActualDirect = matchingList(max, costType, subType);
	this.minActualDirect = matchingList(min, costType, subType);
	clear();
    }

    /**
     * Initialize data.
     */
    private void initPlannedOverall() {
	initValues(TypeEstimateCost.SUB_TYPE_PLANNED);
	this.meanPlannedOverall = getMean();
	clear();
    }

    /**
     * Initialize data.
     */
    private void initPlannedDirect() {
	TypeEstimateCost costType = TypeEstimateCost.DIRECT;
	int subType = TypeEstimateCost.SUB_TYPE_PLANNED;
	initValues(costType, subType);
	double max = getMax();
	double min = getMin();
	this.meanPlannedDirect = getMean();
	this.maxPlannedDirect = matchingList(max, costType, subType);
	this.minPlannedDirect = matchingList(min, costType, subType);
	clear();
    }

    /**
     * Get the list of estimated cost by matching the arguments.
     * 
     * @param comparator
     * @param costType
     * @param subType
     * @return
     */
    private List<EstimateCost> matchingList(double comparator, TypeEstimateCost costType, int subType) {
	List<EstimateCost> returnList = new ArrayList<EstimateCost>();
	for (EstimateCost expense : (costType == TypeEstimateCost.DIRECT ? this.estimatesDirect
		: this.estimatesIndirect)) {
	    double cost = subType == TypeEstimateCost.SUB_TYPE_PLANNED ? expense.getCost()
		    : expense.getActualCost();
	    if (comparator == cost) {
		returnList.add(expense);
	    }
	}
	return returnList;
    }

    private void initValues(int subType) {
	initValues(null, subType);
    }

    /**
     * Initialize values.
     * 
     * @param costType
     * @param subType
     */
    private void initValues(TypeEstimateCost costType, int subType) {
	for (EstimateCost expense : costType == null ? this.estimates
		: (costType == TypeEstimateCost.DIRECT ? this.estimatesDirect
			: this.estimatesIndirect)) {
	    if (subType == TypeEstimateCost.SUB_TYPE_PLANNED) {
		addValue(expense.getCost());
	    } else {
		addValue(expense.getActualCost());
	    }
	}
    }

    public List<EstimateCost> getMaxPlannedDirect() {
	return maxPlannedDirect;
    }

    public List<EstimateCost> getMinPlannedDirect() {
	return minPlannedDirect;
    }

    public List<EstimateCost> getMaxActualDirect() {
	return maxActualDirect;
    }

    public List<EstimateCost> getMinActualDirect() {
	return minActualDirect;
    }

    public List<EstimateCost> getMaxPlannedIndirect() {
	return maxPlannedIndirect;
    }

    public List<EstimateCost> getMinPlannedIndirect() {
	return minPlannedIndirect;
    }

    public List<EstimateCost> getMaxActualIndirect() {
	return maxActualIndirect;
    }

    public List<EstimateCost> getMinActualIndirect() {
	return minActualIndirect;
    }

    public ImmutableList<EstimateCost> getSortedActualIndirect(SortOrder order, Integer limit) {
	return sortList(this.estimatesIndirect, TypeEstimateCost.SUB_TYPE_ACTUAL, order, limit);
    }

    public ImmutableList<EstimateCost> getSortedPlannedIndirect(SortOrder order, Integer limit) {
	return sortList(this.estimatesIndirect, TypeEstimateCost.SUB_TYPE_PLANNED, order, limit);
    }

    public ImmutableList<EstimateCost> getSortedActualDirect(SortOrder order, Integer limit) {
	return sortList(this.estimatesDirect, TypeEstimateCost.SUB_TYPE_ACTUAL, order, limit);
    }

    public ImmutableList<EstimateCost> getSortedPlannedDirect(SortOrder order, Integer limit) {
	return sortList(this.estimatesDirect, TypeEstimateCost.SUB_TYPE_PLANNED, order, limit);
    }

    private ImmutableList<EstimateCost> sortList(List<EstimateCost> estCosts, int subType,
	    SortOrder order, Integer limit) {
	Collections.sort(estCosts, new OrderingEstimateCost(subType, order));
	return limit == null ? FluentIterable.from(estCosts).toList()
		: FluentIterable.from(estCosts).limit(limit).toList();
    }

    public double getMeanPlannedDirect() {
	return meanPlannedDirect;
    }

    public double getMeanPlannedIndirect() {
	return meanPlannedIndirect;
    }

    public double getMeanPlannedOverall() {
	return meanPlannedOverall;
    }

    public double getMeanActualDirect() {
	return meanActualDirect;
    }

    public double getMeanActualIndirect() {
	return meanActualIndirect;
    }

    public double getMeanActualOverall() {
	return meanActualOverall;
    }

}