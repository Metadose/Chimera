package com.cebedo.pmsys.enums;

public enum TableMRCHBHorizontal {

    LAYER_2(2.0, 3.30),

    LAYER_3(3.0, 2.15),

    LAYER_4(4.0, 1.72);

    public static final TableMRCHBHorizontal SAFEST = LAYER_2;

    private double layer;
    private double perSqMeter;

    TableMRCHBHorizontal(double layr, double perSqM) {
	this.layer = layr;
	this.perSqMeter = perSqM;
    }

    public double layer() {
	return this.layer;
    }

    public double getPerSqMeter() {
	return perSqMeter;
    }

    public void setPerSqMeter(double perSqMeter) {
	this.perSqMeter = perSqMeter;
    }

}
