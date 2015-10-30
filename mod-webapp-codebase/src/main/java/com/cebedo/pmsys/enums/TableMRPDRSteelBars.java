package com.cebedo.pmsys.enums;

/**
 * Plain Deformed Round Steel Bars.
 */
@Deprecated
public enum TableMRPDRSteelBars {

    BAR_8_5(8, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 1.98, UnitCommonMass.KILOGRAM),

    BAR_10_5(10, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 38, UnitCommonMass.KILOGRAM),

    BAR_12_5(12, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 4.44, UnitCommonMass.KILOGRAM),

    BAR_13_5(13, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 5.21, UnitCommonMass.KILOGRAM),

    BAR_16_5(16, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 7.90, UnitCommonMass.KILOGRAM),

    BAR_20_5(20, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 12.33, UnitCommonMass.KILOGRAM),

    BAR_25_5(25, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 19.27, UnitCommonMass.KILOGRAM),

    BAR_26_5(26, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 24.17, UnitCommonMass.KILOGRAM),

    BAR_30_5(30, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 27.75, UnitCommonMass.KILOGRAM),

    BAR_32_5(32, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 31.57, UnitCommonMass.KILOGRAM),

    BAR_36_5(36, 5, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 39.96, UnitCommonMass.KILOGRAM),

    BAR_8_6(8, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 2.37, UnitCommonMass.KILOGRAM),

    BAR_10_6(10, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 3.70, UnitCommonMass.KILOGRAM),

    BAR_12_6(12, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 5.33, UnitCommonMass.KILOGRAM),

    BAR_13_6(13, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 6.25, UnitCommonMass.KILOGRAM),

    BAR_16_6(16, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 9.47, UnitCommonMass.KILOGRAM),

    BAR_20_6(20, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 14.80, UnitCommonMass.KILOGRAM),

    BAR_25_6(25, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 23.12, UnitCommonMass.KILOGRAM),

    BAR_26_6(26, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 29.00, UnitCommonMass.KILOGRAM),

    BAR_30_6(30, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 33.29, UnitCommonMass.KILOGRAM),

    BAR_32_6(32, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 37.88, UnitCommonMass.KILOGRAM),

    BAR_36_6(36, 6, UnitCommonLength.MILLIMETER, UnitCommonLength.METER, 47.95, UnitCommonMass.KILOGRAM);

    private double diameter;
    private double length;
    private UnitCommonLength diameterUnit;
    private UnitCommonLength lengthUnit;
    private double weight;
    private UnitCommonMass weightUnit;

    TableMRPDRSteelBars(double diam, double length, UnitCommonLength diamUnit, UnitCommonLength lenUnit,
	    double weight, UnitCommonMass weightUnit) {
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

    public UnitCommonLength getDiameterUnit() {
	return diameterUnit;
    }

    public void setDiameterUnit(UnitCommonLength diameterUnit) {
	this.diameterUnit = diameterUnit;
    }

    public UnitCommonLength getLengthUnit() {
	return lengthUnit;
    }

    public void setLengthUnit(UnitCommonLength lengthUnit) {
	this.lengthUnit = lengthUnit;
    }

    public double getWeight() {
	return weight;
    }

    public void setWeight(double weight) {
	this.weight = weight;
    }

    public UnitCommonMass getWeightUnit() {
	return weightUnit;
    }

    public void setWeightUnit(UnitCommonMass weightUnit) {
	this.weightUnit = weightUnit;
    }
}
