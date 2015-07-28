package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.bean.ConcreteEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBFootingEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBLayingEstimateResults;
import com.cebedo.pmsys.bean.MasonryPlasteringEstimateResults;
import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.enums.MappingEstimationClass;
import com.cebedo.pmsys.enums.TableCHBDimensions;
import com.cebedo.pmsys.enums.TableCHBFootingDimensions;
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
    private EstimationAllowance estimationAllowance;

    /**
     * Bean-backed form.
     */
    // Standard constants.
    private MappingEstimationClass estimationClass;
    private TableCHBDimensions chbDimensions;
    private TableCHBFootingDimensions chbFootingDimensions;

    // First commit.
    private String shapeKey;
    private String estimationAllowanceKey;
    private int[] estimateType;

    // Second commit.
    private Map<String, String> areaFormulaInputs = new HashMap<String, String>();
    private Map<String, String> volumeFormulaInputs = new HashMap<String, String>();
    private Map<String, CommonLengthUnit> areaFormulaInputsUnits = new HashMap<String, CommonLengthUnit>();
    private Map<String, CommonLengthUnit> volumeFormulaInputsUnits = new HashMap<String, CommonLengthUnit>();

    // Masonry (Plastering) inputs.
    private double chbFoundationHeight;
    private CommonLengthUnit chbFoundationUnit;
    private boolean plasterBackToBack; // Compute the needed plaster for the two
				       // sides of the area. If we have a 20
				       // sqm. wall, and we plaster back to
				       // back, then we compute 20sqm x 2sides =
				       // 40sqm total.
    private boolean plasterTopSide; // Plaster the top side of the shape.

    // Metal reinforcement (CHB).
    private String chbVerticalReinforcementKey;
    private String chbHorizontalReinforcementKey;

    /**
     * Results
     */
    private MasonryCHBEstimateResults resultCHBEstimate = new MasonryCHBEstimateResults();
    private MasonryCHBLayingEstimateResults resultCHBLayingEstimate = new MasonryCHBLayingEstimateResults();
    private MasonryPlasteringEstimateResults resultPlasteringEstimate = new MasonryPlasteringEstimateResults();
    private MasonryCHBFootingEstimateResults resultCHBFootingEstimate = new MasonryCHBFootingEstimateResults();
    private ConcreteEstimateResults resultConcreteEstimate = new ConcreteEstimateResults();

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

    public boolean willComputeMasonryCHB() {
	return this.estimateTypes.contains(EstimateType.MASONRY_CHB);
    }

    public boolean willComputeMasonryBlockLaying() {
	return this.estimateTypes.contains(EstimateType.MASONRY_BLOCK_LAYING);
    }

    public boolean willComputeMasonryPlastering() {
	return this.estimateTypes.contains(EstimateType.MASONRY_PLASTERING);
    }

    public boolean willComputeMasonryCHBFooting() {
	return this.estimateTypes.contains(EstimateType.MASONRY_CHB_FOOTING);
    }

    public boolean willComputeMRCHB() {
	return this.estimateTypes
		.contains(EstimateType.METAL_REINFORCEMENT_CHB);
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

    public Map<String, CommonLengthUnit> getVolumeFormulaInputsUnits() {
	return volumeFormulaInputsUnits;
    }

    public void setVolumeFormulaInputsUnits(
	    Map<String, CommonLengthUnit> formulaInputsUnits) {
	this.volumeFormulaInputsUnits = formulaInputsUnits;
    }

    public String getEstimationAllowanceKey() {
	return estimationAllowanceKey;
    }

    public void setEstimationAllowanceKey(String estimationAllowanceKey) {
	this.estimationAllowanceKey = estimationAllowanceKey;
    }

    public EstimationAllowance getEstimationAllowance() {
	return estimationAllowance;
    }

    public void setEstimationAllowance(EstimationAllowance estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    public boolean isPlasterBackToBack() {
	return plasterBackToBack;
    }

    public void setPlasterBackToBack(boolean plasterBackToBack) {
	this.plasterBackToBack = plasterBackToBack;
    }

    public boolean isPlasterTopSide() {
	return plasterTopSide;
    }

    public void setPlasterTopSide(boolean plasterTopSide) {
	this.plasterTopSide = plasterTopSide;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Estimate ? ((Estimate) obj).getKey().equals(
		getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    public double getChbFoundationHeight() {
	return chbFoundationHeight;
    }

    public void setChbFoundationHeight(double chbFoundationHeight) {
	this.chbFoundationHeight = chbFoundationHeight;
    }

    public CommonLengthUnit getChbFoundationUnit() {
	return chbFoundationUnit;
    }

    public void setChbFoundationUnit(CommonLengthUnit chbFoundationUnit) {
	this.chbFoundationUnit = chbFoundationUnit;
    }

    public String getChbVerticalReinforcementKey() {
	return chbVerticalReinforcementKey;
    }

    public void setChbVerticalReinforcementKey(
	    String chbVerticalReinforcementKey) {
	this.chbVerticalReinforcementKey = chbVerticalReinforcementKey;
    }

    public String getChbHorizontalReinforcementKey() {
	return chbHorizontalReinforcementKey;
    }

    public void setChbHorizontalReinforcementKey(
	    String chbHorizontalReinforcementKey) {
	this.chbHorizontalReinforcementKey = chbHorizontalReinforcementKey;
    }

    public TableCHBDimensions getChbDimensions() {
	return chbDimensions;
    }

    public void setChbDimensions(TableCHBDimensions chbDimensions) {
	this.chbDimensions = chbDimensions;
    }

    public MappingEstimationClass getEstimationClass() {
	return estimationClass;
    }

    public void setEstimationClass(MappingEstimationClass estimationClass) {
	this.estimationClass = estimationClass;
    }

    public MasonryCHBEstimateResults getResultCHBEstimate() {
	return resultCHBEstimate;
    }

    public void setResultCHBEstimate(MasonryCHBEstimateResults resultCHBEstimate) {
	this.resultCHBEstimate = resultCHBEstimate;
    }

    public MasonryCHBLayingEstimateResults getResultCHBLayingEstimate() {
	return resultCHBLayingEstimate;
    }

    public void setResultCHBLayingEstimate(
	    MasonryCHBLayingEstimateResults resultCHBLayingEstimate) {
	this.resultCHBLayingEstimate = resultCHBLayingEstimate;
    }

    public MasonryPlasteringEstimateResults getResultPlasteringEstimate() {
	return resultPlasteringEstimate;
    }

    public void setResultPlasteringEstimate(
	    MasonryPlasteringEstimateResults resultPlasteringEstimate) {
	this.resultPlasteringEstimate = resultPlasteringEstimate;
    }

    public TableCHBFootingDimensions getChbFootingDimensions() {
	return chbFootingDimensions;
    }

    public void setChbFootingDimensions(
	    TableCHBFootingDimensions chbFootingDimensions) {
	this.chbFootingDimensions = chbFootingDimensions;
    }

    public MasonryCHBFootingEstimateResults getResultCHBFootingEstimate() {
	return resultCHBFootingEstimate;
    }

    public void setResultCHBFootingEstimate(
	    MasonryCHBFootingEstimateResults resultCHBFootingEstimate) {
	this.resultCHBFootingEstimate = resultCHBFootingEstimate;
    }

    public ConcreteEstimateResults getResultConcreteEstimate() {
	return resultConcreteEstimate;
    }

    public void setResultConcreteEstimate(
	    ConcreteEstimateResults resultConcreteEstimate) {
	this.resultConcreteEstimate = resultConcreteEstimate;
    }

}
