package com.cebedo.pmsys.bean;

import java.io.Serializable;

public class EstimateResultMRIndependentFooting implements Serializable {

    private static final long serialVersionUID = 3831778657286959105L;

    private double steelBarLength;
    private double steelBarsQuantity;
    private double costSteelBars;

    private double tieWireKilos;
    private double tieWireRolls;

    private double costTieWireKilos;
    private double costTieWireRolls;

    public EstimateResultMRIndependentFooting() {
	;
    }

    public EstimateResultMRIndependentFooting(EstimateComputationBean estimateComputationBean,
	    double estBarsToBuy, double lengthToUse, double estTieWireKilos, double estTieWireRolls) {

	setSteelBarLength(lengthToUse);

	setSteelBarsQuantity(estBarsToBuy);
	setCostSteelBars(estimateComputationBean.getCostPerUnitSteelBars() * estBarsToBuy);

	setTieWireKilos(estTieWireKilos);
	setCostTieWireKilos(estimateComputationBean.getCostPerUnitTieWireKilos() * estTieWireKilos);

	setTieWireRolls(estTieWireRolls);
	setCostTieWireRolls(estimateComputationBean.getCostPerUnitTieWireRolls() * estTieWireRolls);

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

    public double getTieWireKilos() {
	return tieWireKilos;
    }

    public void setTieWireKilos(double tieWireKilos) {
	this.tieWireKilos = tieWireKilos;
    }

    public double getTieWireRolls() {
	return tieWireRolls;
    }

    public void setTieWireRolls(double tieWireRolls) {
	this.tieWireRolls = tieWireRolls;
    }

    public double getCostTieWireKilos() {
	return costTieWireKilos;
    }

    public void setCostTieWireKilos(double costTieWireKilos) {
	this.costTieWireKilos = costTieWireKilos;
    }

    public double getCostTieWireRolls() {
	return costTieWireRolls;
    }

    public void setCostTieWireRolls(double costTieWireRolls) {
	this.costTieWireRolls = costTieWireRolls;
    }

}
