package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class ConcreteProportion implements IDomainObject {

    private static final long serialVersionUID = -2080215320887737760L;

    /**
     * Keys.
     */
    private Company company;
    private UUID uuid;

    /**
     * Specs.
     */
    private String name;
    private String description;

    private double ratioCement;
    private double ratioGravel;
    private double ratioSand;

    private double partCement40kg;
    private double partCement50kg;
    private double partSand;
    private double partGravel;

    private Unit unitCement40kgUnit;
    private Unit unitCement50kgUnit;
    private Unit unitSandUnit;
    private Unit unitGravelUnit;

    /**
     * Bean-backed form.
     */
    private String unitKeyCement40kgUnit;
    private String unitKeyCement50kgUnit;
    private String unitKeySandUnit;
    private String unitKeyGravelUnit;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public String getDisplayName() {
	return name + "  ( " + ratioCement + " : " + ratioSand + " : "
		+ ratioGravel + " )";
    }

    public ConcreteProportion() {
	;
    }

    public ConcreteProportion(Company company2) {
	setCompany(company2);
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
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

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_CONCRETE_PROPORTION,
		this.company.getId(), this.uuid);
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_CONCRETE_PROPORTION,
		company2.getId(), "*");
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

    public Unit getUnitCement40kgUnit() {
	return unitCement40kgUnit;
    }

    public void setUnitCement40kgUnit(Unit unitCement40kgUnit) {
	this.unitCement40kgUnit = unitCement40kgUnit;
    }

    public Unit getUnitCement50kgUnit() {
	return unitCement50kgUnit;
    }

    public void setUnitCement50kgUnit(Unit unitCement50kgUnit) {
	this.unitCement50kgUnit = unitCement50kgUnit;
    }

    public Unit getUnitSandUnit() {
	return unitSandUnit;
    }

    public void setUnitSandUnit(Unit unitSandUnit) {
	this.unitSandUnit = unitSandUnit;
    }

    public Unit getUnitGravelUnit() {
	return unitGravelUnit;
    }

    public void setUnitGravelUnit(Unit unitGravelUnit) {
	this.unitGravelUnit = unitGravelUnit;
    }

    public String getUnitKeyCement40kgUnit() {
	return unitKeyCement40kgUnit;
    }

    public void setUnitKeyCement40kgUnit(String unitKeyCement40kgUnit) {
	this.unitKeyCement40kgUnit = unitKeyCement40kgUnit;
    }

    public String getUnitKeyCement50kgUnit() {
	return unitKeyCement50kgUnit;
    }

    public void setUnitKeyCement50kgUnit(String unitKeyCement50kgUnit) {
	this.unitKeyCement50kgUnit = unitKeyCement50kgUnit;
    }

    public String getUnitKeySandUnit() {
	return unitKeySandUnit;
    }

    public void setUnitKeySandUnit(String unitKeySandUnit) {
	this.unitKeySandUnit = unitKeySandUnit;
    }

    public String getUnitKeyGravelUnit() {
	return unitKeyGravelUnit;
    }

    public void setUnitKeyGravelUnit(String unitKeyGravelUnit) {
	this.unitKeyGravelUnit = unitKeyGravelUnit;
    }

}
