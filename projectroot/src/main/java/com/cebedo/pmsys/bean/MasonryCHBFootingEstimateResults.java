package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.utils.EstimateUtils;

public class MasonryCHBFootingEstimateResults implements Serializable {

    private static final long serialVersionUID = -7547660399074104602L;

    /**
     * Results.
     */
    private double cement;
    private double sand;
    private double gravel;

    private double costCement40kg;
    private double costCement50kg;
    private double costSand;
    private double costGravel;

    public MasonryCHBFootingEstimateResults() {
	;
    }

    public MasonryCHBFootingEstimateResults(Estimate estimate, double cement40kg, double gravel2,
	    double sand2) {
	setCement40kg(cement40kg);
	setGravel(gravel2);
	setSand(sand2);

	double bags50kg = EstimateUtils.convert40kgTo50kg(cement40kg);
	setCostCement40kg(cement40kg * estimate.getCostPerUnitCement40kg());
	setCostCement50kg(bags50kg * estimate.getCostPerUnitCement50kg());
	setCostSand(sand2 * estimate.getCostPerUnitSand());
	setCostGravel(gravel2 * estimate.getCostPerUnitGravel());
    }

    public double getCement40kg() {
	return cement;
    }

    public void setCement40kg(double cement) {
	this.cement = cement;
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
