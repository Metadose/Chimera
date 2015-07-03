package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class Unit implements IDomainObject {

    private static final long serialVersionUID = -4796353524486144693L;

    /**
     * Key: company.fk:%s:units:%s
     */
    private Company company;
    private UUID uuid;

    /**
     * Specs.
     */
    private String name;
    private String details;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Unit() {
	;
    }

    public Unit(Company company2) {
	setCompany(company2);
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_UNITS, this.company.getId(),
		this.uuid);
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

    public String getDetails() {
	return details;
    }

    public void setDetails(String details) {
	this.details = details;
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_UNITS, company2.getId(), "*");
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Unit ? ((Unit) obj).getKey().equals(getKey())
		: false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
