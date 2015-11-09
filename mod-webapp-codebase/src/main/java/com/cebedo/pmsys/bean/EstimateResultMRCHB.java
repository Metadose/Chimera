package com.cebedo.pmsys.bean;

import java.io.Serializable;

public class EstimateResultMRCHB implements Serializable {

    private static final long serialVersionUID = -6116881843223134105L;

    private double steelBarsQuantity;
    private double tieWireKilos;
    private double tieWireRolls;

    private double costSteelBars;
    private double costTieWireKilos;
    private double costTieWireRolls;

    private double steelBarLength;

    public EstimateResultMRCHB() {
	;
    }

    public EstimateResultMRCHB(EstimateComputation estimateComputationBean, double steelBars,
	    double tieWireKilos2, double tieWireRolls2, Double lengthToUse) {
	setSteelBarLength(lengthToUse);
	setSteelBarsQuantity(steelBars);
	setTieWireKilos(tieWireKilos2);
	setTieWireRolls(tieWireRolls2);

	double costSB = steelBars * estimateComputationBean.getCostPerUnitSteelBars();
	double costTWK = tieWireKilos2 * estimateComputationBean.getCostPerUnitTieWireKilos();
	double costTWR = tieWireRolls2 * estimateComputationBean.getCostPerUnitTieWireRolls();

	setCostSteelBars(costSB);
	setCostTieWireKilos(costTWK);
	setCostTieWireRolls(costTWR);
    }

    public double getSteelBarsQuantity() {
	return steelBarsQuantity;
    }

    public void setSteelBarsQuantity(double steelBarsQuantity) {
	this.steelBarsQuantity = steelBarsQuantity;
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

    public double getCostSteelBars() {
	return costSteelBars;
    }

    public void setCostSteelBars(double costSteelBars) {
	this.costSteelBars = costSteelBars;
    }

    public double getSteelBarLength() {
	return steelBarLength;
    }

    public void setSteelBarLength(double steelBarLength) {
	this.steelBarLength = steelBarLength;
    }

}
