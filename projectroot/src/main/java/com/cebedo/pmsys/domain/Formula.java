package com.cebedo.pmsys.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;

public class Formula implements IDomainObject {

    private static final long serialVersionUID = 1604488532064947576L;
    public static final String DELIMITER_OPEN_VARIABLE = "[[";
    public static final String DELIMITER_CLOSE_VARIABLE = "]]";

    /**
     * Keys: company:12123:formula:uuid:123-123-123
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
    private String formula;
    private List<String> variableNames;
    private String[] formulaInputs;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Formula() {
	;
    }

    public Formula(Company company2, UUID uuid2) {
	setCompany(company2);
	setUuid(uuid2);
    }

    public Formula(Company company2) {
	setCompany(company2);
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    public static String constructPattern(Company company) {
	return String
		.format(RedisKeyRegistry.KEY_FORMULA, company.getId(), "*");
    }

    @Override
    public String getKey() {
	// company:12123:formula:uuid:123-123-123
	return String.format(RedisKeyRegistry.KEY_FORMULA,
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

    public String getFormula() {
	return formula;
    }

    public void setFormula(String formula) {
	this.formula = formula;
    }

    public String[] getFormulaInputs() {
	return formulaInputs;
    }

    public void setFormulaInputs(String[] formulaInputs) {
	this.formulaInputs = formulaInputs;
    }

    public List<String> getVariableNames() {
	return variableNames;
    }

    public void setVariableNames(List<String> variableNames) {
	this.variableNames = variableNames;
    }

    /**
     * Check if the formula is valid.
     * 
     * @return
     */
    public boolean isValid() {

	// Check if open brackets are equal to close brackets.
	int openIndices = org.apache.commons.lang.StringUtils.countMatches(
		this.formula, DELIMITER_OPEN_VARIABLE);
	int closeIndices = org.apache.commons.lang.StringUtils.countMatches(
		this.formula, DELIMITER_CLOSE_VARIABLE);
	if (openIndices == closeIndices) {
	    return true;
	}
	return false;
    }
}
