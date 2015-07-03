package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.domain.ConcreteProportion;
import com.cebedo.pmsys.domain.PlasterMixture;

public class MasonryPlasteringEstimateResults implements Serializable {

    private static final long serialVersionUID = 1206561790105254749L;

    // Set the results, concrete proportion, plaster mixture,
    // is back to back, plaster top side.

    private ConcreteProportion concreteProportion;
    private PlasterMixture plasterMixture;
    private double cement40kg;
    private double cement50kg;
    private double sand;

    private boolean plasterBackToBack;
    private boolean plasterTopSide;

    public MasonryPlasteringEstimateResults() {
	;
    }

    public MasonryPlasteringEstimateResults(double bags40kg, double bags50kg,
	    double sand2, ConcreteProportion proportion,
	    PlasterMixture plasterMixture2, boolean plasterBackToBack2,
	    boolean plasterTopSide2) {
	setCement40kg(bags40kg);
	setCement50kg(bags50kg);
	setSand(sand2);
	setConcreteProportion(proportion);
	setPlasterMixture(plasterMixture2);
	setPlasterBackToBack(plasterBackToBack2);
	setPlasterTopSide(plasterTopSide2);
    }

    public ConcreteProportion getConcreteProportion() {
	return concreteProportion;
    }

    public void setConcreteProportion(ConcreteProportion concreteProportion) {
	this.concreteProportion = concreteProportion;
    }

    public PlasterMixture getPlasterMixture() {
	return plasterMixture;
    }

    public void setPlasterMixture(PlasterMixture plasterMixture) {
	this.plasterMixture = plasterMixture;
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

    public boolean isPlasterBackToBack() {
	return plasterBackToBack;
    }

    public void setPlasterBackToBack(boolean plasterBackToBack) {
	this.plasterBackToBack = plasterBackToBack;
    }

    public boolean isPlasterTopSide() {
	return plasterTopSide;
    }

    public void setPlasterTopSide(boolean plasterTopSide) {
	this.plasterTopSide = plasterTopSide;
    }

}
