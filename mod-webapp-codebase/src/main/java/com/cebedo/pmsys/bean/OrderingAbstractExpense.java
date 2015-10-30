package com.cebedo.pmsys.bean;

import com.cebedo.pmsys.base.AbstractExpense;
import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.Ordering;

public class OrderingAbstractExpense extends Ordering<AbstractExpense> {

    private SortOrder order = SortOrder.ASCENDING;

    public OrderingAbstractExpense() {
	;
    }

    public OrderingAbstractExpense(SortOrder order2) {
	this.order = order2;
    }

    @Override
    public int compare(AbstractExpense left, AbstractExpense right) {

	double leftValue = left.getCost();
	double rightValue = right.getCost();

	// And all results are valid.
	int comparison = leftValue > rightValue ? 1 : rightValue > leftValue ? -1 : 0;
	return this.order == SortOrder.ASCENDING ? comparison : comparison * -1;
    }

}
