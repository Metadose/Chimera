package com.cebedo.pmsys.enums;

public enum TableDimensionCHBFooting {

    FOOTING_10_30("10cm x 30cm", 10, UnitLength.CENTIMETER, 30,
	    UnitLength.CENTIMETER),

    FOOTING_10_35("10cm x 35cm", 10, UnitLength.CENTIMETER, 35,
	    UnitLength.CENTIMETER),

    FOOTING_10_40("10cm x 40cm", 10, UnitLength.CENTIMETER, 40,
	    UnitLength.CENTIMETER),

    FOOTING_10_50("10cm x 50cm", 10, UnitLength.CENTIMETER, 50,
	    UnitLength.CENTIMETER),

    FOOTING_15_40("15cm x 40cm", 15, UnitLength.CENTIMETER, 40,
	    UnitLength.CENTIMETER),

    FOOTING_15_45("15cm x 45cm", 15, UnitLength.CENTIMETER, 45,
	    UnitLength.CENTIMETER),

    FOOTING_15_50("15cm x 50cm", 15, UnitLength.CENTIMETER, 50,
	    UnitLength.CENTIMETER),

    FOOTING_15_60("15cm x 60cm", 15, UnitLength.CENTIMETER, 60,
	    UnitLength.CENTIMETER);

    private String label;
    private double thickness;
    private UnitLength thickessUnit;
    private double width;
    private UnitLength widthUnit;

    TableDimensionCHBFooting(String label, double thickness,
	    UnitLength thickessUnit, double width,
	    UnitLength widthUnit) {
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

    public UnitLength getThickessUnit() {
	return thickessUnit;
    }

    public void setThickessUnit(UnitLength thickessUnit) {
	this.thickessUnit = thickessUnit;
    }

    public double getWidth() {
	return width;
    }

    public void setWidth(double width) {
	this.width = width;
    }

    public UnitLength getWidthUnit() {
	return widthUnit;
    }

    public void setWidthUnit(UnitLength widthUnit) {
	this.widthUnit = widthUnit;
    }

}
