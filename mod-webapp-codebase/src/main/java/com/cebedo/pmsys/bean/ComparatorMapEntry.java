package com.cebedo.pmsys.bean;

import java.util.Map;

import com.cebedo.pmsys.model.Staff;
import com.google.common.collect.Ordering;

public class ComparatorMapEntry extends Ordering<Map.Entry<Staff, Integer>> {

    public enum Order {
	ASCENDING(), DESCENDING();
    }

    private Order order = Order.ASCENDING;

    public ComparatorMapEntry() {
	;
    }

    @Override
    public int compare(Map.Entry<Staff, Integer> left, Map.Entry<Staff, Integer> right) {
	int comparison = left.getValue().compareTo(right.getValue());
	if (this.order == Order.DESCENDING) {
	    return comparison * -1;
	}
	return comparison;
    }

    public Order getOrder() {
	return order;
    }

    public ComparatorMapEntry setOrder(Order order) {
	this.order = order;
	return this;
    }

}
