package com.cebedo.pmsys.bean;

import java.io.Serializable;

import com.cebedo.pmsys.domain.BlockLayingMixture;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.domain.ConcreteProportion;

public class MasonryBlockLayingEstimateResults implements Serializable {

    private static final long serialVersionUID = -927040799981812601L;

    /**
     * Inputs.
     */
    private BlockLayingMixture blockLayingMixture;
    private CHB chbMeasurement;

    /**
     * Assigned input.
     */
    private ConcreteProportion concreteProportion;

    /**
     * Results.
     */
    private double bags;
    private double sand;

    public MasonryBlockLayingEstimateResults() {
	;
    }

    public MasonryBlockLayingEstimateResults(CHB chb,
	    BlockLayingMixture mixture, double bagsNeeded, double sandNeeded,
	    ConcreteProportion proportion) {
	setBags(bagsNeeded);
	setSand(sandNeeded);
	setBlockLayingMixture(mixture);
	setChbMeasurement(chb);
	setConcreteProportion(proportion);
    }

    public BlockLayingMixture getBlockLayingMixture() {
	return blockLayingMixture;
    }

    public void setBlockLayingMixture(BlockLayingMixture blockLayingMixture) {
	this.blockLayingMixture = blockLayingMixture;
    }

    public CHB getChbMeasurement() {
	return chbMeasurement;
    }

    public void setChbMeasurement(CHB chbMeasurement) {
	this.chbMeasurement = chbMeasurement;
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

    public ConcreteProportion getConcreteProportion() {
	return concreteProportion;
    }

    public void setConcreteProportion(ConcreteProportion concreteProportion) {
	this.concreteProportion = concreteProportion;
    }

}
