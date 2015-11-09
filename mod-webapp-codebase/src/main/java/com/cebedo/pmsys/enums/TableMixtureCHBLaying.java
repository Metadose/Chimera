package com.cebedo.pmsys.enums;

public enum TableMixtureCHBLaying {

    CLASS_A_10_20_40("Class A (10cm x 20cm x 40cm)", "A", TableDimensionCHB.CHB_10_20_40, 0.792,
	    UnitMass.CEMENT_BAG_40KG, 0.0435, UnitVolume.CUBIC_METER),

    CLASS_A_15_20_40("Class A (15cm x 20cm x 40cm)", "A", TableDimensionCHB.CHB_15_20_40, 1.526,
	    UnitMass.CEMENT_BAG_40KG, 0.0844, UnitVolume.CUBIC_METER),

    CLASS_A_20_20_40("Class A (20cm x 20cm x 40cm)", "A", TableDimensionCHB.CHB_20_20_40, 2.260,
	    UnitMass.CEMENT_BAG_40KG, 0.1250, UnitVolume.CUBIC_METER),

    CLASS_B_10_20_40("Class B (10cm x 20cm x 40cm)", "B", TableDimensionCHB.CHB_10_20_40, 0.522,
	    UnitMass.CEMENT_BAG_40KG, 0.0435, UnitVolume.CUBIC_METER),

    CLASS_B_15_20_40("Class B (15cm x 20cm x 40cm)", "B", TableDimensionCHB.CHB_15_20_40, 1.018,
	    UnitMass.CEMENT_BAG_40KG, 0.0844, UnitVolume.CUBIC_METER),

    CLASS_B_20_20_40("Class B (20cm x 20cm x 40cm)", "B", TableDimensionCHB.CHB_20_20_40, 1.500,
	    UnitMass.CEMENT_BAG_40KG, 0.1250, UnitVolume.CUBIC_METER),

    CLASS_C_10_20_40("Class C (10cm x 20cm x 40cm)", "C", TableDimensionCHB.CHB_10_20_40, 0.394,
	    UnitMass.CEMENT_BAG_40KG, 0.0435, UnitVolume.CUBIC_METER),

    CLASS_C_15_20_40("Class C (15cm x 20cm x 40cm)", "C", TableDimensionCHB.CHB_15_20_40, 0.763,
	    UnitMass.CEMENT_BAG_40KG, 0.0844, UnitVolume.CUBIC_METER),

    CLASS_C_20_20_40("Class C (20cm x 20cm x 40cm)", "C", TableDimensionCHB.CHB_20_20_40, 1.125,
	    UnitMass.CEMENT_BAG_40KG, 0.1250, UnitVolume.CUBIC_METER),

    CLASS_D_10_20_40("Class D (10cm x 20cm x 40cm)", "D", TableDimensionCHB.CHB_10_20_40, 0.328,
	    UnitMass.CEMENT_BAG_40KG, 0.0435, UnitVolume.CUBIC_METER),

    CLASS_D_15_20_40("Class D (15cm x 20cm x 40cm)", "D", TableDimensionCHB.CHB_15_20_40, 0.633,
	    UnitMass.CEMENT_BAG_40KG, 0.0844, UnitVolume.CUBIC_METER),

    CLASS_D_20_20_40("Class D (20cm x 20cm x 40cm)", "D", TableDimensionCHB.CHB_20_20_40, 0.938,
	    UnitMass.CEMENT_BAG_40KG, 0.1250, UnitVolume.CUBIC_METER);

    private String label;
    private String mixClass;
    private TableDimensionCHB chb;
    private double partCement40kgBag;
    private UnitMass partCement40kgBagUnit;
    private double partSand;
    private UnitVolume partSandUnit;

    TableMixtureCHBLaying(String label, String mixClass, TableDimensionCHB chb,
	    double partCement40kgBag, UnitMass partCement40kgBagUnit, double partSand,
	    UnitVolume partSandUnit) {
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

    public TableDimensionCHB getChb() {
	return chb;
    }

    public void setChb(TableDimensionCHB chb) {
	this.chb = chb;
    }

    public double getPartCement40kgBag() {
	return partCement40kgBag;
    }

    public void setPartCement40kgBag(double partCement40kgBag) {
	this.partCement40kgBag = partCement40kgBag;
    }

    public UnitMass getPartCement40kgBagUnit() {
	return partCement40kgBagUnit;
    }

    public void setPartCement40kgBagUnit(UnitMass partCement40kgBagUnit) {
	this.partCement40kgBagUnit = partCement40kgBagUnit;
    }

    public double getPartSand() {
	return partSand;
    }

    public void setPartSand(double partSand) {
	this.partSand = partSand;
    }

    public UnitVolume getPartSandUnit() {
	return partSandUnit;
    }

    public void setPartSandUnit(UnitVolume partSandUnit) {
	this.partSandUnit = partSandUnit;
    }
}
