package com.cebedo.pmsys.enums;

public enum TableDimensionCHBFooting {

    FOOTING_10_30("10cm x 30cm", 10, UnitCommonLength.CENTIMETER, 30,
	    UnitCommonLength.CENTIMETER),

    FOOTING_10_35("10cm x 35cm", 10, UnitCommonLength.CENTIMETER, 35,
	    UnitCommonLength.CENTIMETER),

    FOOTING_10_40("10cm x 40cm", 10, UnitCommonLength.CENTIMETER, 40,
	    UnitCommonLength.CENTIMETER),

    FOOTING_10_50("10cm x 50cm", 10, UnitCommonLength.CENTIMETER, 50,
	    UnitCommonLength.CENTIMETER),

    FOOTING_15_40("15cm x 40cm", 15, UnitCommonLength.CENTIMETER, 40,
	    UnitCommonLength.CENTIMETER),

    FOOTING_15_45("15cm x 45cm", 15, UnitCommonLength.CENTIMETER, 45,
	    UnitCommonLength.CENTIMETER),

    FOOTING_15_50("15cm x 50cm", 15, UnitCommonLength.CENTIMETER, 50,
	    UnitCommonLength.CENTIMETER),

    FOOTING_15_60("15cm x 60cm", 15, UnitCommonLength.CENTIMETER, 60,
	    UnitCommonLength.CENTIMETER);

    private String label;
    private double thickness;
    private UnitCommonLength thickessUnit;
    private double width;
    private UnitCommonLength widthUnit;

    TableDimensionCHBFooting(String label, double thickness,
	    UnitCommonLength thickessUnit, double width,
	    UnitCommonLength widthUnit) {
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

    public UnitCommonLength getThickessUnit() {
	return thickessUnit;
    }

    public void setThickessUnit(UnitCommonLength thickessUnit) {
	this.thickessUnit = thickessUnit;
    }

    public double getWidth() {
	return width;
    }

    public void setWidth(double width) {
	this.width = width;
    }

    public UnitCommonLength getWidthUnit() {
	return widthUnit;
    }

    public void setWidthUnit(UnitCommonLength widthUnit) {
	this.widthUnit = widthUnit;
    }

}
