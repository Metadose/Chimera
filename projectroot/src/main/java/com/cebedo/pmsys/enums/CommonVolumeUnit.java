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
}
