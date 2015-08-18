package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.bean.EstimateComputationBean;
import com.cebedo.pmsys.bean.EstimateComputationInputBean;
import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.enums.TableEstimationAllowance;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class EstimationOutput implements IDomainObject {

    private static final long serialVersionUID = -4949844838990090245L;

    /**
     * Keys.
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Inputs.
     */
    private String name;
    private String remarks;

    /**
     * Output.
     */
    private List<EstimateComputationBean> estimateComputationBeans;
    private String estimatesAsJson;
    private TableEstimationAllowance estimationAllowance;
    private Date lastComputed;

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

    // Grand total.
    private double costGrandTotal = 0;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public EstimationOutput() {
	;
    }

    public EstimationOutput(EstimateComputationInputBean estimateInput) {
	Project proj = estimateInput.getProject();
	setCompany(proj.getCompany());
	setProject(proj);
	setEstimationAllowance(estimateInput.getEstimationAllowance());
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

    public TableEstimationAllowance getEstimationAllowance() {
	return estimationAllowance;
    }

    public void setEstimationAllowance(TableEstimationAllowance estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    public List<EstimateComputationBean> getEstimates() {
	return estimateComputationBeans;
    }

    public void setEstimates(List<EstimateComputationBean> estimateComputationBeans) {
	this.estimateComputationBeans = estimateComputationBeans;
    }

    public Date getLastComputed() {
	return lastComputed;
    }

    public void setLastComputed(Date lastComputed) {
	this.lastComputed = lastComputed;
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(RegistryRedisKeys.KEY_ESTIMATION_OUTPUT, this.company.getId(),
		this.project.getId(), this.uuid);
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof EstimationOutput ? ((EstimationOutput) obj).getKey().equals(getKey())
		: false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    public static String constructPattern(Project proj) {
	return String.format(RegistryRedisKeys.KEY_ESTIMATION_OUTPUT, proj.getCompany().getId(),
		proj.getId(), "*");
    }

    public String getEstimatesAsJson() {
	return estimatesAsJson;
    }

    public void setEstimatesAsJson(String estimatesAsJson) {
	this.estimatesAsJson = estimatesAsJson;
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

    public void setResults(EstimateComputationInputBean estimateInput,
	    List<EstimateComputationBean> estimates2, String rowListJson) {
	setName(estimateInput.getName());
	setRemarks(estimateInput.getRemarks());
	setEstimates(estimates2);
	setEstimatesAsJson(rowListJson);
	setLastComputed(new Date(System.currentTimeMillis()));
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

    public String getCostGrandTotalAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(costGrandTotal);
    }

    public double getCostGrandTotal() {
	return costGrandTotal;
    }

    public void setCostGrandTotal(double costGrandTotal) {
	this.costGrandTotal = costGrandTotal;
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

}
