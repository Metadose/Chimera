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

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public double getThickness() {
	return thickness;
    }

    public void setThickness(double thickness) {
	this.thickness = thickness;
    }

    public CommonLengthUnit getThickessUnit() {
	return thickessUnit;
    }

    public void setThickessUnit(CommonLengthUnit thickessUnit) {
	this.thickessUnit = thickessUnit;
    }

    public double getWidth() {
	return width;
    }

    public void setWidth(double width) {
	this.width = width;
    }

    public CommonLengthUnit getWidthUnit() {
	return widthUnit;
    }

    public void setWidthUnit(CommonLengthUnit widthUnit) {
	this.widthUnit = widthUnit;
    }

}
