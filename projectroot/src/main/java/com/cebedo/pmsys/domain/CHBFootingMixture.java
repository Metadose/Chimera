package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class CHBFootingMixture implements IDomainObject {

    private static final long serialVersionUID = 7832409143057570305L;

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
     * Specs.
     */
    private CHBFootingDimension footingDimension;
    private ConcreteProportion concreteProportion;
    private double cement; // FIXME What is the unit? Assume 50kg bag.
    private double sand;
    private double gravel;

    /**
     * Bean-backed form.
     */
    private String chbFootingDimensionKey;
    private String concreteProportionKey;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public CHBFootingMixture() {
	;
    }

    public CHBFootingMixture(Company company2) {
	setCompany(company2);
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

    public CHBFootingDimension getFootingDimension() {
	return footingDimension;
    }

    public void setFootingDimension(CHBFootingDimension footingDimension) {
	this.footingDimension = footingDimension;
    }

    public ConcreteProportion getConcreteProportion() {
	return concreteProportion;
    }

    public void setConcreteProportion(ConcreteProportion concreteProportion) {
	this.concreteProportion = concreteProportion;
    }

    public double getCement() {
	return cement;
    }

    public void setCement(double cement) {
	this.cement = cement;
    }

    public double getSand() {
	return sand;
    }

    public void setSand(double sand) {
	this.sand = sand;
    }

    public double getGravel() {
	return gravel;
    }

    public void setGravel(double gravel) {
	this.gravel = gravel;
    }

    public String getChbFootingDimensionKey() {
	return chbFootingDimensionKey;
    }

    public void setChbFootingDimensionKey(String chbFootingDimensionKey) {
	this.chbFootingDimensionKey = chbFootingDimensionKey;
    }

    public String getConcreteProportionKey() {
	return concreteProportionKey;
    }

    public void setConcreteProportionKey(String concreteProportionKey) {
	this.concreteProportionKey = concreteProportionKey;
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_CHB_FOOTING_MIXTURE,
		this.company.getId(), this.uuid);
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_CHB_FOOTING_MIXTURE,
		company2.getId(), "*");
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof CHBFootingMixture ? ((CHBFootingMixture) obj)
		.getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
