package com.cebedo.pmsys.enums;

public enum TableCHBLayingMixture {

    CLASS_A_10_20_40("Class A (10cm x 20cm x 40cm)", "A", TableCHBDimensions.CHB_10_20_40, 0.792,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0435, CommonVolumeUnit.CUBIC_METER),

    CLASS_A_15_20_40("Class A (15cm x 20cm x 40cm)", "A", TableCHBDimensions.CHB_15_20_40, 1.526,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0844, CommonVolumeUnit.CUBIC_METER),

    CLASS_A_20_20_40("Class A (20cm x 20cm x 40cm)", "A", TableCHBDimensions.CHB_20_20_40, 2.260,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.1250, CommonVolumeUnit.CUBIC_METER),

    CLASS_B_10_20_40("Class B (10cm x 20cm x 40cm)", "B", TableCHBDimensions.CHB_10_20_40, 0.522,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0435, CommonVolumeUnit.CUBIC_METER),

    CLASS_B_15_20_40("Class B (15cm x 20cm x 40cm)", "B", TableCHBDimensions.CHB_15_20_40, 1.018,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0844, CommonVolumeUnit.CUBIC_METER),

    CLASS_B_20_20_40("Class B (20cm x 20cm x 40cm)", "B", TableCHBDimensions.CHB_20_20_40, 1.500,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.1250, CommonVolumeUnit.CUBIC_METER),

    CLASS_C_10_20_40("Class C (10cm x 20cm x 40cm)", "C", TableCHBDimensions.CHB_10_20_40, 0.394,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0435, CommonVolumeUnit.CUBIC_METER),

    CLASS_C_15_20_40("Class C (15cm x 20cm x 40cm)", "C", TableCHBDimensions.CHB_15_20_40, 0.763,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0844, CommonVolumeUnit.CUBIC_METER),

    CLASS_C_20_20_40("Class C (20cm x 20cm x 40cm)", "C", TableCHBDimensions.CHB_20_20_40, 1.125,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.1250, CommonVolumeUnit.CUBIC_METER),

    CLASS_D_10_20_40("Class D (10cm x 20cm x 40cm)", "D", TableCHBDimensions.CHB_10_20_40, 0.328,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0435, CommonVolumeUnit.CUBIC_METER),

    CLASS_D_15_20_40("Class D (15cm x 20cm x 40cm)", "D", TableCHBDimensions.CHB_15_20_40, 0.633,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0844, CommonVolumeUnit.CUBIC_METER),

    CLASS_D_20_20_40("Class D (20cm x 20cm x 40cm)", "D", TableCHBDimensions.CHB_20_20_40, 0.938,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.1250, CommonVolumeUnit.CUBIC_METER);

    private String label;
    private String mixClass;
    private TableCHBDimensions chb;
    private double partCement40kgBag;
    private CommonMassUnit partCement40kgBagUnit;
    private double partSand;
    private CommonVolumeUnit partSandUnit;

    TableCHBLayingMixture(String label, String mixClass, TableCHBDimensions chb,
	    double partCement40kgBag, CommonMassUnit partCement40kgBagUnit, double partSand,
	    CommonVolumeUnit partSandUnit) {
	this.mixClass = mixClass;
	this.label = label;
	this.chb = chb;
	this.partCement40kgBag = partCement40kgBag;
	this.partCement40kgBagUnit = partCement40kgBagUnit;
	this.partSand = partSand;
	this.partSandUnit = partSandUnit;
    }

    public String getMixClass() {
	return mixClass;
    }

    public void setMixClass(String mixClass) {
	this.mixClass = mixClass;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public TableCHBDimensions getChb() {
	return chb;
    }

    public void setChb(TableCHBDimensions chb) {
	this.chb = chb;
    }

    public double getPartCement40kgBag() {
	return partCement40kgBag;
    }

    public void setPartCement40kgBag(double partCement40kgBag) {
	this.partCement40kgBag = partCement40kgBag;
    }

    public CommonMassUnit getPartCement40kgBagUnit() {
	return partCement40kgBagUnit;
    }

    public void setPartCement40kgBagUnit(CommonMassUnit partCement40kgBagUnit) {
	this.partCement40kgBagUnit = partCement40kgBagUnit;
    }

    public double getPartSand() {
	return partSand;
    }

    public void setPartSand(double partSand) {
	this.partSand = partSand;
    }

    public CommonVolumeUnit getPartSandUnit() {
	return partSandUnit;
    }

    public void setPartSandUnit(CommonVolumeUnit partSandUnit) {
	this.partSandUnit = partSandUnit;
    }
}
