package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.enums.TableCHBDimensions;
import com.cebedo.pmsys.enums.TableCHBLayingMixture;
import com.cebedo.pmsys.enums.TableConcreteProportion;

public class MasonryCHBLayingEstimateResults implements Serializable {

    private static final long serialVersionUID = -927040799981812601L;

    /**
     * Ingredients.
     */
    private TableCHBDimensions chb;
    private TableCHBLayingMixture chbLayingMix;
    private TableConcreteProportion proportion;

    /**
     * Results.
     */
    private double bags;
    private double sand;

    public MasonryCHBLayingEstimateResults() {
	;
    }

    public MasonryCHBLayingEstimateResults(TableCHBDimensions chb,
	    TableCHBLayingMixture chbLayingMix,
	    TableConcreteProportion proportion, double bagsNeeded,
	    double sandNeeded) {
	setChb(chb);
	setChbLayingMix(chbLayingMix);
	setProportion(proportion);
	setBags(bagsNeeded);
	setSand(sandNeeded);
    }

    public double getBags() {
	return bags;
    }

    public void setBags(double bags) {
	this.bags = bags;
    }

    public double getSand() {
	return sand;
    }

    public void setSand(double sand) {
	this.sand = sand;
    }

    public TableCHBDimensions getChb() {
	return chb;
    }

    public void setChb(TableCHBDimensions chb) {
	this.chb = chb;
    }

    public TableCHBLayingMixture getChbLayingMix() {
	return chbLayingMix;
    }

    public void setChbLayingMix(TableCHBLayingMixture chbLayingMix) {
	this.chbLayingMix = chbLayingMix;
    }

    public TableConcreteProportion getProportion() {
	return proportion;
    }

    public void setProportion(TableConcreteProportion proportion) {
	this.proportion = proportion;
    }

}
