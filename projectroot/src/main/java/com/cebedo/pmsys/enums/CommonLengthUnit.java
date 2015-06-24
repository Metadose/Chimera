package com.cebedo.pmsys.enums;

public enum CommonLengthUnit {

    MILLIMETER(0, "Millimeter", "mm", 0.001),

    CENTIMETER(1, "Centimeter", "cm", 0.01),

    INCH(2, "Inch", "in", 0.0254),

    FOOT(3, "Foot", "ft", 0.3048),

    YARD(4, "Yard", "yd", 0.9144),

    METER(5, "Meter", "m", 1),

    KILOMETER(6, "Kilometer", "km", 1000),

    MILE(7, "Mile", "mi", 1609.34),

    NAUTICAL_MILE(8, "Nautical Mile", "nmi", 1852);

    int id;
    String label;
    String symbol;
    double conversionToMeter;

    CommonLengthUnit(int id, String n, String s, double convert) {
	this.id = id;
	this.label = n;
	this.symbol = s;
	this.conversionToMeter = convert;
    }

    public static CommonLengthUnit of(int id) {

	if (id == MILLIMETER.id()) {
	    return MILLIMETER;
	} else if (id == CENTIMETER.id()) {
	    return CENTIMETER;
	} else if (id == INCH.id()) {
	    return INCH;
	} else if (id == FOOT.id()) {
	    return FOOT;
	} else if (id == YARD.id()) {
	    return YARD;
	} else if (id == METER.id()) {
	    return METER;
	} else if (id == KILOMETER.id()) {
	    return KILOMETER;
	} else if (id == MILE.id()) {
	    return MILE;
	} else if (id == NAUTICAL_MILE.id()) {
	    return NAUTICAL_MILE;
	}

	return METER;
    }

    public int id() {
	return this.id;
    }

    public String label() {
	return this.label;
    }

    public String symbol() {
	return this.symbol;
    }

    public double conversionToMeter() {
	return this.conversionToMeter;
    }

}