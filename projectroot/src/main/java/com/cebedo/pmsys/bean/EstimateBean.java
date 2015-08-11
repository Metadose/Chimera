package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.enums.MappingEstimationClass;
import com.cebedo.pmsys.enums.TableCHBDimensions;
import com.cebedo.pmsys.enums.TableCHBFootingDimensions;
import com.cebedo.pmsys.enums.TableConcreteProportion;
import com.cebedo.pmsys.enums.TableEstimationAllowance;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;

public class EstimateBean implements Serializable {

    private static final long serialVersionUID = -3521975517764441834L;

    /**
     * Key parts.
     */
    private Company company;
    private Project project;

    /**
     * Basic specs.
     */
    private String name;
    private String remarks;

    /**
     * Computational specs.
     */
    private ShapeBean shapeBean;
    private double costPerUnitCHB = 0;
    private double costPerUnitCement40kg = 0;
    private double costPerUnitCement50kg = 0;
    private double costPerUnitSand = 0;
    private double costPerUnitGravel = 0;

    /**
     * Inputs.
     */
    // User input.
    private MultipartFile estimationFile;
    private TableEstimationAllowance estimationAllowance = TableEstimationAllowance.ALLOWANCE_0;

    // Standard constants.
    private MappingEstimationClass estimationClass = MappingEstimationClass.CLASS_A;
    private TableCHBDimensions chbDimensions = TableCHBDimensions.CHB_20_20_40;
    private TableCHBFootingDimensions chbFootingDimensions = TableCHBFootingDimensions.FOOTING_15_60;

    // Yes/No in the Excel file.
    private List<EstimateType> estimateTypes = new ArrayList<EstimateType>();

    // Masonry (Plastering) inputs.
    // TODO Is there a way to compute foundation based on area?
    // If no, choose this in JSP.
    private double chbFoundationHeight;
    private CommonLengthUnit chbFoundationUnit = CommonLengthUnit.METER;

    /**
     * Results
     */
    // Materials quantity and cost per estimate type.
    private ConcreteEstimateResults resultConcreteEstimate = new ConcreteEstimateResults();
    private MasonryCHBEstimateResults resultCHBEstimate = new MasonryCHBEstimateResults();
    private MasonryCHBLayingEstimateResults resultCHBLayingEstimate = new MasonryCHBLayingEstimateResults();
    private MasonryPlasteringEstimateResults resultPlasteringEstimate = new MasonryPlasteringEstimateResults();
    private MasonryCHBFootingEstimateResults resultCHBFootingEstimate = new MasonryCHBFootingEstimateResults();

    // Cost of the whole row.
    double costCement40kg = 0;
    double costCement50kg = 0;
    double costSand = 0;
    double costGravel = 0;
    double costCHB = 0;

    public EstimateBean() {
	;
    }

    public EstimateBean(Project proj) {
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
	return this.estimateTypes.contains(EstimateType.METAL_REINFORCEMENT_CHB);
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

    public TableConcreteProportion getConcreteProportion() {
	return getEstimationClass().getConcreteProportion();
    }

    public void setProject(Project project) {
	this.project = project;
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

    public ShapeBean getShape() {
	return shapeBean;
    }

    public void setShape(ShapeBean shapeBean) {
	this.shapeBean = shapeBean;
    }

    public List<EstimateType> getEstimateTypes() {
	return estimateTypes;
    }

    public void setEstimateTypes(List<EstimateType> estimateTypes) {
	this.estimateTypes = estimateTypes;
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

    public void setResultCHBLayingEstimate(MasonryCHBLayingEstimateResults resultCHBLayingEstimate) {
	this.resultCHBLayingEstimate = resultCHBLayingEstimate;
    }

    public MasonryPlasteringEstimateResults getResultPlasteringEstimate() {
	return resultPlasteringEstimate;
    }

    public void setResultPlasteringEstimate(MasonryPlasteringEstimateResults resultPlasteringEstimate) {
	this.resultPlasteringEstimate = resultPlasteringEstimate;
    }

    public TableCHBFootingDimensions getChbFootingDimensions() {
	return chbFootingDimensions;
    }

    public void setChbFootingDimensions(TableCHBFootingDimensions chbFootingDimensions) {
	this.chbFootingDimensions = chbFootingDimensions;
    }

    public MasonryCHBFootingEstimateResults getResultCHBFootingEstimate() {
	return resultCHBFootingEstimate;
    }

    public void setResultCHBFootingEstimate(MasonryCHBFootingEstimateResults resultCHBFootingEstimate) {
	this.resultCHBFootingEstimate = resultCHBFootingEstimate;
    }

    public ConcreteEstimateResults getResultConcreteEstimate() {
	return resultConcreteEstimate;
    }

    public void setResultConcreteEstimate(ConcreteEstimateResults resultConcreteEstimate) {
	this.resultConcreteEstimate = resultConcreteEstimate;
    }

    public TableEstimationAllowance getEstimationAllowance() {
	return estimationAllowance;
    }

    public void setEstimationAllowance(TableEstimationAllowance estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    public MultipartFile getEstimationFile() {
	return estimationFile;
    }

    public void setEstimationFile(MultipartFile estimationFile) {
	this.estimationFile = estimationFile;
    }

    public double getCostPerUnitCement40kg() {
	return costPerUnitCement40kg;
    }

    public void setCostPerUnitCement40kg(double costPerUnitCement40kg) {
	this.costPerUnitCement40kg = costPerUnitCement40kg;
    }

    public double getCostPerUnitCement50kg() {
	return costPerUnitCement50kg;
    }

    public void setCostPerUnitCement50kg(double costPerUnitCement50kg) {
	this.costPerUnitCement50kg = costPerUnitCement50kg;
    }

    public double getCostPerUnitSand() {
	return costPerUnitSand;
    }

    public void setCostPerUnitSand(double costPerUnitSand) {
	this.costPerUnitSand = costPerUnitSand;
    }

    public double getCostPerUnitGravel() {
	return costPerUnitGravel;
    }

    public void setCostPerUnitGravel(double costPerUnitGravel) {
	this.costPerUnitGravel = costPerUnitGravel;
    }

    public double getCostPerUnitCHB() {
	return costPerUnitCHB;
    }

    public void setCostPerUnitCHB(double costPerUnitCHB) {
	this.costPerUnitCHB = costPerUnitCHB;
    }

    public double getCostCement40kg() {
	return costCement40kg;
    }

    public void setCostCement40kg(double costCement40kg) {
	this.costCement40kg = costCement40kg;
    }

    public double getCostCement50kg() {
	return costCement50kg;
    }

    public void setCostCement50kg(double costCement50kg) {
	this.costCement50kg = costCement50kg;
    }

    public double getCostSand() {
	return costSand;
    }

    public void setCostSand(double costSand) {
	this.costSand = costSand;
    }

    public double getCostGravel() {
	return costGravel;
    }

    public void setCostGravel(double costGravel) {
	this.costGravel = costGravel;
    }

    public double getCostCHB() {
	return costCHB;
    }

    public void setCostCHB(double costCHB) {
	this.costCHB = costCHB;
    }

}
