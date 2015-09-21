package com.cebedo.pmsys.enums;

public enum CommonLengthUnit implements IUnitOfMeasure {

    METER(5, "Meter", "m", 1),

    MILLIMETER(0, "Millimeter", "mm", 0.001),

    CENTIMETER(1, "Centimeter", "cm", 0.01),

    INCH(2, "Inch", "in", 0.0254),

    FOOT(3, "Foot", "ft", 0.3048),

    YARD(4, "Yard", "yd", 0.9144),

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

	if (id == MILLIMETER.getId()) {
	    return MILLIMETER;
	} else if (id == CENTIMETER.getId()) {
	    return CENTIMETER;
	} else if (id == INCH.getId()) {
	    return INCH;
	} else if (id == FOOT.getId()) {
	    return FOOT;
	} else if (id == YARD.getId()) {
	    return YARD;
	} else if (id == METER.getId()) {
	    return METER;
	} else if (id == KILOMETER.getId()) {
	    return KILOMETER;
	} else if (id == MILE.getId()) {
	    return MILE;
	} else if (id == NAUTICAL_MILE.getId()) {
	    return NAUTICAL_MILE;
	}

	return METER;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public String getSymbol() {
	return symbol;
    }

    public void setSymbol(String symbol) {
	this.symbol = symbol;
    }

    public double getConversionToMeter() {
	return conversionToMeter;
    }

    public void setConversionToMeter(double conversionToMeter) {
	this.conversionToMeter = conversionToMeter;
    }

}