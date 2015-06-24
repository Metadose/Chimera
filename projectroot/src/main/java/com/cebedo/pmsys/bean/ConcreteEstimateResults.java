package com.cebedo.pmsys.bean;

import java.io.Serializable;

public class ConcreteEstimateResults implements Serializable {

    private static final long serialVersionUID = -2748458108457052450L;

    private double cement40kg;
    private double cement50kg;
    private double sand;
    private double gravel;

    public ConcreteEstimateResults() {
	;
    }

    public ConcreteEstimateResults(double estCement40kg, double estCement50kg,
	    double estSand, double estGravel) {
	setCement40kg(estCement40kg);
	setCement50kg(estCement50kg);
	setSand(estSand);
	setGravel(estGravel);
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

}
