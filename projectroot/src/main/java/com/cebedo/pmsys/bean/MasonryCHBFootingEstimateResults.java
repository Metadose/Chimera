package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.enums.TableCHBFootingDimensions;
import com.cebedo.pmsys.enums.TableCHBFootingMixture;

public class MasonryCHBFootingEstimateResults implements Serializable {

    private static final long serialVersionUID = -7547660399074104602L;

    /**
     * Ingredients.
     */
    private TableCHBFootingDimensions footingDimensions;
    private TableCHBFootingMixture footingMixture;

    /**
     * Results.
     */
    private double cement;
    private double sand;
    private double gravel;

    public MasonryCHBFootingEstimateResults() {
	;
    }

    public MasonryCHBFootingEstimateResults(double cement2, double gravel2,
	    double sand2, TableCHBFootingDimensions chbFooting,
	    TableCHBFootingMixture footingMixture) {
	setCement(cement2);
	setGravel(gravel2);
	setSand(sand2);
	setFootingDimensions(chbFooting);
	setFootingMixture(footingMixture);
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

    public TableCHBFootingDimensions getFootingDimensions() {
	return footingDimensions;
    }

    public void setFootingDimensions(TableCHBFootingDimensions footingDimensions) {
	this.footingDimensions = footingDimensions;
    }

    public TableCHBFootingMixture getFootingMixture() {
	return footingMixture;
    }

    public void setFootingMixture(TableCHBFootingMixture footingMixture) {
	this.footingMixture = footingMixture;
    }

}
