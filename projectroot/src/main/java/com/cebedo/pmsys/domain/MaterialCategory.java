package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class MaterialCategory implements IDomainObject {

    private static final long serialVersionUID = 1251896191084042909L;

    /**
     * Key: company.fk:%s:materialcategory:%s
     */
    private Company company;
    private UUID uuid;

    /**
     * Specs.
     */
    private String name;
    private Unit unit;

    /**
     * Bean-backed form.
     */
    private String unitKey;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public MaterialCategory(Company company2) {
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
	return String.format(RedisKeyRegistry.KEY_MATERIAL_CATEGORY,
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

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_MATERIAL_CATEGORY,
		company2.getId(), "*");
    }

    public String getUnitKey() {
	return unitKey;
    }

    public void setUnitKey(String unitKey) {
	this.unitKey = unitKey;
    }

}
