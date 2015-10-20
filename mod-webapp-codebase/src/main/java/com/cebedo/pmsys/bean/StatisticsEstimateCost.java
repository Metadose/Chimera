package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.enums.EstimateCostType;
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

    public StatisticsEstimateCost() {
	;
    }

    public StatisticsEstimateCost(List<EstimateCost> exp) {
	this.estimates = exp;
	for (EstimateCost est : this.estimates) {

	    double difference = est.getCost() - est.getActualCost();
	    if (est.getCostType() == EstimateCostType.DIRECT) {
		this.estimatesDirect.add(est);
		this.differencesDirect.put(est, difference);
	    } else {
		this.estimatesIndirect.add(est);
		this.differencesIndirect.put(est, difference);
	    }
	}
	initPlannedDirect();
	initPlannedIndirect();
	initActualDirect();
	initActualIndirect();
    }

    public ImmutableList<Entry<EstimateCost, Double>> getSortedDifferencesIndirect(Integer limit,
	    SortOrder order) {
	return sortDifferences(this.differencesIndirect, limit, order);
    }

    public ImmutableList<Entry<EstimateCost, Double>> getSortedDifferencesDirect(Integer limit,
	    SortOrder order) {
	return sortDifferences(this.differencesDirect, limit, order);
    }

    public ImmutableList<Entry<EstimateCost, Double>> sortDifferences(Map<EstimateCost, Double> toSort,
	    Integer limit, SortOrder order) {
	ArrayList<Entry<EstimateCost, Double>> entryList = Lists.newArrayList(toSort.entrySet());
	Collections.sort(entryList, new ComparatorMapEntry(order));
	if (limit != null) {
	    return FluentIterable.from(entryList).limit(limit).toList();
	}
	return FluentIterable.from(entryList).toList();
    }

    private void initActualIndirect() {
	EstimateCostType costType = EstimateCostType.INDIRECT;
	int subType = EstimateCostType.ACTUAL;
	initValues(costType, subType);
	double max = getMax();
	double min = getMin();
	this.maxActualIndirect = matchingList(max, costType, subType);
	this.minActualIndirect = matchingList(min, costType, subType);
	clear();
    }

    private void initPlannedIndirect() {
	EstimateCostType costType = EstimateCostType.INDIRECT;
	int subType = EstimateCostType.PLANNED;
	initValues(costType, subType);
	double max = getMax();
	double min = getMin();
	this.maxPlannedIndirect = matchingList(max, costType, subType);
	this.minPlannedIndirect = matchingList(min, costType, subType);
	clear();
    }

    private void initActualDirect() {
	EstimateCostType costType = EstimateCostType.DIRECT;
	int subType = EstimateCostType.ACTUAL;
	initValues(costType, subType);
	double max = getMax();
	double min = getMin();
	this.maxActualDirect = matchingList(max, costType, subType);
	this.minActualDirect = matchingList(min, costType, subType);
	clear();
    }

    private void initPlannedDirect() {
	EstimateCostType costType = EstimateCostType.DIRECT;
	int subType = EstimateCostType.PLANNED;
	initValues(costType, subType);
	double max = getMax();
	double min = getMin();
	this.maxPlannedDirect = matchingList(max, costType, subType);
	this.minPlannedDirect = matchingList(min, costType, subType);
	clear();
    }

    private List<EstimateCost> matchingList(double comparator, EstimateCostType costType, int subType) {
	List<EstimateCost> returnList = new ArrayList<EstimateCost>();
	for (EstimateCost expense : (costType == EstimateCostType.DIRECT ? this.estimatesDirect
		: this.estimatesIndirect)) {
	    double cost = subType == EstimateCostType.PLANNED ? expense.getCost()
		    : expense.getActualCost();
	    if (comparator == cost) {
		returnList.add(expense);
	    }
	}
	return returnList;
    }

    private void initValues(EstimateCostType costType, int subType) {
	for (EstimateCost expense : (costType == EstimateCostType.DIRECT ? this.estimatesDirect
		: this.estimatesIndirect)) {
	    if (subType == EstimateCostType.PLANNED) {
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
	return sortList(this.estimatesIndirect, EstimateCostType.ACTUAL, order, limit);
    }

    public ImmutableList<EstimateCost> getSortedPlannedIndirect(SortOrder order, Integer limit) {
	return sortList(this.estimatesIndirect, EstimateCostType.PLANNED, order, limit);
    }

    public ImmutableList<EstimateCost> getSortedActualDirect(SortOrder order, Integer limit) {
	return sortList(this.estimatesDirect, EstimateCostType.ACTUAL, order, limit);
    }

    public ImmutableList<EstimateCost> getSortedPlannedDirect(SortOrder order, Integer limit) {
	return sortList(this.estimatesDirect, EstimateCostType.PLANNED, order, limit);
    }

    private ImmutableList<EstimateCost> sortList(List<EstimateCost> estCosts, int subType,
	    SortOrder order, Integer limit) {
	Collections.sort(estCosts, new ComparatorEstimateCost(subType, order));
	return limit == null ? FluentIterable.from(estCosts).toList()
		: FluentIterable.from(estCosts).limit(limit).toList();
    }

}