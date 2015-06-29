package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class MasonryEstimateResults implements Serializable {

    private static final long serialVersionUID = -5533583947231752985L;

    private double totalCHB;
    private CHB chbMeasurement;

    public MasonryEstimateResults() {
	;
    }

    public MasonryEstimateResults(CHB chb, double totalCHB2) {
	setChbMeasurement(chb);
	setTotalCHB(totalCHB2);
    }

    public double getTotalCHB() {
	return totalCHB;
    }

    public String getTotalCHBAsString() {
	return NumberFormatUtils.getFormattedCeilingQuantity(this.totalCHB);
    }

    public void setTotalCHB(double totalCHB) {
	this.totalCHB = totalCHB;
    }

    public CHB getChbMeasurement() {
	return chbMeasurement;
    }

    public void setChbMeasurement(CHB chbMeasurement) {
	this.chbMeasurement = chbMeasurement;
    }
}
