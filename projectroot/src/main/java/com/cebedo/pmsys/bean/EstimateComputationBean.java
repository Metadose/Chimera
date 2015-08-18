package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.enums.MappingEstimationClass;
import com.cebedo.pmsys.enums.TableDimensionCHB;
import com.cebedo.pmsys.enums.TableDimensionCHBFooting;
import com.cebedo.pmsys.enums.TableEstimationAllowance;
import com.cebedo.pmsys.enums.TableMRCHBHorizontal;
import com.cebedo.pmsys.enums.TableMRCHBTieWire;
import com.cebedo.pmsys.enums.TableMRCHBVertical;
import com.cebedo.pmsys.enums.TableProportionConcrete;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;

public class EstimateComputationBean implements Serializable {

    private static final long serialVersionUID = -3521975517764441834L;

    /**
     * Key parts.
     */
    private Company company;
    private Project project;

    /**
     * Specs.
     */
    private String name;
    private String remarks;
    private EstimateComputationShape estimateComputationShape;

    /**
     * Inputs.
     */
    // User input.
    private MultipartFile estimationFile;
    private TableEstimationAllowance estimationAllowance = TableEstimationAllowance.ALLOWANCE_0;

    // Standard constants.
    private MappingEstimationClass estimationClass = MappingEstimationClass.CLASS_A;
    private TableDimensionCHB chbDimensions = TableDimensionCHB.CHB_20_20_40;
    private TableDimensionCHBFooting chbFootingDimensions = TableDimensionCHBFooting.FOOTING_15_60;
    private TableMRCHBVertical mrCHBVertical = TableMRCHBVertical.SAFEST;
    private TableMRCHBHorizontal mrCHBHorizontal = TableMRCHBHorizontal.SAFEST;
    private TableMRCHBTieWire mrCHBTieWire = TableMRCHBTieWire.SAFEST;

    // Yes/No in the Excel file.
    private List<EstimateType> estimateTypes = new ArrayList<EstimateType>();

    // Masonry (Plastering) inputs.
    // TODO Is there a way to compute foundation based on area?
    // If no, choose this in JSP.
    private double chbFoundationHeight;
    private CommonLengthUnit chbFoundationUnit = CommonLengthUnit.METER;

    // Prices per Unit.
    private double costPerUnitCHB = 0;
    private double costPerUnitCement40kg = 0;
    private double costPerUnitCement50kg = 0;
    private double costPerUnitSand = 0;
    private double costPerUnitGravel = 0;
    private double costPerUnitSteelBars = 0;
    private double costPerUnitTieWireKilos = 0;
    private double costPerUnitTieWireRolls = 0;

    /**
     * Results
     */
    // Materials quantity and cost per estimate type.
    // TODO Remove suffix "*Estimate" on below variables.
    private EstimateResultConcrete resultConcreteEstimate = new EstimateResultConcrete();
    private EstimateResultMasonryCHB resultCHBEstimate = new EstimateResultMasonryCHB();
    private EstimateResultMasonryCHBLaying resultCHBLayingEstimate = new EstimateResultMasonryCHBLaying();
    private EstimateResultMasonryPlastering resultPlasteringEstimate = new EstimateResultMasonryPlastering();
    private EstimateResultMasonryCHBFooting resultCHBFootingEstimate = new EstimateResultMasonryCHBFooting();
    private EstimateResultMRCHB resultMRCHB = new EstimateResultMRCHB();

    // Quantity of the whole row.
    private double quantityCement40kg = 0;
    private double quantityCement50kg = 0;
    private double quantitySand = 0;
    private double quantityGravel = 0;
    private double quantityCHB = 0;
    private double quantitySteelBars = 0;
    private double quantityTieWireKilos = 0;
    private double quantityTieWireRolls = 0;

    // Cost of the whole row.
    private double costCement40kg = 0;
    private double costCement50kg = 0;
    private double costSand = 0;
    private double costGravel = 0;
    private double costCHB = 0;
    private double costSteelBars = 0;
    private double costTieWireKilos = 0;
    private double costTieWireRolls = 0;

    public EstimateComputationBean() {
	;
    }

    public EstimateComputationBean(Project proj) {
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

    public TableProportionConcrete getConcreteProportion() {
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

    public EstimateComputationShape getShape() {
	return estimateComputationShape;
    }

    public void setShape(EstimateComputationShape estimateComputationShape) {
	this.estimateComputationShape = estimateComputationShape;
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

    public TableDimensionCHB getChbDimensions() {
	return chbDimensions;
    }

    public void setChbDimensions(TableDimensionCHB chbDimensions) {
	this.chbDimensions = chbDimensions;
    }

    public MappingEstimationClass getEstimationClass() {
	return estimationClass;
    }

    public void setEstimationClass(MappingEstimationClass estimationClass) {
	this.estimationClass = estimationClass;
    }

    public EstimateResultMasonryCHB getResultCHBEstimate() {
	return resultCHBEstimate;
    }

    public void setResultCHBEstimate(EstimateResultMasonryCHB resultCHBEstimate) {
	this.resultCHBEstimate = resultCHBEstimate;
    }

    public EstimateResultMasonryCHBLaying getResultCHBLayingEstimate() {
	return resultCHBLayingEstimate;
    }

    public void setResultCHBLayingEstimate(EstimateResultMasonryCHBLaying resultCHBLayingEstimate) {
	this.resultCHBLayingEstimate = resultCHBLayingEstimate;
    }

    public EstimateResultMasonryPlastering getResultPlasteringEstimate() {
	return resultPlasteringEstimate;
    }

    public void setResultPlasteringEstimate(EstimateResultMasonryPlastering resultPlasteringEstimate) {
	this.resultPlasteringEstimate = resultPlasteringEstimate;
    }

    public TableDimensionCHBFooting getChbFootingDimensions() {
	return chbFootingDimensions;
    }

    public void setChbFootingDimensions(TableDimensionCHBFooting chbFootingDimensions) {
	this.chbFootingDimensions = chbFootingDimensions;
    }

    public EstimateResultMasonryCHBFooting getResultCHBFootingEstimate() {
	return resultCHBFootingEstimate;
    }

    public void setResultCHBFootingEstimate(EstimateResultMasonryCHBFooting resultCHBFootingEstimate) {
	this.resultCHBFootingEstimate = resultCHBFootingEstimate;
    }

    public EstimateResultConcrete getResultConcreteEstimate() {
	return resultConcreteEstimate;
    }

    public void setResultConcreteEstimate(EstimateResultConcrete resultConcreteEstimate) {
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

    public double getQuantityCement40kg() {
	return quantityCement40kg;
    }

    public void setQuantityCement40kg(double quantityCement40kg) {
	this.quantityCement40kg = quantityCement40kg;
    }

    public double getQuantityCement50kg() {
	return quantityCement50kg;
    }

    public void setQuantityCement50kg(double quantityCement50kg) {
	this.quantityCement50kg = quantityCement50kg;
    }

    public double getQuantitySand() {
	return quantitySand;
    }

    public void setQuantitySand(double quantitySand) {
	this.quantitySand = quantitySand;
    }

    public double getQuantityGravel() {
	return quantityGravel;
    }

    public void setQuantityGravel(double quantityGravel) {
	this.quantityGravel = quantityGravel;
    }

    public double getQuantityCHB() {
	return quantityCHB;
    }

    public void setQuantityCHB(double quantityCHB) {
	this.quantityCHB = quantityCHB;
    }

    public TableMRCHBVertical getMrCHBVertical() {
	return mrCHBVertical;
    }

    public void setMrCHBVertical(TableMRCHBVertical mrCHBVertical) {
	this.mrCHBVertical = mrCHBVertical;
    }

    public TableMRCHBHorizontal getMrCHBHorizontal() {
	return mrCHBHorizontal;
    }

    public void setMrCHBHorizontal(TableMRCHBHorizontal mrCHBHorizontal) {
	this.mrCHBHorizontal = mrCHBHorizontal;
    }

    public TableMRCHBTieWire getMrCHBTieWire() {
	return mrCHBTieWire;
    }

    public void setMrCHBTieWire(TableMRCHBTieWire mrCHBTieWire) {
	this.mrCHBTieWire = mrCHBTieWire;
    }

    public EstimateResultMRCHB getResultMRCHB() {
	return resultMRCHB;
    }

    public void setResultMRCHB(EstimateResultMRCHB resultMRCHB) {
	this.resultMRCHB = resultMRCHB;
    }

    public double getCostSteelBars() {
	return costSteelBars;
    }

    public void setCostSteelBars(double costSteelBars) {
	this.costSteelBars = costSteelBars;
    }

    public double getCostTieWireKilos() {
	return costTieWireKilos;
    }

    public void setCostTieWireKilos(double costTieWireKilos) {
	this.costTieWireKilos = costTieWireKilos;
    }

    public double getCostTieWireRolls() {
	return costTieWireRolls;
    }

    public void setCostTieWireRolls(double costTieWireRolls) {
	this.costTieWireRolls = costTieWireRolls;
    }

    public double getCostPerUnitSteelBars() {
	return costPerUnitSteelBars;
    }

    public void setCostPerUnitSteelBars(double costPerUnitSteelBars) {
	this.costPerUnitSteelBars = costPerUnitSteelBars;
    }

    public double getCostPerUnitTieWireKilos() {
	return costPerUnitTieWireKilos;
    }

    public void setCostPerUnitTieWireKilos(double costPerUnitTieWireKilos) {
	this.costPerUnitTieWireKilos = costPerUnitTieWireKilos;
    }

    public double getCostPerUnitTieWireRolls() {
	return costPerUnitTieWireRolls;
    }

    public void setCostPerUnitTieWireRolls(double costPerUnitTieWireRolls) {
	this.costPerUnitTieWireRolls = costPerUnitTieWireRolls;
    }

    public double getQuantitySteelBars() {
	return quantitySteelBars;
    }

    public void setQuantitySteelBars(double quantitySteelBars) {
	this.quantitySteelBars = quantitySteelBars;
    }

    public double getQuantityTieWireKilos() {
	return quantityTieWireKilos;
    }

    public void setQuantityTieWireKilos(double quantityTieWireKilos) {
	this.quantityTieWireKilos = quantityTieWireKilos;
    }

    public double getQuantityTieWireRolls() {
	return quantityTieWireRolls;
    }

    public void setQuantityTieWireRolls(double quantityTieWireRolls) {
	this.quantityTieWireRolls = quantityTieWireRolls;
    }

}
