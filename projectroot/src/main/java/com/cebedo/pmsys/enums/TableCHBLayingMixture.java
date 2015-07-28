package com.cebedo.pmsys.enums;

public enum TableCHBLayingMixture {

    CLASS_A_10_20_40("Class A (10cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_10_20_40, 0.792,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.0435,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_A_15_20_40("Class A (15cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_15_20_40, 1.526,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.0844,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_A_20_20_40("Class A (20cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_20_20_40, 2.260,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.1250,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_B_10_20_40("Class B (10cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_10_20_40, 0.522,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.0435,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_B_15_20_40("Class B (15cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_15_20_40, 1.018,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.0844,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_B_20_20_40("Class B (20cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_20_20_40, 1.500,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.1250,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_C_10_20_40("Class C (10cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_10_20_40, 0.394,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.0435,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_C_15_20_40("Class C (15cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_15_20_40, 0.763,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.0844,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_C_20_20_40("Class C (20cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_20_20_40, 1.125,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.1250,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_D_10_20_40("Class D (10cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_10_20_40, 0.328,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.0435,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_D_15_20_40("Class D (15cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_15_20_40, 0.633,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.0844,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_D_20_20_40("Class D (20cm x 20cm x 40cm)",
	    TableCHBDimensions.CHB_20_20_40, 0.938,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.1250,
	    CommonVolumeUnit.CUBIC_METER);

    private String label;
    private TableCHBDimensions chb;
    private double partCement40kgBag;
    private CommonMassUnit partCement40kgBagUnit;
    private double partSand;
    private CommonVolumeUnit partSandUnit;

    TableCHBLayingMixture(String label, TableCHBDimensions chb,
	    double partCement40kgBag, CommonMassUnit partCement40kgBagUnit,
	    double partSand, CommonVolumeUnit partSandUnit) {
	this.label = label;
	this.chb = chb;
	this.partCement40kgBag = partCement40kgBag;
	this.partCement40kgBagUnit = partCement40kgBagUnit;
	this.partSand = partSand;
	this.partSandUnit = partSandUnit;
    }
}
