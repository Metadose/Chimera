package com.cebedo.pmsys.bean;

import java.util.Comparator;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ComparatorValue implements Comparator {

    public enum ValueOrdering {
	ASCENDING(), DESCENDING();
    }

    private Map base;
    private ValueOrdering ordering;

    public ComparatorValue(Map base, ValueOrdering order) {
	this.base = base;
	this.ordering = order;
    }

    @Override
    public int compare(Object arg0, Object arg1) {
	if ((Double) this.base.get(arg0) >= (Double) this.base.get(arg1)) {
	    return this.ordering == ValueOrdering.ASCENDING ? -1 : 1;
	} else {
	    return this.ordering == ValueOrdering.ASCENDING ? 1 : -1;
	}
    }
}