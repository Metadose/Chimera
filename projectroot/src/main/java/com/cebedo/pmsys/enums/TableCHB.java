package com.cebedo.pmsys.enums;

public enum TableCHB {

    CHB_10_20_40(10, CommonLengthUnit.CENTIMETER, 20,
	    CommonLengthUnit.CENTIMETER, 40, CommonLengthUnit.CENTIMETER),

    CHB_15_20_40(15, CommonLengthUnit.CENTIMETER, 20,
	    CommonLengthUnit.CENTIMETER, 40, CommonLengthUnit.CENTIMETER),

    CHB_20_20_40(20, CommonLengthUnit.CENTIMETER, 20,
	    CommonLengthUnit.CENTIMETER, 40, CommonLengthUnit.CENTIMETER);

    private double thickness;
    private double height;
    private double length;
    private CommonLengthUnit thicknessUnit;
    private CommonLengthUnit heightUnit;
    private CommonLengthUnit lengthUnit;

    TableCHB(double thickness, CommonLengthUnit thicknessUnit, double height,
	    CommonLengthUnit heightUnit, double length,
	    CommonLengthUnit lengthUnit) {
	this.thickness = thickness;
	this.height = height;
	this.length = length;
	this.thicknessUnit = thicknessUnit;
	this.heightUnit = heightUnit;
	this.lengthUnit = lengthUnit;
    }
}
