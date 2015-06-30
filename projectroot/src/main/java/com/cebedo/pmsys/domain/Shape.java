package com.cebedo.pmsys.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class Shape implements IDomainObject {

    private static final long serialVersionUID = 1604488532064947576L;
    public static final String DELIMITER_OPEN_VARIABLE = "[[";
    public static final String DELIMITER_CLOSE_VARIABLE = "]]";

    /**
     * Keys.
     */
    private Company company;
    private UUID uuid;

    /**
     * Fields.
     */
    private String name;
    private String description;

    /**
     * Specs.
     */
    private String areaFormula;
    private String volumeFormula;
    private List<String> areaVariableNames;
    private List<String> volumeVariableNames;
    private String[] formulaInputs;

    /**
     * Computed.
     */
    private double area;
    private double volume;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Shape() {
	;
    }

    public Shape(Company company2, UUID uuid2) {
	setCompany(company2);
	setUuid(uuid2);
    }

    public Shape(Company company2) {
	setCompany(company2);
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    public static String constructPattern(Company company) {
	return String.format(RedisKeyRegistry.KEY_SHAPE, company.getId(), "*");
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_SHAPE, this.company.getId(),
		this.uuid);
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

    public String getVolumeFormula() {
	return volumeFormula;
    }

    public void setVolumeFormula(String formula) {
	this.volumeFormula = formula;
    }

    public String getAreaFormula() {
	return areaFormula;
    }

    public void setAreaFormula(String areaFormula) {
	this.areaFormula = areaFormula;
    }

    public String[] getFormulaInputs() {
	return formulaInputs;
    }

    public void setFormulaInputs(String[] formulaInputs) {
	this.formulaInputs = formulaInputs;
    }

    public List<String> getVolumeVariableNames() {
	return volumeVariableNames;
    }

    public void setVolumeVariableNames(List<String> variableNames) {
	this.volumeVariableNames = variableNames;
    }

    public List<String> getAreaVariableNames() {
	return areaVariableNames;
    }

    public void setAreaVariableNames(List<String> areaVariableNames) {
	this.areaVariableNames = areaVariableNames;
    }

    /**
     * Check if the formula is valid.
     * 
     * @return
     */
    public boolean isValid() {

	// Check if open brackets are equal to close brackets.
	int openIndices = org.apache.commons.lang.StringUtils.countMatches(
		this.volumeFormula, DELIMITER_OPEN_VARIABLE);
	int closeIndices = org.apache.commons.lang.StringUtils.countMatches(
		this.volumeFormula, DELIMITER_CLOSE_VARIABLE);
	if (openIndices == closeIndices) {
	    return true;
	}
	return false;
    }

    public String getAreaFormulaWithoutDelimiters() {
	String areaFormula = this.areaFormula;
	areaFormula = StringUtils.remove(areaFormula,
		Shape.DELIMITER_OPEN_VARIABLE);
	areaFormula = StringUtils.remove(areaFormula,
		Shape.DELIMITER_CLOSE_VARIABLE);
	return areaFormula;
    }

    public String getVolumeFormulaWithoutDelimiters() {
	String volFormula = this.volumeFormula;
	volFormula = StringUtils.remove(volFormula,
		Shape.DELIMITER_OPEN_VARIABLE);
	volFormula = StringUtils.remove(volFormula,
		Shape.DELIMITER_CLOSE_VARIABLE);
	return volFormula;
    }

    public double getArea() {
	return area;
    }

    public void setArea(double area) {
	this.area = area;
    }

    public double getVolume() {
	return volume;
    }

    public void setVolume(double volume) {
	this.volume = volume;
    }
}
