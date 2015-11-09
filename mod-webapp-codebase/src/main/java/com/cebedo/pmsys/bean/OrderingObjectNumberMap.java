package com.cebedo.pmsys.bean;

import java.util.Map;

import com.cebedo.pmsys.enums.SortOrder;
import com.google.common.collect.Ordering;

public class OrderingObjectNumberMap extends Ordering<Map.Entry<? extends Object, ? extends Number>> {

    private SortOrder order = SortOrder.ASCENDING;

    public OrderingObjectNumberMap() {
	;
    }

    public OrderingObjectNumberMap(SortOrder order2) {
	this.order = order2;
    }

    @Override
    public int compare(Map.Entry<? extends Object, ? extends Number> left,
	    Map.Entry<? extends Object, ? extends Number> right) {
	Double leftVal = left.getValue().doubleValue();
	Double rightVal = right.getValue().doubleValue();
	int comparison = leftVal.compareTo(rightVal);
	if (this.order == SortOrder.DESCENDING) {
	    return comparison * -1;
	}
	return comparison;
    }
}
