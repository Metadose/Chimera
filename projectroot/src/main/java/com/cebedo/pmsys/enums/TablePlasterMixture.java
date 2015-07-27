package com.cebedo.pmsys.enums;

public enum TablePlasterMixture {

    CLASS_A_10_20_40("Class A (10 x 20 x 40)", TableCHB.CHB_10_20_40, 0.792,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0435,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_B_10_20_40("Class B (10 x 20 x 40)", TableCHB.CHB_10_20_40, 0.522,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0435,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_C_10_20_40("Class C (10 x 20 x 40)", TableCHB.CHB_10_20_40, 0.394,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0435,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_D_10_20_40("Class D (10 x 20 x 40)", TableCHB.CHB_10_20_40, 0.328,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0435,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_A_15_20_40("Class A (15 x 20 x 40)", TableCHB.CHB_15_20_40, 1.526,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0844,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_B_15_20_40("Class B (15 x 20 x 40)", TableCHB.CHB_15_20_40, 1.018,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0844,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_C_15_20_40("Class C (15 x 20 x 40)", TableCHB.CHB_15_20_40, 0.763,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0844,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_D_15_20_40("Class D (15 x 20 x 40)", TableCHB.CHB_15_20_40, 0.633,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.0844,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_A_20_20_40("Class A (20 x 20 x 40)", TableCHB.CHB_20_20_40, 2.260,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.1250,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_B_20_20_40("Class B (20 x 20 x 40)", TableCHB.CHB_20_20_40, 1.500,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.1250,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_C_20_20_40("Class C (20 x 20 x 40)", TableCHB.CHB_20_20_40, 1.125,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.1250,
	    CommonVolumeUnit.CUBIC_METER),

    CLASS_D_20_20_40("Class D (20 x 20 x 40)", TableCHB.CHB_20_20_40, 0.938,
	    CommonMassUnit.CEMENT_BAG_40KG, 0.1250,
	    CommonVolumeUnit.CUBIC_METER);

    private String label;
    private TableCHB chb;
    private double partCement40kgBag;
    private CommonMassUnit partCement40kgBagUnit;
    private double partSand;
    private CommonVolumeUnit partSandUnit;

    TablePlasterMixture(String label, TableCHB chb, double partCement40kgBag,
	    CommonMassUnit partCement40kgBagUnit, double partSand,
	    CommonVolumeUnit partSandUnit) {
	this.label = label;
	this.chb = chb;
	this.partCement40kgBag = partCement40kgBag;
	this.partCement40kgBagUnit = partCement40kgBagUnit;
	this.partSand = partSand;
	this.partSandUnit = partSandUnit;
    }

}
