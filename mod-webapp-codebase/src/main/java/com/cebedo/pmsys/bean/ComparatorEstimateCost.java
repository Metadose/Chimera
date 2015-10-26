package com.cebedo.pmsys.bean;

import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.Ordering;

public class ComparatorEstimateCost extends Ordering<EstimateCost> {

    private int subType = EstimateCostType.SUB_TYPE_PLANNED;
    private SortOrder order = SortOrder.ASCENDING;

    public ComparatorEstimateCost() {
	;
    }

    public ComparatorEstimateCost(int sub, SortOrder order) {
	this.subType = sub;
	this.order = order;
    }

    @Override
    public int compare(EstimateCost left, EstimateCost right) {
	double leftValue = this.subType == EstimateCostType.SUB_TYPE_PLANNED ? left.getCost()
		: left.getActualCost();
	double rightValue = this.subType == EstimateCostType.SUB_TYPE_PLANNED ? right.getCost()
		: right.getActualCost();

	// And all results are valid.
	int comparison = leftValue > rightValue ? 1 : rightValue > leftValue ? -1 : 0;
	return this.order == SortOrder.ASCENDING ? comparison : comparison * -1;
    }
}
