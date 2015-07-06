package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.model.Company;

public class CHBFootingDimension implements IDomainObject {

    private static final long serialVersionUID = -7461097475087900128L;

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
     * Dimensions.
     */
    private double thickness;
    private double width;
    private CommonLengthUnit thicknessUnit;
    private CommonLengthUnit widthUnit;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public CHBFootingDimension() {
	;
    }

    public CHBFootingDimension(Company company2) {
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

    public double getThickness() {
	return thickness;
    }

    public void setThickness(double thickness) {
	this.thickness = thickness;
    }

    public double getWidth() {
	return width;
    }

    public void setWidth(double width) {
	this.width = width;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_CHB_FOOTING_DIMENSION,
		this.company.getId(), this.uuid);
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_CHB_FOOTING_DIMENSION,
		company2.getId(), "*");
    }

    public CommonLengthUnit getThicknessUnit() {
	return thicknessUnit;
    }

    public void setThicknessUnit(CommonLengthUnit thicknessUnit) {
	this.thicknessUnit = thicknessUnit;
    }

    public CommonLengthUnit getWidthUnit() {
	return widthUnit;
    }

    public void setWidthUnit(CommonLengthUnit widthUnit) {
	this.widthUnit = widthUnit;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof CHBFootingDimension ? ((CHBFootingDimension) obj)
		.getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
