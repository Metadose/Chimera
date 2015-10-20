package com.cebedo.pmsys.bean;

import com.cebedo.pmsys.domain.AbstractExpense;
import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.Ordering;

public class ComparatorAbstractExpense extends Ordering<AbstractExpense> {

    private SortOrder order = SortOrder.ASCENDING;

    public ComparatorAbstractExpense() {
	;
    }

    @Override
    public int compare(AbstractExpense left, AbstractExpense right) {

	double leftValue = left.getCost();
	double rightValue = right.getCost();

	// And all results are valid.
	int comparison = leftValue > rightValue ? 1 : rightValue > leftValue ? -1 : 0;
	return this.order == SortOrder.ASCENDING ? comparison : comparison * -1;
    }

    public SortOrder getOrder() {
	return order;
    }

    public ComparatorAbstractExpense setOrder(SortOrder order) {
	this.order = order;
	return this;
    }

}
