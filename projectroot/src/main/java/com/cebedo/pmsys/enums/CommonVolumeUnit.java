package com.cebedo.pmsys.enums;

public enum CommonVolumeUnit {

    CUBIC_METER(1, "Cubic Meter", "cu. m.", 1);

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
