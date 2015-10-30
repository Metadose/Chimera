package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.text.NumberFormat;

import com.cebedo.pmsys.utils.NumberFormatUtils;

public class EstimateResultMasonryCHB implements Serializable {

    private static NumberFormat quantityFormatter = NumberFormatUtils.getQuantityFormatter();
    private static final long serialVersionUID = -5533583947231752985L;

    private double totalCHB;
    private double costCHB;

    public EstimateResultMasonryCHB() {
	;
    }

    public EstimateResultMasonryCHB(EstimateComputation estimateComputationBean, double totalCHB2) {
	setTotalCHB(totalCHB2);
	setCostCHB(this.totalCHB * estimateComputationBean.getCostPerUnitCHB());
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

    public double getCostCHB() {
	return costCHB;
    }

    public void setCostCHB(double costCHB) {
	this.costCHB = costCHB;
    }

}
