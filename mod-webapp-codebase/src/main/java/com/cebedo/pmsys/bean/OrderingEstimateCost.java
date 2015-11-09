package com.cebedo.pmsys.bean;

import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.enums.TypeEstimateCost;
import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.Ordering;

public class OrderingEstimateCost extends Ordering<EstimateCost> {

    private int subType = TypeEstimateCost.SUB_TYPE_PLANNED;
    private SortOrder order = SortOrder.ASCENDING;

    public OrderingEstimateCost() {
	;
    }

    public OrderingEstimateCost(int sub, SortOrder order) {
	this.subType = sub;
	this.order = order;
    }

    @Override
    public int compare(EstimateCost left, EstimateCost right) {
	double leftValue = this.subType == TypeEstimateCost.SUB_TYPE_PLANNED ? left.getCost()
		: left.getActualCost();
	double rightValue = this.subType == TypeEstimateCost.SUB_TYPE_PLANNED ? right.getCost()
		: right.getActualCost();

	// And all results are valid.
	int comparison = leftValue > rightValue ? 1 : rightValue > leftValue ? -1 : 0;
	return this.order == SortOrder.ASCENDING ? comparison : comparison * -1;
    }
}
