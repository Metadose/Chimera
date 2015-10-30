package com.cebedo.pmsys.enums;

public enum TableProportionConcrete {

    CLASS_AA("Class AA", "A", 1, 1.5, 3, 12, UnitCommonMass.CEMENT_BAG_40KG,
	    9.5, UnitCommonMass.CEMENT_BAG_50KG, 0.5,
	    UnitCommonVolume.CUBIC_METER, 1.0, UnitCommonVolume.CUBIC_METER),

    CLASS_A("Class A", "B", 1, 2, 4, 9, UnitCommonMass.CEMENT_BAG_40KG, 7,
	    UnitCommonMass.CEMENT_BAG_50KG, 0.5, UnitCommonVolume.CUBIC_METER,
	    1.0, UnitCommonVolume.CUBIC_METER),

    CLASS_B("Class B", "C", 1, 2.5, 5, 7.5, UnitCommonMass.CEMENT_BAG_40KG, 6,
	    UnitCommonMass.CEMENT_BAG_50KG, 0.5, UnitCommonVolume.CUBIC_METER,
	    1.0, UnitCommonVolume.CUBIC_METER),

    CLASS_C("Class C", "D", 1, 3, 6, 6, UnitCommonMass.CEMENT_BAG_40KG, 5,
	    UnitCommonMass.CEMENT_BAG_50KG, 0.5, UnitCommonVolume.CUBIC_METER,
	    1.0, UnitCommonVolume.CUBIC_METER);

    private String label;
    private String mixClass;
    private double ratioCement;
    private double ratioGravel;
    private double ratioSand;
    private double partCement40kg;
    private double partCement50kg;
    private double partSand;
    private double partGravel;
    private UnitCommonMass partCement40kgUnit;
    private UnitCommonMass partCement50kgUnit;
    private UnitCommonVolume partSandUnit;
    private UnitCommonVolume partGravelUnit;

    TableProportionConcrete(String label, String mixClass, double ratioCement,
	    double ratioGravel, double ratioSand, double partCement40kg,
	    UnitCommonMass partCement40kgUnit, double partCement50kg,
	    UnitCommonMass partCement50kgUnit, double partSand,
	    UnitCommonVolume partSandUnit, double partGravel,
	    UnitCommonVolume partGravelUnit) {

	this.label = label;
	this.mixClass = mixClass;
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

    public double getRatioCement() {
	return ratioCement;
    }

    public void setRatioCement(double ratioCement) {
	this.ratioCement = ratioCement;
    }

    public double getRatioGravel() {
	return ratioGravel;
    }

    public void setRatioGravel(double ratioGravel) {
	this.ratioGravel = ratioGravel;
    }

    public double getRatioSand() {
	return ratioSand;
    }

    public void setRatioSand(double ratioSand) {
	this.ratioSand = ratioSand;
    }

    public double getPartCement40kg() {
	return partCement40kg;
    }

    public void setPartCement40kg(double partCement40kg) {
	this.partCement40kg = partCement40kg;
    }

    public double getPartCement50kg() {
	return partCement50kg;
    }

    public void setPartCement50kg(double partCement50kg) {
	this.partCement50kg = partCement50kg;
    }

    public double getPartSand() {
	return partSand;
    }

    public void setPartSand(double partSand) {
	this.partSand = partSand;
    }

    public double getPartGravel() {
	return partGravel;
    }

    public void setPartGravel(double partGravel) {
	this.partGravel = partGravel;
    }

    public UnitCommonMass getPartCement40kgUnit() {
	return partCement40kgUnit;
    }

    public void setPartCement40kgUnit(UnitCommonMass partCement40kgUnit) {
	this.partCement40kgUnit = partCement40kgUnit;
    }

    public UnitCommonMass getPartCement50kgUnit() {
	return partCement50kgUnit;
    }

    public void setPartCement50kgUnit(UnitCommonMass partCement50kgUnit) {
	this.partCement50kgUnit = partCement50kgUnit;
    }

    public UnitCommonVolume getPartSandUnit() {
	return partSandUnit;
    }

    public void setPartSandUnit(UnitCommonVolume partSandUnit) {
	this.partSandUnit = partSandUnit;
    }

    public UnitCommonVolume getPartGravelUnit() {
	return partGravelUnit;
    }

    public void setPartGravelUnit(UnitCommonVolume partGravelUnit) {
	this.partGravelUnit = partGravelUnit;
    }
}
