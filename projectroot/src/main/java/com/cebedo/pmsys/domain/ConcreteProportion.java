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

    private Unit unitCement40kg;
    private Unit unitCement50kg;
    private Unit unitSand;
    private Unit unitGravel;

    /**
     * Bean-backed form.
     */
    private String unitKeyCement40kg;
    private String unitKeyCement50kg;
    private String unitKeySand;
    private String unitKeyGravel;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public String getDisplayName() {
	return name + " (" + ratioCement + ":" + ratioSand + ":" + ratioGravel
		+ ")";
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

    public Unit getUnitCement40kg() {
	return unitCement40kg;
    }

    public void setUnitCement40kg(Unit u) {
	this.unitCement40kg = u;
    }

    public Unit getUnitCement50kg() {
	return unitCement50kg;
    }

    public void setUnitCement50kg(Unit u) {
	this.unitCement50kg = u;
    }

    public Unit getUnitSand() {
	return unitSand;
    }

    public void setUnitSand(Unit u) {
	this.unitSand = u;
    }

    public Unit getUnitGravel() {
	return unitGravel;
    }

    public void setUnitGravel(Unit u) {
	this.unitGravel = u;
    }

    public String getUnitKeyCement40kg() {
	return unitKeyCement40kg;
    }

    public void setUnitKeyCement40kg(String u) {
	this.unitKeyCement40kg = u;
    }

    public String getUnitKeyCement50kg() {
	return unitKeyCement50kg;
    }

    public void setUnitKeyCement50kg(String u) {
	this.unitKeyCement50kg = u;
    }

    public String getUnitKeySand() {
	return unitKeySand;
    }

    public void setUnitKeySand(String u) {
	this.unitKeySand = u;
    }

    public String getUnitKeyGravel() {
	return unitKeyGravel;
    }

    public void setUnitKeyGravel(String u) {
	this.unitKeyGravel = u;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof ConcreteProportion ? ((ConcreteProportion) obj)
		.getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
