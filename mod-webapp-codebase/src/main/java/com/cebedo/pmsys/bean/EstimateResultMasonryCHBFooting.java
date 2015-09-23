package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.utils.EstimateUtils;

public class EstimateResultMasonryCHBFooting implements Serializable {

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

    public EstimateResultMasonryCHBFooting() {
	;
    }

    public EstimateResultMasonryCHBFooting(EstimateComputationBean estimateComputationBean,
	    double cement40kg, double gravel2, double sand2) {
	setCement40kg(cement40kg);
	setGravel(gravel2);
	setSand(sand2);

	double cement50kg = Math.ceil(EstimateUtils.convert40kgTo50kg(cement40kg));
	setCostCement40kg(cement40kg * estimateComputationBean.getCostPerUnitCement40kg());
	setCostCement50kg(cement50kg * estimateComputationBean.getCostPerUnitCement50kg());
	setCostSand(sand2 * estimateComputationBean.getCostPerUnitSand());
	setCostGravel(gravel2 * estimateComputationBean.getCostPerUnitGravel());
    }

    public double getCement40kg() {
	return cement;
    }

    public double getCement50kg() {
	return Math.ceil(EstimateUtils.convert40kgTo50kg(getCement40kg()));
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
