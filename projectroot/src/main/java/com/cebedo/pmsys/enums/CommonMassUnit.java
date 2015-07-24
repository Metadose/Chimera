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

}
