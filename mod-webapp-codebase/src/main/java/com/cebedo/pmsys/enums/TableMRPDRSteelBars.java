package com.cebedo.pmsys.enums;

/**
 * Plain Deformed Round Steel Bars.
 */
@Deprecated
public enum TableMRPDRSteelBars {

    BAR_8_5(8, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 1.98, CommonMassUnit.KILOGRAM),

    BAR_10_5(10, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 38, CommonMassUnit.KILOGRAM),

    BAR_12_5(12, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 4.44, CommonMassUnit.KILOGRAM),

    BAR_13_5(13, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 5.21, CommonMassUnit.KILOGRAM),

    BAR_16_5(16, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 7.90, CommonMassUnit.KILOGRAM),

    BAR_20_5(20, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 12.33, CommonMassUnit.KILOGRAM),

    BAR_25_5(25, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 19.27, CommonMassUnit.KILOGRAM),

    BAR_26_5(26, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 24.17, CommonMassUnit.KILOGRAM),

    BAR_30_5(30, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 27.75, CommonMassUnit.KILOGRAM),

    BAR_32_5(32, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 31.57, CommonMassUnit.KILOGRAM),

    BAR_36_5(36, 5, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 39.96, CommonMassUnit.KILOGRAM),

    BAR_8_6(8, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 2.37, CommonMassUnit.KILOGRAM),

    BAR_10_6(10, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 3.70, CommonMassUnit.KILOGRAM),

    BAR_12_6(12, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 5.33, CommonMassUnit.KILOGRAM),

    BAR_13_6(13, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 6.25, CommonMassUnit.KILOGRAM),

    BAR_16_6(16, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 9.47, CommonMassUnit.KILOGRAM),

    BAR_20_6(20, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 14.80, CommonMassUnit.KILOGRAM),

    BAR_25_6(25, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 23.12, CommonMassUnit.KILOGRAM),

    BAR_26_6(26, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 29.00, CommonMassUnit.KILOGRAM),

    BAR_30_6(30, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 33.29, CommonMassUnit.KILOGRAM),

    BAR_32_6(32, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 37.88, CommonMassUnit.KILOGRAM),

    BAR_36_6(36, 6, CommonLengthUnit.MILLIMETER, CommonLengthUnit.METER, 47.95, CommonMassUnit.KILOGRAM);

    private double diameter;
    private double length;
    private CommonLengthUnit diameterUnit;
    private CommonLengthUnit lengthUnit;
    private double weight;
    private CommonMassUnit weightUnit;

    TableMRPDRSteelBars(double diam, double length, CommonLengthUnit diamUnit, CommonLengthUnit lenUnit,
	    double weight, CommonMassUnit weightUnit) {
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

    public CommonLengthUnit getDiameterUnit() {
	return diameterUnit;
    }

    public void setDiameterUnit(CommonLengthUnit diameterUnit) {
	this.diameterUnit = diameterUnit;
    }

    public CommonLengthUnit getLengthUnit() {
	return lengthUnit;
    }

    public void setLengthUnit(CommonLengthUnit lengthUnit) {
	this.lengthUnit = lengthUnit;
    }

    public double getWeight() {
	return weight;
    }

    public void setWeight(double weight) {
	this.weight = weight;
    }

    public CommonMassUnit getWeightUnit() {
	return weightUnit;
    }

    public void setWeightUnit(CommonMassUnit weightUnit) {
	this.weightUnit = weightUnit;
    }
}
