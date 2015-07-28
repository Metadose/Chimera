package com.cebedo.pmsys.enums;

public enum TablePlasterMixture {

    CLASS_A("Class A", 1, 2, 18.0, CommonMassUnit.CEMENT_BAG_40KG, 14.5,
	    CommonMassUnit.CEMENT_BAG_50KG, 1.0, CommonVolumeUnit.CUBIC_METER),

    CLASS_B("Class B", 1, 3, 12.0, CommonMassUnit.CEMENT_BAG_40KG, 9.5,
	    CommonMassUnit.CEMENT_BAG_50KG, 1.0, CommonVolumeUnit.CUBIC_METER),

    CLASS_C("Class C", 1, 4, 9.0, CommonMassUnit.CEMENT_BAG_40KG, 7.0,
	    CommonMassUnit.CEMENT_BAG_50KG, 1.0, CommonVolumeUnit.CUBIC_METER),

    CLASS_D("Class D", 1, 5, 7.5, CommonMassUnit.CEMENT_BAG_40KG, 6.0,
	    CommonMassUnit.CEMENT_BAG_50KG, 1.0, CommonVolumeUnit.CUBIC_METER);

    private String label;
    private double ratioCement;
    private double ratioSand;
    private double partCement40kg;
    private CommonMassUnit partCement40kgUnit;
    private double partCement50kg;
    private CommonMassUnit partCement50kgUnit;
    private double partSand;
    private CommonVolumeUnit partSandUnit;

    TablePlasterMixture(String label, double ratioCement, double ratioSand,
	    double partCement40kg, CommonMassUnit partCement40kgUnit,
	    double partCement50kg, CommonMassUnit partCement50kgUnit,
	    double partSand, CommonVolumeUnit partSandUnit) {
	this.label = label;
	this.ratioCement = ratioCement;
	this.ratioSand = ratioSand;
	this.partCement40kg = partCement40kg;
	this.partCement40kgUnit = partCement40kgUnit;
	this.partCement50kg = partCement50kg;
	this.partCement50kgUnit = partCement50kgUnit;
	this.partSand = partSand;
	this.partSandUnit = partSandUnit;
    }

}
