package com.cebedo.pmsys.enums;

public enum TableCHBDimensions {

    CHB_10_20_40("10cm x 20cm x 40cm", 10, CommonLengthUnit.CENTIMETER, 20,
	    CommonLengthUnit.CENTIMETER, 40, CommonLengthUnit.CENTIMETER),

    CHB_15_20_40("15cm x 20cm x 40cm", 15, CommonLengthUnit.CENTIMETER, 20,
	    CommonLengthUnit.CENTIMETER, 40, CommonLengthUnit.CENTIMETER),

    CHB_20_20_40("20cm x 20cm x 40cm", 20, CommonLengthUnit.CENTIMETER, 20,
	    CommonLengthUnit.CENTIMETER, 40, CommonLengthUnit.CENTIMETER);

    private String label;
    private double thickness;
    private double height;
    private double length;
    private CommonLengthUnit thicknessUnit;
    private CommonLengthUnit heightUnit;
    private CommonLengthUnit lengthUnit;

    TableCHBDimensions(String label, double thickness,
	    CommonLengthUnit thicknessUnit, double height,
	    CommonLengthUnit heightUnit, double length,
	    CommonLengthUnit lengthUnit) {
	this.label = label;
	this.thickness = thickness;
	this.height = height;
	this.length = length;
	this.thicknessUnit = thicknessUnit;
	this.heightUnit = heightUnit;
	this.lengthUnit = lengthUnit;
    }
}
