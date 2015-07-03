package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class PlasterMixture implements IDomainObject {

    private static final long serialVersionUID = 420970587433399031L;

    /**
     * Key parts.
     */
    private Company company;
    private UUID uuid;

    /**
     * Basic details.
     */
    private String name;
    private String description;

    /**
     * Bean-backed form.
     */
    private String concreteProportionKey;

    /**
     * Specs.
     */
    private ConcreteProportion concreteProportion;
    private double proportionCement;
    private double proportionSand;
    private double cement40kg;
    private double cement50kg;
    private double sand;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public PlasterMixture() {
	;
    }

    public PlasterMixture(Company company2) {
	setCompany(company2);
    }

    public String getDisplayName() {
	return getName() + " (" + getProportionCement() + ":"
		+ getProportionSand() + ")";
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

    public String getConcreteProportionKey() {
	return concreteProportionKey;
    }

    public void setConcreteProportionKey(String concreteProportionKey) {
	this.concreteProportionKey = concreteProportionKey;
    }

    public ConcreteProportion getConcreteProportion() {
	return concreteProportion;
    }

    public void setConcreteProportion(ConcreteProportion concreteProportion) {
	this.concreteProportion = concreteProportion;
    }

    public double getProportionCement() {
	return proportionCement;
    }

    public void setProportionCement(double proportionCement) {
	this.proportionCement = proportionCement;
    }

    public double getProportionSand() {
	return proportionSand;
    }

    public void setProportionSand(double proportionSand) {
	this.proportionSand = proportionSand;
    }

    public double getCement40kg() {
	return cement40kg;
    }

    public void setCement40kg(double cement40kg) {
	this.cement40kg = cement40kg;
    }

    public double getCement50kg() {
	return cement50kg;
    }

    public void setCement50kg(double cement50kg) {
	this.cement50kg = cement50kg;
    }

    public double getSand() {
	return sand;
    }

    public void setSand(double sand) {
	this.sand = sand;
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_PLASTER_MIXTURE,
		this.company.getId(), this.uuid);
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

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_PLASTER_MIXTURE,
		company2.getId(), "*");
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof PlasterMixture ? ((PlasterMixture) obj).getKey()
		.equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
