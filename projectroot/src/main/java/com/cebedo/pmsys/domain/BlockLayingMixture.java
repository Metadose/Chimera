package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class BlockLayingMixture implements IDomainObject {

    private static final long serialVersionUID = -3391223718773903102L;

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
     * Inputs.
     */
    private double cementBags; // TODO Not sure if this is 40kg or 50kg bag?
    private double sand; // In cubic meters.

    private CHB chbMeasurement; // Generated using the key from form.
    private ConcreteProportion concreteProportion; // Generated using the key
						   // from form.

    /**
     * Bean-backed form.
     */
    private String chbKey;
    private String concreteProportionKey;

    public BlockLayingMixture() {
	;
    }

    public BlockLayingMixture(Company company2) {
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

    public double getCementBags() {
	return cementBags;
    }

    public void setCementBags(double cementBags) {
	this.cementBags = cementBags;
    }

    public double getSand() {
	return sand;
    }

    public void setSand(double sand) {
	this.sand = sand;
    }

    public CHB getChbMeasurement() {
	return chbMeasurement;
    }

    public void setChbMeasurement(CHB chbMeasurement) {
	this.chbMeasurement = chbMeasurement;
    }

    public String getChbKey() {
	return chbKey;
    }

    public void setChbKey(String chbKey) {
	this.chbKey = chbKey;
    }

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_BLOCK_LAYING_MIXTURE,
		this.company.getId(), this.uuid);
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_BLOCK_LAYING_MIXTURE,
		company2.getId(), "*");
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

    @Override
    public boolean equals(Object obj) {
	return obj instanceof BlockLayingMixture ? ((BlockLayingMixture) obj)
		.getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
