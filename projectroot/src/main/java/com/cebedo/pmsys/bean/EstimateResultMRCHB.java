package com.cebedo.pmsys.bean;

import java.io.Serializable;

public class EstimateResultMRCHB implements Serializable {

    private static final long serialVersionUID = -6116881843223134105L;

    private double steelBarsQuantity;
    private double tieWireKilos;
    private double tieWireRolls;

    public EstimateResultMRCHB() {
	;
    }

    public EstimateResultMRCHB(double steelBars, double tieWireKilos2, double tieWireRolls2) {
	setSteelBarsQuantity(steelBars);
	setTieWireKilos(tieWireKilos2);
	setTieWireRolls(tieWireRolls2);
    }

    public double getSteelBarsQuantity() {
	return steelBarsQuantity;
    }

    public void setSteelBarsQuantity(double steelBarsQuantity) {
	this.steelBarsQuantity = steelBarsQuantity;
    }

    public double getTieWireKilos() {
	return tieWireKilos;
    }

    public void setTieWireKilos(double tieWireKilos) {
	this.tieWireKilos = tieWireKilos;
    }

    public double getTieWireRolls() {
	return tieWireRolls;
    }

    public void setTieWireRolls(double tieWireRolls) {
	this.tieWireRolls = tieWireRolls;
    }

}
