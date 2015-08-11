package com.cebedo.pmsys.bean;

import java.io.Serializable;

public class MasonryPlasteringEstimateResults implements Serializable {

    private static final long serialVersionUID = 1206561790105254749L;

    private double cement40kg;
    private double cement50kg;
    private double sand;

    // Cost.
    private double costCement40kg;
    private double costCement50kg;
    private double costSand;

    public MasonryPlasteringEstimateResults() {
	;
    }

    public MasonryPlasteringEstimateResults(EstimateBean estimateBean, double bags40kg, double bags50kg,
	    double sand2) {
	setCement40kg(bags40kg);
	setCement50kg(bags50kg);
	setSand(sand2);

	setCostCement40kg(bags40kg * estimateBean.getCostPerUnitCement40kg());
	setCostCement50kg(bags50kg * estimateBean.getCostPerUnitCement50kg());
	setCostSand(sand2 * estimateBean.getCostPerUnitSand());
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

}
