package com.cebedo.pmsys.enums;

public enum CommonWeightUnit {

    KILOGRAM(1, "Kilogram", "kg", 1);

    int id;
    String label;
    String symbol;
    double conversionToKilogram;

    CommonWeightUnit(int id, String n, String s, double convert) {
	this.id = id;
	this.label = n;
	this.symbol = s;
	this.conversionToKilogram = convert;
    }

}
