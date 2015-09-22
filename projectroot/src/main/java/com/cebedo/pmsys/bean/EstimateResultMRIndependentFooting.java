package com.cebedo.pmsys.bean;

import java.io.Serializable;

public class EstimateResultMRIndependentFooting implements Serializable {

    private static final long serialVersionUID = 3831778657286959105L;

    private double steelBarLength;
    private double steelBarsQuantity;
    private double costSteelBars;

    public EstimateResultMRIndependentFooting() {
	;
    }

    public EstimateResultMRIndependentFooting(EstimateComputationBean estimateComputationBean,
	    double estBarsToBuy, double lengthToUse) {
	setSteelBarLength(lengthToUse);
	setSteelBarsQuantity(estBarsToBuy);
	setCostSteelBars(estimateComputationBean.getCostPerUnitSteelBars() * estBarsToBuy);
    }

    public double getSteelBarLength() {
	return steelBarLength;
    }

    public void setSteelBarLength(double steelBarLength) {
	this.steelBarLength = steelBarLength;
    }

    public double getSteelBarsQuantity() {
	return steelBarsQuantity;
    }

    public void setSteelBarsQuantity(double steelBarsQuantity) {
	this.steelBarsQuantity = steelBarsQuantity;
    }

    public double getCostSteelBars() {
	return costSteelBars;
    }

    public void setCostSteelBars(double costSteelBars) {
	this.costSteelBars = costSteelBars;
    }

}
