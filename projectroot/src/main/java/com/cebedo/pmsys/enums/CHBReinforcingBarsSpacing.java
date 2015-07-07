package com.cebedo.pmsys.enums;

public enum CHBReinforcingBarsSpacing {

    CM_40(40.0, CommonLengthUnit.CENTIMETER),

    CM_60(60.0, CommonLengthUnit.CENTIMETER),

    CM_80(80.0, CommonLengthUnit.CENTIMETER);

    private double spacing;
    private CommonLengthUnit unit;

    CHBReinforcingBarsSpacing(double space, CommonLengthUnit unt) {
	this.spacing = space;
	this.unit = unt;
    }

    public double spacing() {
	return this.spacing;
    }

    public CommonLengthUnit unit() {
	return this.unit;
    }

    public String label() {
	return this.spacing + " " + this.unit.symbol();
    }

}
