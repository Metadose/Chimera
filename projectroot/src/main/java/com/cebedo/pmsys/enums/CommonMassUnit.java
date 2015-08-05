package com.cebedo.pmsys.enums;

public enum CommonMassUnit {

    KILOGRAM(1, "Kilogram", "kg", 1),

    CEMENT_BAG_40KG(2, "Cement Bag (40kg)", "40kg Bag", 40),

    CEMENT_BAG_50KG(3, "Cement Bag (50kg)", "50kg Bag", 50);

    int id;
    String label;
    String symbol;
    double conversionToKilogram;

    CommonMassUnit(int id, String n, String s, double convert) {
	this.id = id;
	this.label = n;
	this.symbol = s;
	this.conversionToKilogram = convert;
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

    public double getConversionToKilogram() {
	return conversionToKilogram;
    }

    public void setConversionToKilogram(double conversionToKilogram) {
	this.conversionToKilogram = conversionToKilogram;
    }

}
