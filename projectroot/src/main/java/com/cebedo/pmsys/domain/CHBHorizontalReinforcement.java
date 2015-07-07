package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.CHBReinforcingBarsLayer;
import com.cebedo.pmsys.model.Company;

public class CHBHorizontalReinforcement implements IDomainObject {

    private static final long serialVersionUID = -7186577554455879897L;

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
    private CHBReinforcingBarsLayer barsLayer;
    private double barLengthPerSqm;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public CHBHorizontalReinforcement() {
	;
    }

    public CHBHorizontalReinforcement(Company company2) {
	setCompany(company2);
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof CHBHorizontalReinforcement ? ((CHBHorizontalReinforcement) obj)
		.getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_CHB_HORIZONTAL_REINFORCEMENT,
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

    public CHBReinforcingBarsLayer getBarsLayer() {
	return barsLayer;
    }

    public void setBarsLayer(CHBReinforcingBarsLayer barsLayer) {
	this.barsLayer = barsLayer;
    }

    public double getBarLengthPerSqm() {
	return barLengthPerSqm;
    }

    public void setBarLengthPerSqm(double barLengthPerSqm) {
	this.barLengthPerSqm = barLengthPerSqm;
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_CHB_HORIZONTAL_REINFORCEMENT,
		company2.getId(), "*");
    }
}
