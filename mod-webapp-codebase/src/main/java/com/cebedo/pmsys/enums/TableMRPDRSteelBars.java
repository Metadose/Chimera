package com.cebedo.pmsys.enums;

/**
 * Plain Deformed Round Steel Bars.
 */
@Deprecated
public enum TableMRPDRSteelBars {

    BAR_8_5(8, 5, UnitLength.MILLIMETER, UnitLength.METER, 1.98, UnitMass.KILOGRAM),

    BAR_10_5(10, 5, UnitLength.MILLIMETER, UnitLength.METER, 38, UnitMass.KILOGRAM),

    BAR_12_5(12, 5, UnitLength.MILLIMETER, UnitLength.METER, 4.44, UnitMass.KILOGRAM),

    BAR_13_5(13, 5, UnitLength.MILLIMETER, UnitLength.METER, 5.21, UnitMass.KILOGRAM),

    BAR_16_5(16, 5, UnitLength.MILLIMETER, UnitLength.METER, 7.90, UnitMass.KILOGRAM),

    BAR_20_5(20, 5, UnitLength.MILLIMETER, UnitLength.METER, 12.33, UnitMass.KILOGRAM),

    BAR_25_5(25, 5, UnitLength.MILLIMETER, UnitLength.METER, 19.27, UnitMass.KILOGRAM),

    BAR_26_5(26, 5, UnitLength.MILLIMETER, UnitLength.METER, 24.17, UnitMass.KILOGRAM),

    BAR_30_5(30, 5, UnitLength.MILLIMETER, UnitLength.METER, 27.75, UnitMass.KILOGRAM),

    BAR_32_5(32, 5, UnitLength.MILLIMETER, UnitLength.METER, 31.57, UnitMass.KILOGRAM),

    BAR_36_5(36, 5, UnitLength.MILLIMETER, UnitLength.METER, 39.96, UnitMass.KILOGRAM),

    BAR_8_6(8, 6, UnitLength.MILLIMETER, UnitLength.METER, 2.37, UnitMass.KILOGRAM),

    BAR_10_6(10, 6, UnitLength.MILLIMETER, UnitLength.METER, 3.70, UnitMass.KILOGRAM),

    BAR_12_6(12, 6, UnitLength.MILLIMETER, UnitLength.METER, 5.33, UnitMass.KILOGRAM),

    BAR_13_6(13, 6, UnitLength.MILLIMETER, UnitLength.METER, 6.25, UnitMass.KILOGRAM),

    BAR_16_6(16, 6, UnitLength.MILLIMETER, UnitLength.METER, 9.47, UnitMass.KILOGRAM),

    BAR_20_6(20, 6, UnitLength.MILLIMETER, UnitLength.METER, 14.80, UnitMass.KILOGRAM),

    BAR_25_6(25, 6, UnitLength.MILLIMETER, UnitLength.METER, 23.12, UnitMass.KILOGRAM),

    BAR_26_6(26, 6, UnitLength.MILLIMETER, UnitLength.METER, 29.00, UnitMass.KILOGRAM),

    BAR_30_6(30, 6, UnitLength.MILLIMETER, UnitLength.METER, 33.29, UnitMass.KILOGRAM),

    BAR_32_6(32, 6, UnitLength.MILLIMETER, UnitLength.METER, 37.88, UnitMass.KILOGRAM),

    BAR_36_6(36, 6, UnitLength.MILLIMETER, UnitLength.METER, 47.95, UnitMass.KILOGRAM);

    private double diameter;
    private double length;
    private UnitLength diameterUnit;
    private UnitLength lengthUnit;
    private double weight;
    private UnitMass weightUnit;

    TableMRPDRSteelBars(double diam, double length, UnitLength diamUnit, UnitLength lenUnit,
	    double weight, UnitMass weightUnit) {
	this.diameter = diam;
	this.diameterUnit = diamUnit;
	this.length = length;
	this.lengthUnit = lenUnit;
	this.weight = weight;
	this.weightUnit = weightUnit;
    }

    public double getDiameter() {
	return diameter;
    }

    public void setDiameter(double diameter) {
	this.diameter = diameter;
    }

    public double getLength() {
	return length;
    }

    public void setLength(double length) {
	this.length = length;
    }

    public UnitLength getDiameterUnit() {
	return diameterUnit;
    }

    public void setDiameterUnit(UnitLength diameterUnit) {
	this.diameterUnit = diameterUnit;
    }

    public UnitLength getLengthUnit() {
	return lengthUnit;
    }

    public void setLengthUnit(UnitLength lengthUnit) {
	this.lengthUnit = lengthUnit;
    }

    public double getWeight() {
	return weight;
    }

    public void setWeight(double weight) {
	this.weight = weight;
    }

    public UnitMass getWeightUnit() {
	return weightUnit;
    }

    public void setWeightUnit(UnitMass weightUnit) {
	this.weightUnit = weightUnit;
    }
}
