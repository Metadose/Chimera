package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.bean.ConcreteEstimateResults;
import com.cebedo.pmsys.bean.MasonryEstimateResults;
import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.CommonLengthUnit;
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
    private boolean copy;

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
    private Map<String, String> areaFormulaInputs = new HashMap<String, String>();
    private Map<String, String> volumeFormulaInputs = new HashMap<String, String>();
    private Map<String, CommonLengthUnit> areaFormulaInputsUnits = new HashMap<String, CommonLengthUnit>();
    private Map<String, CommonLengthUnit> volumeFormulaInputsUnits = new HashMap<String, CommonLengthUnit>();

    // Concrete estimate.
    private String concreteProportionKeys[];

    // Masonry estimate;
    private String chbMeasurementKey;

    /**
     * Results
     */
    private ConcreteEstimateResults resultEstimateConcrete;
    private MasonryEstimateResults resultEstimateMasonry;

    /**
     * Concrete.
     */
    private ConcreteProportion concreteProportion;

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

    public boolean willComputeMasonry() {
	return this.estimateTypes.contains(EstimateType.MASONRY);
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

    public ConcreteProportion getConcreteProportion() {
	return concreteProportion;
    }

    public void setConcreteProportion(ConcreteProportion concreteProportion) {
	this.concreteProportion = concreteProportion;
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

    public Map<String, String> getVolumeFormulaInputs() {
	return volumeFormulaInputs;
    }

    public void setVolumeFormulaInputs(Map<String, String> formulaInputs) {
	this.volumeFormulaInputs = formulaInputs;
    }

    public Map<String, String> getAreaFormulaInputs() {
	return areaFormulaInputs;
    }

    public void setAreaFormulaInputs(Map<String, String> areaFormulaInputs) {
	this.areaFormulaInputs = areaFormulaInputs;
    }

    public Map<String, CommonLengthUnit> getAreaFormulaInputsUnits() {
	return areaFormulaInputsUnits;
    }

    public void setAreaFormulaInputsUnits(
	    Map<String, CommonLengthUnit> areaFormulaInputsUnits) {
	this.areaFormulaInputsUnits = areaFormulaInputsUnits;
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

    public static String constructPattern(Project proj) {
	return String.format(RedisKeyRegistry.KEY_ESTIMATE, proj.getCompany()
		.getId(), proj.getId(), "*");
    }

    public ConcreteEstimateResults getResultEstimateConcrete() {
	return resultEstimateConcrete;
    }

    public void setResultEstimateConcrete(
	    ConcreteEstimateResults resultEstimateConcrete) {
	this.resultEstimateConcrete = resultEstimateConcrete;
    }

    public Map<String, CommonLengthUnit> getVolumeFormulaInputsUnits() {
	return volumeFormulaInputsUnits;
    }

    public void setVolumeFormulaInputsUnits(
	    Map<String, CommonLengthUnit> formulaInputsUnits) {
	this.volumeFormulaInputsUnits = formulaInputsUnits;
    }

    public String[] getConcreteProportionKeys() {
	return concreteProportionKeys;
    }

    public void setConcreteProportionKeys(String[] concreteProportionKeys) {
	this.concreteProportionKeys = concreteProportionKeys;
    }

    public String getChbMeasurementKey() {
	return chbMeasurementKey;
    }

    public void setChbMeasurementKey(String chbMeasurementKey) {
	this.chbMeasurementKey = chbMeasurementKey;
    }

    public MasonryEstimateResults getResultEstimateMasonry() {
	return resultEstimateMasonry;
    }

    public void setResultEstimateMasonry(
	    MasonryEstimateResults resultEstimateMasonry) {
	this.resultEstimateMasonry = resultEstimateMasonry;
    }

    public boolean isCopy() {
	return copy;
    }

    public void setCopy(boolean copy) {
	this.copy = copy;
    }

}
