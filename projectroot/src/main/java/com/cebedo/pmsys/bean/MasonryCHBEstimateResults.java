package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.text.NumberFormat;

import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class MasonryCHBEstimateResults implements Serializable {

    private static NumberFormat quantityFormatter = NumberFormatUtils
	    .getQuantityFormatter();
    private static final long serialVersionUID = -5533583947231752985L;

    private double totalCHB;
    private CHB chbMeasurement;

    public MasonryCHBEstimateResults() {
	;
    }

    public MasonryCHBEstimateResults(CHB chb, double totalCHB2) {
	setChbMeasurement(chb);
	setTotalCHB(totalCHB2);
    }

    public double getTotalCHB() {
	return totalCHB;
    }

    public String getTotalCHBAsString() {
	return quantityFormatter.format(Math.ceil(totalCHB));
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
