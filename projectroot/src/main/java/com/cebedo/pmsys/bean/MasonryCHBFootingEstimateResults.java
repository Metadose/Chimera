package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.domain.CHBFootingDimension;
import com.cebedo.pmsys.domain.CHBFootingMixture;
import com.cebedo.pmsys.domain.ConcreteProportion;

public class MasonryCHBFootingEstimateResults implements Serializable {

    private static final long serialVersionUID = -7547660399074104602L;

    private double cement;
    private double sand;
    private double gravel;
    private CHBFootingMixture chbFootingMixture;
    private ConcreteProportion concreteProportion;
    private CHBFootingDimension chbFootingDimension;

    public MasonryCHBFootingEstimateResults() {
	;
    }

    public MasonryCHBFootingEstimateResults(double cement2, double gravel2,
	    double sand2, ConcreteProportion prop,
	    CHBFootingDimension footingDimension,
	    CHBFootingMixture chbFootingMix) {
	setCement(cement2);
	setGravel(gravel2);
	setSand(sand2);
	setConcreteProportion(prop);
	setChbFootingDimension(footingDimension);
	setChbFootingMixture(chbFootingMix);
    }

    public double getCement() {
	return cement;
    }

    public void setCement(double cement) {
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

    public CHBFootingMixture getChbFootingMixture() {
	return chbFootingMixture;
    }

    public void setChbFootingMixture(CHBFootingMixture chbFootingMixture) {
	this.chbFootingMixture = chbFootingMixture;
    }

    public ConcreteProportion getConcreteProportion() {
	return concreteProportion;
    }

    public void setConcreteProportion(ConcreteProportion concreteProportion) {
	this.concreteProportion = concreteProportion;
    }

    public CHBFootingDimension getChbFootingDimension() {
	return chbFootingDimension;
    }

    public void setChbFootingDimension(CHBFootingDimension chbFootingDimension) {
	this.chbFootingDimension = chbFootingDimension;
    }

}
