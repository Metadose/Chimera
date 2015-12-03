package com.cebedo.pmsys.bean;

import java.io.Serializable;

public class PairCountValue implements Serializable {

    private static final long serialVersionUID = 5704659894068939477L;

    private double count;
    private double value;

    public PairCountValue() {
	;
    }

    public PairCountValue(double c, double v) {
	setCount(c);
	setValue(v);
    }

    public double getCount() {
	return count;
    }

    public void setCount(double count) {
	this.count = count;
    }

    public double getValue() {
	return value;
    }

    public void setValue(double value) {
	this.value = value;
    }

    public String toString() {
	return String.format("[C: %s, V: %s]", count, value);
    }

}
