package com.cebedo.pmsys.enums;

public enum UnitCommonMass {

    KILOGRAM(1, "Kilogram", "kg", 1),

    CEMENT_BAG_40KG(2, "Cement Bag (40kg)", "40kg Bag(s)", 40),

    CEMENT_BAG_50KG(3, "Cement Bag (50kg)", "50kg Bag(s)", 50),

    METRIC_TON(4, "Metric Ton", "Mg", 0.001),

    GRAM(5, "Gram", "g", 1000),

    MILLIGRAM(6, "Milligram", "mg", 0.000001),

    POUND(7, "Pound", "lbs", 2.20462);

    int id;
    String label;
    String symbol;
    double conversionToKilogram;

    UnitCommonMass(int id, String n, String s, double convert) {
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
