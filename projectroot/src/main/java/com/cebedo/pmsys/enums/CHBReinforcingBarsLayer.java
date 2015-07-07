package com.cebedo.pmsys.enums;

public enum CHBReinforcingBarsLayer {

    LAYER_2(2.0),

    LAYER_3(3.0),

    LAYER_4(4.0);

    private double layer;

    CHBReinforcingBarsLayer(double layr) {
	this.layer = layr;
    }

    public double layer() {
	return this.layer;
    }

}
