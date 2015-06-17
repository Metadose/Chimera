package com.cebedo.pmsys.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

public class Formula implements IDomainObject {

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("Formula");
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

    public static String constructKey(Company company2, String uuid2) {
	// company:12123:formula:uuid:123-123-123
	String companyPart = Company.OBJECT_NAME + ":"
		+ (company2 == null ? "*" : company2.getId()) + ":";
	String formulaPart = RedisConstants.OBJECT_FORMULA + ":";
	String uuidPart = "uuid:" + uuid2;
	String key = companyPart + formulaPart + uuidPart;
	return key;
    }

    public static String constructKey(Company company, UUID uuid) {
	// company:12123:formula:uuid:123-123-123
	String companyPart = Company.OBJECT_NAME + ":"
		+ (company == null ? "*" : company.getId()) + ":";
	String formulaPart = RedisConstants.OBJECT_FORMULA + ":";
	String uuidPart = "uuid:" + uuid;
	String key = companyPart + formulaPart + uuidPart;
	return key;
    }

    public static String constructPattern(Company company) {
	// company:12123:formula:uuid:123-123-123
	String companyPart = Company.OBJECT_NAME + ":"
		+ (company == null ? "*" : company.getId()) + ":";
	String formulaPart = RedisConstants.OBJECT_FORMULA + ":";
	String uuidPart = "uuid:*";
	String key = companyPart + formulaPart + uuidPart;
	return key;
    }

    @Override
    public String getKey() {
	// company:12123:formula:uuid:123-123-123
	String companyPart = Company.OBJECT_NAME + ":"
		+ (this.company == null ? 0 : this.company.getId()) + ":";
	String formulaPart = RedisConstants.OBJECT_FORMULA + ":";
	String uuidPart = "uuid:" + this.uuid;
	String key = companyPart + formulaPart + uuidPart;
	return key;
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
