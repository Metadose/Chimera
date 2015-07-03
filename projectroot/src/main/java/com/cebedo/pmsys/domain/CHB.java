package com.cebedo.pmsys.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.model.Company;
import com.udojava.evalex.Expression;

public class CHB implements IDomainObject {

    private static final long serialVersionUID = 1721363028074669984L;

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
    private double thickness;
    private double height;
    private double length;

    private CommonLengthUnit thicknessUnit;
    private CommonLengthUnit heightUnit;
    private CommonLengthUnit lengthUnit;

    public CHB(Company company2) {
	setCompany(company2);
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public String getDisplayName() {
	return this.name + " (" + getPerSqM() + " CHB/sq.m.)";
    }

    /**
     * Number of CHB for every 1 square meter. TODO Cache this function.
     * 
     * @return
     */
    public double getPerSqM() {
	Expression mathExp = new Expression("1 / (length * height)");

	// Convert length and
	double lengthValue = this.length;
	if (this.lengthUnit != CommonLengthUnit.METER) {
	    double meterConvert = this.lengthUnit.conversionToMeter();
	    lengthValue = meterConvert * this.length;
	}
	String lengthAsStr = lengthValue + "";

	// height to meters.
	double heightValue = this.height;
	if (this.heightUnit != CommonLengthUnit.METER) {
	    double meterConvert = this.heightUnit.conversionToMeter();
	    heightValue = meterConvert * this.height;
	}
	String heightAsStr = heightValue + "";

	// Replace math expression variables.
	mathExp = mathExp.with("length", lengthAsStr)
		.and("height", heightAsStr);

	// Compute.
	BigDecimal result = mathExp.eval();
	return result.doubleValue();
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

    public CommonLengthUnit getThicknessUnit() {
	return thicknessUnit;
    }

    public void setThicknessUnit(CommonLengthUnit thicknessUnit) {
	this.thicknessUnit = thicknessUnit;
    }

    public CommonLengthUnit getHeightUnit() {
	return heightUnit;
    }

    public void setHeightUnit(CommonLengthUnit heightUnit) {
	this.heightUnit = heightUnit;
    }

    public CommonLengthUnit getLengthUnit() {
	return lengthUnit;
    }

    public void setLengthUnit(CommonLengthUnit lengthUnit) {
	this.lengthUnit = lengthUnit;
    }

    public double getHeight() {
	return height;
    }

    public void setHeight(double height) {
	this.height = height;
    }

    public double getLength() {
	return length;
    }

    public void setLength(double length) {
	this.length = length;
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
	return String.format(RedisKeyRegistry.KEY_CHB, this.company.getId(),
		this.uuid);
    }

    public static String constructPattern(Company company2) {
	return String.format(RedisKeyRegistry.KEY_CHB, company2.getId(), "*");
    }

    /**
     * Get the block laying mix for this CHB.
     * 
     * @param mixList
     * @param proportion
     * @return
     */
    public BlockLayingMixture getBlockLayingMixture(
	    List<BlockLayingMixture> mixList, ConcreteProportion proportion) {

	String proportionKey = proportion.getKey();
	String chbKey = getKey();
	for (BlockLayingMixture mix : mixList) {

	    // If we have found the mix for this CHB, then use it.
	    String mixCHBKey = mix.getChbMeasurement().getKey();
	    if (chbKey.equals(mixCHBKey)
		    && mix.getConcreteProportionKey().equals(proportionKey)) {
		return mix;
	    }
	}
	return null;
    }

    // TODO Override the others.
    @Override
    public boolean equals(Object obj) {
	return obj instanceof CHB ? ((CHB) obj).getKey().equals(getKey())
		: false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }
}
