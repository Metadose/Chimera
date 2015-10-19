package com.cebedo.pmsys.bean;

import java.util.Map;

import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.Ordering;

public class ComparatorMapEntry extends Ordering<Map.Entry<? extends Object, Integer>> {

    private SortOrder order = SortOrder.ASCENDING;

    public ComparatorMapEntry() {
	;
    }

    @Override
    public int compare(Map.Entry<? extends Object, Integer> left,
	    Map.Entry<? extends Object, Integer> right) {
	int comparison = left.getValue().compareTo(right.getValue());
	if (this.order == SortOrder.DESCENDING) {
	    return comparison * -1;
	}
	return comparison;
    }

    public SortOrder getOrder() {
	return order;
    }

    public ComparatorMapEntry setOrder(SortOrder order) {
	this.order = order;
	return this;
    }

}
