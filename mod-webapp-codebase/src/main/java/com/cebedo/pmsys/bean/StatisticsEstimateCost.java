package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.enums.EstimateCostType;

public class StatisticsEstimateCost extends SummaryStatistics {

    private static final long serialVersionUID = -8828578865525649789L;
    private List<EstimateCost> estimates = new ArrayList<EstimateCost>();

    private List<EstimateCost> maxPlannedDirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> minPlannedDirect = new ArrayList<EstimateCost>();

    private List<EstimateCost> maxActualDirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> minActualDirect = new ArrayList<EstimateCost>();

    private List<EstimateCost> maxPlannedIndirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> minPlannedIndirect = new ArrayList<EstimateCost>();

    private List<EstimateCost> maxActualIndirect = new ArrayList<EstimateCost>();
    private List<EstimateCost> minActualIndirect = new ArrayList<EstimateCost>();

    public StatisticsEstimateCost() {
	;
    }

    public StatisticsEstimateCost(List<EstimateCost> exp) {
	this.estimates = exp;
	initPlannedDirect();
	initActualDirect();
	initPlannedIndirect();
	initActualIndirect();
    }

    private void initActualIndirect() {
	EstimateCostType costType = EstimateCostType.INDIRECT;
	int subType = EstimateCostType.ACTUAL;
	double max = getMax();
	double min = getMin();

	initValues(costType, subType);
	this.maxActualIndirect = getMatchingList(max, costType, subType);
	this.minActualIndirect = getMatchingList(min, costType, subType);

	clear();
    }

    private void initPlannedIndirect() {
	EstimateCostType costType = EstimateCostType.INDIRECT;
	int subType = EstimateCostType.PLANNED;
	double max = getMax();
	double min = getMin();

	initValues(costType, subType);
	this.maxPlannedIndirect = getMatchingList(max, costType, subType);
	this.minPlannedIndirect = getMatchingList(min, costType, subType);

	clear();
    }

    private void initActualDirect() {
	EstimateCostType costType = EstimateCostType.DIRECT;
	int subType = EstimateCostType.ACTUAL;
	double max = getMax();
	double min = getMin();

	initValues(costType, subType);
	this.maxActualDirect = getMatchingList(max, costType, subType);
	this.minActualDirect = getMatchingList(min, costType, subType);

	clear();
    }

    private void initPlannedDirect() {
	EstimateCostType costType = EstimateCostType.DIRECT;
	int subType = EstimateCostType.PLANNED;
	double max = getMax();
	double min = getMin();

	initValues(costType, subType);
	this.maxPlannedDirect = getMatchingList(max, costType, subType);
	this.minPlannedDirect = getMatchingList(min, costType, subType);

	clear();
    }

    private List<EstimateCost> getMatchingList(double comparator, EstimateCostType costType,
	    int subType) {
	List<EstimateCost> returnList = new ArrayList<EstimateCost>();
	for (EstimateCost expense : this.estimates) {
	    double cost = subType == EstimateCostType.PLANNED ? expense.getCost()
		    : expense.getActualCost();
	    if (comparator == cost && expense.getCostType() == costType) {
		returnList.add(expense);
	    }
	}
	return returnList;
    }

    private void initValues(EstimateCostType costType, int subType) {
	for (EstimateCost expense : this.estimates) {
	    if (expense.getCostType() == costType) {

		// Add only planned values.
		if (subType == EstimateCostType.PLANNED) {
		    addValue(expense.getCost());
		} else {
		    addValue(expense.getActualCost());
		}
	    }
	}
    }

}