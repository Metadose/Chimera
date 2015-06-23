package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;

public class Estimate implements IDomainObject {

    private static final long serialVersionUID = -3521975517764441834L;

    /**
     * Key parts.
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Basic specs.
     */
    private String name;
    private String remarks;
    private Date lastComputed;

    /**
     * Computational specs.
     */
    private Shape shape;
    private List<EstimateType> estimateTypes;

    /**
     * Bean-backed form.
     */
    // First commit.
    private String shapeKey;
    private int[] estimateType;

    // Second commit.
    private String[] formulaInputs;
    private String concreteMixingRatioKey;

    /**
     * Concrete.
     */
    private ConcreteMixingRatio concreteMixingRatio;
    private double resultCement;
    private double resultSand;
    private double resultGravel;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Estimate() {
	;
    }

    public Estimate(Project proj) {
	setCompany(proj.getCompany());
	setProject(proj);
    }

    public boolean willComputeConcrete() {
	return this.estimateTypes.contains(EstimateType.CONCRETE);
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
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

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public Date getLastComputed() {
	return lastComputed;
    }

    public void setLastComputed(Date lastComputed) {
	this.lastComputed = lastComputed;
    }

    public Shape getShape() {
	return shape;
    }

    public void setShape(Shape shape) {
	this.shape = shape;
    }

    public String[] getFormulaInputs() {
	return formulaInputs;
    }

    public void setFormulaInputs(String[] formulaInputs) {
	this.formulaInputs = formulaInputs;
    }

    public ConcreteMixingRatio getConcreteMixingRatio() {
	return concreteMixingRatio;
    }

    public void setConcreteMixingRatio(ConcreteMixingRatio concreteMixingRatio) {
	this.concreteMixingRatio = concreteMixingRatio;
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_ESTIMATE,
		this.company.getId(), this.project.getId(), this.uuid);
    }

    public String getConcreteMixingRatioKey() {
	return concreteMixingRatioKey;
    }

    public void setConcreteMixingRatioKey(String concreteMixingRatioKey) {
	this.concreteMixingRatioKey = concreteMixingRatioKey;
    }

    public String getShapeKey() {
	return shapeKey;
    }

    public void setShapeKey(String shapeKey) {
	this.shapeKey = shapeKey;
    }

    public int[] getEstimateType() {
	return estimateType;
    }

    public void setEstimateType(int[] estimateType) {
	this.estimateType = estimateType;
    }

    public List<EstimateType> getEstimateTypes() {
	return estimateTypes;
    }

    public void setEstimateTypes(List<EstimateType> estimateTypes) {
	this.estimateTypes = estimateTypes;
    }

    public double getResultSand() {
	return resultSand;
    }

    public void setResultSand(double resultSand) {
	this.resultSand = resultSand;
    }

    public double getResultGravel() {
	return resultGravel;
    }

    public void setResultGravel(double resultGravel) {
	this.resultGravel = resultGravel;
    }

    public double getResultCement() {
	return resultCement;
    }

    public void setResultCement(double resultCement) {
	this.resultCement = resultCement;
    }

    public static String constructPattern(Project proj) {
	return String.format(RedisKeyRegistry.KEY_ESTIMATE, proj.getCompany()
		.getId(), proj.getId(), "*");
    }

}
