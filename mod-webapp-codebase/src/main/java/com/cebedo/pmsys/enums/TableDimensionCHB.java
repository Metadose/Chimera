package com.cebedo.pmsys.enums;

public enum TableDimensionCHB {

    CHB_10_20_40("10cm x 20cm x 40cm", 10, UnitLength.CENTIMETER, 20,
	    UnitLength.CENTIMETER, 40, UnitLength.CENTIMETER),

    CHB_15_20_40("15cm x 20cm x 40cm", 15, UnitLength.CENTIMETER, 20,
	    UnitLength.CENTIMETER, 40, UnitLength.CENTIMETER),

    CHB_20_20_40("20cm x 20cm x 40cm", 20, UnitLength.CENTIMETER, 20,
	    UnitLength.CENTIMETER, 40, UnitLength.CENTIMETER);

    public static final double STANDARD_CHB_PER_SQ_M = 12.5;

    private String label;
    private double thickness;
    private double height;
    private double length;
    private UnitLength thicknessUnit;
    private UnitLength heightUnit;
    private UnitLength lengthUnit;

    TableDimensionCHB(String label, double thickness,
	    UnitLength thicknessUnit, double height,
	    UnitLength heightUnit, double length,
	    UnitLength lengthUnit) {
	this.label = label;
	this.thickness = thickness;
	this.height = height;
	this.length = length;
	this.thicknessUnit = thicknessUnit;
	this.heightUnit = heightUnit;
	this.lengthUnit = lengthUnit;
    }
}
