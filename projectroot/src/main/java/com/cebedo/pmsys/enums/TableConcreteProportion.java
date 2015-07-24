package com.cebedo.pmsys.enums;

public enum TableConcreteProportion {

    CLASS_AA("Class AA", 1, 1.5, 3, 12, CommonMassUnit.CEMENT_BAG_40KG, 9.5,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.5, CommonVolumeUnit.CUBIC_METER,
	    1.0, CommonVolumeUnit.CUBIC_METER),

    CLASS_A("Class A", 1, 2, 4, 9, CommonMassUnit.CEMENT_BAG_40KG, 7,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.5, CommonVolumeUnit.CUBIC_METER,
	    1.0, CommonVolumeUnit.CUBIC_METER),

    CLASS_B("Class B", 1, 2.5, 5, 7.5, CommonMassUnit.CEMENT_BAG_40KG, 6,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.5, CommonVolumeUnit.CUBIC_METER,
	    1.0, CommonVolumeUnit.CUBIC_METER),

    CLASS_C("Class C", 1, 3, 6, 6, CommonMassUnit.CEMENT_BAG_40KG, 5,
	    CommonMassUnit.CEMENT_BAG_50KG, 0.5, CommonVolumeUnit.CUBIC_METER,
	    1.0, CommonVolumeUnit.CUBIC_METER);

    private String label;
    private double ratioCement;
    private double ratioGravel;
    private double ratioSand;
    private double partCement40kg;
    private double partCement50kg;
    private double partSand;
    private double partGravel;
    private CommonMassUnit partCement40kgUnit;
    private CommonMassUnit partCement50kgUnit;
    private CommonVolumeUnit partSandUnit;
    private CommonVolumeUnit partGravelUnit;

    TableConcreteProportion(String label, double ratioCement,
	    double ratioGravel, double ratioSand, double partCement40kg,
	    CommonMassUnit partCement40kgUnit, double partCement50kg,
	    CommonMassUnit partCement50kgUnit, double partSand,
	    CommonVolumeUnit partSandUnit, double partGravel,
	    CommonVolumeUnit partGravelUnit) {

	this.label = label;
	this.ratioCement = ratioCement;
	this.ratioGravel = ratioGravel;
	this.ratioSand = ratioSand;
	this.partCement40kg = partCement40kg;
	this.partCement50kg = partCement50kg;
	this.partSand = partSand;
	this.partGravel = partGravel;
	this.partCement40kgUnit = partCement40kgUnit;
	this.partCement50kgUnit = partCement50kgUnit;
	this.partSandUnit = partSandUnit;
	this.partGravelUnit = partGravelUnit;
    }
}
