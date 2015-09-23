package com.cebedo.pmsys.enums;

public enum CommonVolumeUnit {

    CUBIC_METER(1, "Cubic Meter", "cu. m.", 1),

    CUBIC_FOOT(2, "Cubic Foot", "cu. ft.", 35.3147),

    CUBIC_INCH(3, "Cubic Inch", "cu. in.", 61023.7),

    LITER(4, "Liter", "L", 1000),

    MILLILITER(5, "Milliliter", "mL", 0.000001),

    IMPERIAL_GAL(6, "Imperial Gallon", "(UK) gal", 219.969),

    US_GAL(7, "US Gallon", "(US) gal", 264.172);

    int id;
    String label;
    String symbol;
    double conversionToCuM;

    CommonVolumeUnit(int id, String n, String s, double convert) {
	this.id = id;
	this.label = n;
	this.symbol = s;
	this.conversionToCuM = convert;
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

    public double getConversionToCuM() {
	return conversionToCuM;
    }

    public void setConversionToCuM(double conversionToCuM) {
	this.conversionToCuM = conversionToCuM;
    }
}
