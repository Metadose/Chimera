package com.cebedo.pmsys.enums;

public enum TableMRCHBTieWire {

    GI_40_2_40CM(TableMRCHBVertical.CM_40.spacing(), TableMRCHBHorizontal.LAYER_2.layer(), 0.086);

    public static final TableMRCHBTieWire SAFEST = GI_40_2_40CM;

    private double verticalSpacing;
    private double horizontalLayer;
    private double kgPerSqMeter;

    TableMRCHBTieWire(double vertSpace, double horizLayer, double kg) {
	setVerticalSpacing(vertSpace);
	setHorizontalLayer(horizLayer);
	setKgPerSqMeter(kg);
    }

    public double getVerticalSpacing() {
	return verticalSpacing;
    }

    public void setVerticalSpacing(double verticalSpacing) {
	this.verticalSpacing = verticalSpacing;
    }

    public double getHorizontalLayer() {
	return horizontalLayer;
    }

    public void setHorizontalLayer(double horizontalLayer) {
	this.horizontalLayer = horizontalLayer;
    }

    public double getKgPerSqMeter() {
	return kgPerSqMeter;
    }

    public void setKgPerSqMeter(double kgPerSqMeter) {
	this.kgPerSqMeter = kgPerSqMeter;
    }

}
