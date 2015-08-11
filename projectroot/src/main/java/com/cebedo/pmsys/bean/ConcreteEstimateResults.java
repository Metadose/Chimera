package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ConcreteEstimateResults implements Serializable {

    private static final long serialVersionUID = -2748458108457052450L;

    private double cement40kg;
    private double cement50kg;
    private double sand;
    private double gravel;

    private double costCement40kg;
    private double costCement50kg;
    private double costSand;
    private double costGravel;

    public ConcreteEstimateResults() {
	;
    }

    public ConcreteEstimateResults(EstimateBean estimateBean, double estCement40kg,
	    double estCement50kg, double estSand, double estGravel) {
	setCement40kg(estCement40kg);
	setCement50kg(estCement50kg);
	setSand(estSand);
	setGravel(estGravel);

	setCostCement40kg(estCement40kg * estimateBean.getCostPerUnitCement40kg());
	setCostCement50kg(estCement50kg * estimateBean.getCostPerUnitCement50kg());
	setCostSand(estSand * estimateBean.getCostPerUnitSand());
	setCostGravel(estGravel * estimateBean.getCostPerUnitGravel());
    }

    /**
     * Formatted quantity text display.
     */
    public String getCement40kgAsString() {
	return getFormattedQuantityAsString(cement40kg);
    }

    public String getCement50kgAsString() {
	return getFormattedQuantityAsString(cement50kg);
    }

    public String getSandAsString() {
	return getFormattedQuantityAsString(sand);
    }

    public String getGravelAsString() {
	return getFormattedQuantityAsString(gravel);
    }

    /**
     * Format function.
     */
    private String getFormattedQuantityAsString(double dblVal) {
	double displayDbl = Math.ceil(dblVal);
	DecimalFormat df = new DecimalFormat();
	return df.format(displayDbl);
    }

    public double getCement40kg() {
	return cement40kg;
    }

    public void setCement40kg(double cement40kg) {
	this.cement40kg = cement40kg;
    }

    public double getCement50kg() {
	return cement50kg;
    }

    public void setCement50kg(double cement50kg) {
	this.cement50kg = cement50kg;
    }

    public double getSand() {
	return sand;
    }

    public void setSand(double sand) {
	this.sand = sand;
    }

    public double getGravel() {
	return gravel;
    }

    public void setGravel(double gravel) {
	this.gravel = gravel;
    }

    public double getCostCement40kg() {
	return costCement40kg;
    }

    public void setCostCement40kg(double costCement40kg) {
	this.costCement40kg = costCement40kg;
    }

    public double getCostCement50kg() {
	return costCement50kg;
    }

    public void setCostCement50kg(double costCement50kg) {
	this.costCement50kg = costCement50kg;
    }

    public double getCostSand() {
	return costSand;
    }

    public void setCostSand(double costSand) {
	this.costSand = costSand;
    }

    public double getCostGravel() {
	return costGravel;
    }

    public void setCostGravel(double costGravel) {
	this.costGravel = costGravel;
    }

}
