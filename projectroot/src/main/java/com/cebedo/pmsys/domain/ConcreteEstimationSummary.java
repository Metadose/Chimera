package com.cebedo.pmsys.domain;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.bean.CostEstimationBean;
import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class ConcreteEstimationSummary implements IDomainObject {

    private static final long serialVersionUID = -8209296327109938266L;

    /**
     * Key parts.
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Number of units and their unit of measure.
     */
    private ConcreteProportion concreteProportion;
    private double totalUnitsCement40kg;
    private double totalUnitsCement50kg;
    private double totalUnitsSand;
    private double totalUnitsGravel;
    private Unit unitCement40kg;
    private Unit unitCement50kg;
    private Unit unitSand;
    private Unit unitGravel;

    /**
     * Total of cost.
     */
    private double totalCostCement40kg;
    private double totalCostCement50kg;
    private double totalCostSand;
    private double totalCostGravel;
    private double grandTotalCostIf40kg;
    private double grandTotalCostIf50kg;

    /**
     * Bean-backed form.
     */
    private String name;
    private String description;
    private String[] estimationToCompute;

    private double costPerUnitCement40kg;
    private double costPerUnitCement50kg;
    private double costPerUnitSand;
    private double costPerUnitGravel;

    /**
     * Other specs.
     */
    private Date lastComputed;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public ConcreteEstimationSummary() {
	;
    }

    public ConcreteEstimationSummary(Project proj) {
	setCompany(proj.getCompany());
	setProject(proj);
    }

    public ConcreteEstimationSummary(Project proj,
	    CostEstimationBean costEstimationBean) {
	setCompany(proj.getCompany());
	setProject(proj);

	this.costPerUnitCement40kg = costEstimationBean
		.getCostPerUnitCement40kg();
	this.costPerUnitCement50kg = costEstimationBean
		.getCostPerUnitCement50kg();
	this.costPerUnitGravel = costEstimationBean.getCostPerUnitGravel();
	this.costPerUnitSand = costEstimationBean.getCostPerUnitSand();
	this.description = costEstimationBean.getDescription();
	this.estimationToCompute = costEstimationBean.getEstimationToCompute();
	this.name = costEstimationBean.getName();
    }

    /**
     * Formatted cost text display.
     */
    public String getTotalCostCement40kgAsString() {
	return getFormattedCostAsString(totalCostCement40kg);
    }

    public String getTotalCostCement50kgAsString() {
	return getFormattedCostAsString(totalCostCement50kg);
    }

    public String getTotalCostSandAsString() {
	return getFormattedCostAsString(totalCostSand);
    }

    public String getTotalCostGravelAsString() {
	return getFormattedCostAsString(totalCostGravel);
    }

    public String getGrandTotalCostIf40kgAsString() {
	return getFormattedCostAsString(grandTotalCostIf40kg);
    }

    public String getGrandTotalCostIf50kgAsString() {
	return getFormattedCostAsString(grandTotalCostIf50kg);
    }

    /**
     * Formatted quantity text display.
     */
    public String getTotalUnitsCement40kgAsString() {
	return getFormattedQuantityAsString(totalUnitsCement40kg);
    }

    public String getTotalUnitsCement50kgAsString() {
	return getFormattedQuantityAsString(totalUnitsCement50kg);
    }

    public String getTotalUnitsSandAsString() {
	return getFormattedQuantityAsString(totalUnitsSand);
    }

    public String getTotalUnitsGravelAsString() {
	return getFormattedQuantityAsString(totalUnitsGravel);
    }

    /**
     * Format functions.
     */
    private String getFormattedCostAsString(double dblVal) {
	double displayDbl = Math.ceil(dblVal);
	return NumberFormatUtils.getCurrencyFormatter().format(displayDbl);
    }

    private String getFormattedQuantityAsString(double dblVal) {
	double displayDbl = Math.ceil(dblVal);
	DecimalFormat df = new DecimalFormat();
	return df.format(displayDbl);
    }

    /**
     * Per unit displays.
     */
    public String getCostPerUnitCement40kgAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		costPerUnitCement40kg);
    }

    public String getCostPerUnitCement50kgAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		costPerUnitCement50kg);
    }

    public String getCostPerUnitSandAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(costPerUnitSand);
    }

    public String getCostPerUnitGravelAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		costPerUnitGravel);
    }

    /**
     * Other functions.
     */
    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    public double getTotalUnitsCement40kg() {
	return totalUnitsCement40kg;
    }

    public void setTotalUnitsCement40kg(double totalUnitsCement40kg) {
	this.totalUnitsCement40kg = totalUnitsCement40kg;
    }

    public double getTotalUnitsCement50kg() {
	return totalUnitsCement50kg;
    }

    public void setTotalUnitsCement50kg(double totalUnitsCement50kg) {
	this.totalUnitsCement50kg = totalUnitsCement50kg;
    }

    public double getTotalUnitsSand() {
	return totalUnitsSand;
    }

    public void setTotalUnitsSand(double totalUnitsSand) {
	this.totalUnitsSand = totalUnitsSand;
    }

    public double getTotalUnitsGravel() {
	return totalUnitsGravel;
    }

    public void setTotalUnitsGravel(double totalUnitsGravel) {
	this.totalUnitsGravel = totalUnitsGravel;
    }

    public Unit getUnitCement40kg() {
	return unitCement40kg;
    }

    public void setUnitCement40kg(Unit unitCement40kg) {
	this.unitCement40kg = unitCement40kg;
    }

    public Unit getUnitCement50kg() {
	return unitCement50kg;
    }

    public void setUnitCement50kg(Unit unitCement50kg) {
	this.unitCement50kg = unitCement50kg;
    }

    public Unit getUnitSand() {
	return unitSand;
    }

    public void setUnitSand(Unit unitSand) {
	this.unitSand = unitSand;
    }

    public Unit getUnitGravel() {
	return unitGravel;
    }

    public void setUnitGravel(Unit unitGravel) {
	this.unitGravel = unitGravel;
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

    public double getTotalCostCement40kg() {
	return totalCostCement40kg;
    }

    public void setTotalCostCement40kg(double totalCostCement40kg) {
	this.totalCostCement40kg = totalCostCement40kg;
    }

    public double getTotalCostCement50kg() {
	return totalCostCement50kg;
    }

    public void setTotalCostCement50kg(double totalCostCement50kg) {
	this.totalCostCement50kg = totalCostCement50kg;
    }

    public double getTotalCostSand() {
	return totalCostSand;
    }

    public void setTotalCostSand(double totalCostSand) {
	this.totalCostSand = totalCostSand;
    }

    public double getTotalCostGravel() {
	return totalCostGravel;
    }

    public void setTotalCostGravel(double totalCostGravel) {
	this.totalCostGravel = totalCostGravel;
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

    public Date getLastComputed() {
	return lastComputed;
    }

    public void setLastComputed(Date lastComputed) {
	this.lastComputed = lastComputed;
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

    public double getGrandTotalCostIf40kg() {
	return grandTotalCostIf40kg;
    }

    public void setGrandTotalCostIf40kg(double grandTotalCostIf40kg) {
	this.grandTotalCostIf40kg = grandTotalCostIf40kg;
    }

    public double getGrandTotalCostIf50kg() {
	return grandTotalCostIf50kg;
    }

    public void setGrandTotalCostIf50kg(double grandTotalCostIf50kg) {
	this.grandTotalCostIf50kg = grandTotalCostIf50kg;
    }

    public String[] getEstimationToCompute() {
	return estimationToCompute;
    }

    public void setEstimationToCompute(String[] estimationToCompute) {
	this.estimationToCompute = estimationToCompute;
    }

    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_CONCRETE_ESTIMATION_SUMMARY,
		this.company.getId(), this.project.getId(), this.uuid);
    }

    public static String constructPattern(Project proj) {
	return String.format(RedisKeyRegistry.KEY_CONCRETE_ESTIMATION_SUMMARY,
		proj.getCompany().getId(), proj.getId(), "*");
    }

    public ConcreteProportion getConcreteProportion() {
	return concreteProportion;
    }

    public void setConcreteProportion(ConcreteProportion concreteProportion) {
	this.concreteProportion = concreteProportion;
    }
}
