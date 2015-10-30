package com.cebedo.pmsys.enums;

public enum TableDimensionCHB {

    CHB_10_20_40("10cm x 20cm x 40cm", 10, UnitCommonLength.CENTIMETER, 20,
	    UnitCommonLength.CENTIMETER, 40, UnitCommonLength.CENTIMETER),

    CHB_15_20_40("15cm x 20cm x 40cm", 15, UnitCommonLength.CENTIMETER, 20,
	    UnitCommonLength.CENTIMETER, 40, UnitCommonLength.CENTIMETER),

    CHB_20_20_40("20cm x 20cm x 40cm", 20, UnitCommonLength.CENTIMETER, 20,
	    UnitCommonLength.CENTIMETER, 40, UnitCommonLength.CENTIMETER);

    public static final double STANDARD_CHB_PER_SQ_M = 12.5;

    private String label;
    private double thickness;
    private double height;
    private double length;
    private UnitCommonLength thicknessUnit;
    private UnitCommonLength heightUnit;
    private UnitCommonLength lengthUnit;

    TableDimensionCHB(String label, double thickness,
	    UnitCommonLength thicknessUnit, double height,
	    UnitCommonLength heightUnit, double length,
	    UnitCommonLength lengthUnit) {
	this.label = label;
	this.thickness = thickness;
	this.height = height;
	this.length = length;
	this.thicknessUnit = thicknessUnit;
	this.heightUnit = heightUnit;
	this.lengthUnit = lengthUnit;
    }
}
