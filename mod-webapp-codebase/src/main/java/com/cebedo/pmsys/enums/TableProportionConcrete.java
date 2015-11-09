package com.cebedo.pmsys.enums;

public enum TableProportionConcrete {

    CLASS_AA("Class AA", "A", 1, 1.5, 3, 12, UnitMass.CEMENT_BAG_40KG,
	    9.5, UnitMass.CEMENT_BAG_50KG, 0.5,
	    UnitVolume.CUBIC_METER, 1.0, UnitVolume.CUBIC_METER),

    CLASS_A("Class A", "B", 1, 2, 4, 9, UnitMass.CEMENT_BAG_40KG, 7,
	    UnitMass.CEMENT_BAG_50KG, 0.5, UnitVolume.CUBIC_METER,
	    1.0, UnitVolume.CUBIC_METER),

    CLASS_B("Class B", "C", 1, 2.5, 5, 7.5, UnitMass.CEMENT_BAG_40KG, 6,
	    UnitMass.CEMENT_BAG_50KG, 0.5, UnitVolume.CUBIC_METER,
	    1.0, UnitVolume.CUBIC_METER),

    CLASS_C("Class C", "D", 1, 3, 6, 6, UnitMass.CEMENT_BAG_40KG, 5,
	    UnitMass.CEMENT_BAG_50KG, 0.5, UnitVolume.CUBIC_METER,
	    1.0, UnitVolume.CUBIC_METER);

    private String label;
    private String mixClass;
    private double ratioCement;
    private double ratioGravel;
    private double ratioSand;
    private double partCement40kg;
    private double partCement50kg;
    private double partSand;
    private double partGravel;
    private UnitMass partCement40kgUnit;
    private UnitMass partCement50kgUnit;
    private UnitVolume partSandUnit;
    private UnitVolume partGravelUnit;

    TableProportionConcrete(String label, String mixClass, double ratioCement,
	    double ratioGravel, double ratioSand, double partCement40kg,
	    UnitMass partCement40kgUnit, double partCement50kg,
	    UnitMass partCement50kgUnit, double partSand,
	    UnitVolume partSandUnit, double partGravel,
	    UnitVolume partGravelUnit) {

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

    public UnitMass getPartCement40kgUnit() {
	return partCement40kgUnit;
    }

    public void setPartCement40kgUnit(UnitMass partCement40kgUnit) {
	this.partCement40kgUnit = partCement40kgUnit;
    }

    public UnitMass getPartCement50kgUnit() {
	return partCement50kgUnit;
    }

    public void setPartCement50kgUnit(UnitMass partCement50kgUnit) {
	this.partCement50kgUnit = partCement50kgUnit;
    }

    public UnitVolume getPartSandUnit() {
	return partSandUnit;
    }

    public void setPartSandUnit(UnitVolume partSandUnit) {
	this.partSandUnit = partSandUnit;
    }

    public UnitVolume getPartGravelUnit() {
	return partGravelUnit;
    }

    public void setPartGravelUnit(UnitVolume partGravelUnit) {
	this.partGravelUnit = partGravelUnit;
    }
}
