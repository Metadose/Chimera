package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.CHBReinforcingBarsSpacing;
import com.cebedo.pmsys.model.Company;

public class CHBVerticalReinforcement implements IDomainObject {

    private static final long serialVersionUID = 5885689720165969988L;

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
    private CHBReinforcingBarsSpacing barsSpacing;
    private double barLengthPerSqm;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public CHBVerticalReinforcement() {
	;
    }

    public CHBVerticalReinforcement(Company company2) {
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
	return obj instanceof CHBVerticalReinforcement ? ((CHBVerticalReinforcement) obj)
		.getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_CHB_VERTICAL_REINFORCEMENT,
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

    public CHBReinforcingBarsSpacing getBarsSpacing() {
	return barsSpacing;
    }

    public void setBarsSpacing(CHBReinforcingBarsSpacing barsSpacing) {
	this.barsSpacing = barsSpacing;
    }

    public double getBarLengthPerSqm() {
	return barLengthPerSqm;
    }

    public void setBarLengthPerSqm(double barLengthPerSqm) {
	this.barLengthPerSqm = barLengthPerSqm;
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_CHB_VERTICAL_REINFORCEMENT,
		company2.getId(), "*");
    }
}
