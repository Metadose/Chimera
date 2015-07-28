package com.cebedo.pmsys.enums;

public enum TableCHBFootingDimensions {

    FOOTING_10_30("10cm x 30cm", 10, CommonLengthUnit.CENTIMETER, 30,
	    CommonLengthUnit.CENTIMETER),

    FOOTING_10_35("10cm x 35cm", 10, CommonLengthUnit.CENTIMETER, 35,
	    CommonLengthUnit.CENTIMETER),

    FOOTING_10_40("10cm x 40cm", 10, CommonLengthUnit.CENTIMETER, 40,
	    CommonLengthUnit.CENTIMETER),

    FOOTING_10_50("10cm x 50cm", 10, CommonLengthUnit.CENTIMETER, 50,
	    CommonLengthUnit.CENTIMETER),

    FOOTING_15_40("15cm x 40cm", 15, CommonLengthUnit.CENTIMETER, 40,
	    CommonLengthUnit.CENTIMETER),

    FOOTING_15_45("15cm x 45cm", 15, CommonLengthUnit.CENTIMETER, 45,
	    CommonLengthUnit.CENTIMETER),

    FOOTING_15_50("15cm x 50cm", 15, CommonLengthUnit.CENTIMETER, 50,
	    CommonLengthUnit.CENTIMETER),

    FOOTING_15_60("15cm x 60cm", 15, CommonLengthUnit.CENTIMETER, 60,
	    CommonLengthUnit.CENTIMETER);

    private String label;
    private double thickness;
    private CommonLengthUnit thickessUnit;
    private double width;
    private CommonLengthUnit widthUnit;

    TableCHBFootingDimensions(String label, double thickness,
	    CommonLengthUnit thickessUnit, double width,
	    CommonLengthUnit widthUnit) {
	this.label = label;
	this.thickness = thickness;
	this.thickessUnit = thickessUnit;
	this.width = width;
	this.widthUnit = widthUnit;
    }

}
