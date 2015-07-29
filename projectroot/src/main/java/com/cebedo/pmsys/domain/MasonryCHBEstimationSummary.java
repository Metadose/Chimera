package com.cebedo.pmsys.domain;

import java.text.NumberFormat;
import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.bean.CostEstimationBean;
import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.TableCHBDimensions;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class MasonryCHBEstimationSummary implements IDomainObject {

    private static NumberFormat quantityFormatter = NumberFormatUtils
	    .getQuantityFormatter();
    private static NumberFormat costFormatter = NumberFormatUtils
	    .getCostFormatter();
    private static final long serialVersionUID = -946543461997147334L;

    /**
     * Key parts.
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Computed specs.
     */
    private double totalPiecesCHB;
    private double totalCostOfCHB;

    /**
     * Bean-backed form.
     */
    private double costPerPieceCHB;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    /**
     * Basic details.
     */
    private String name;
    private String description;
    private String[] estimationToCompute;

    /**
     * Properties to set during cost estimation.
     */
    private TableCHBDimensions chbDimensions;
    private Map<String, String> areaFormulaInputs;
    private double area;
    private EstimationAllowance estimationAllowance;

    public MasonryCHBEstimationSummary() {
	;
    }

    public MasonryCHBEstimationSummary(Project proj,
	    CostEstimationBean costEstimationBean) {
	setCompany(proj.getCompany());
	setProject(proj);

	this.name = costEstimationBean.getName();
	this.description = costEstimationBean.getDescription();
	this.estimationToCompute = costEstimationBean.getEstimationToCompute();
	this.costPerPieceCHB = costEstimationBean.getCostPerPieceCHB();
    }

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	return String.format(
		RedisKeyRegistry.KEY_MASONRY_CHB_ESTIMATION_SUMMARY,
		this.company.getId(), this.project.getId(), this.uuid);
    }

    public static String constructPattern(Project proj) {
	return String.format(
		RedisKeyRegistry.KEY_MASONRY_CHB_ESTIMATION_SUMMARY, proj
			.getCompany().getId(), proj.getId(), "*");
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

    public double getTotalPiecesCHB() {
	return totalPiecesCHB;
    }

    public String getTotalPiecesCHBAsString() {
	return quantityFormatter.format(Math.ceil(totalPiecesCHB));
    }

    public void setTotalPiecesCHB(double totalPiecesCHB) {
	this.totalPiecesCHB = totalPiecesCHB;
    }

    public double getTotalCostOfCHB() {
	return totalCostOfCHB;
    }

    public String getTotalCostOfCHBAsString() {
	return costFormatter.format(Math.ceil(totalCostOfCHB));
    }

    public void setTotalCostOfCHB(double totalCostPerCHB) {
	this.totalCostOfCHB = totalCostPerCHB;
    }

    public double getCostPerPieceCHB() {
	return costPerPieceCHB;
    }

    public String getCostPerPieceCHBAsString() {
	return costFormatter.format(Math.ceil(costPerPieceCHB));
    }

    public void setCostPerPieceCHB(double costPerPieceCHB) {
	this.costPerPieceCHB = costPerPieceCHB;
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

    public String[] getEstimationToCompute() {
	return estimationToCompute;
    }

    public void setEstimationToCompute(String[] estimationToCompute) {
	this.estimationToCompute = estimationToCompute;
    }

    public Map<String, String> getAreaFormulaInputs() {
	return areaFormulaInputs;
    }

    public void setAreaFormulaInputs(Map<String, String> areaFormulaInputs) {
	this.areaFormulaInputs = areaFormulaInputs;
    }

    public double getArea() {
	return area;
    }

    public void setArea(double area) {
	this.area = area;
    }

    public EstimationAllowance getEstimationAllowance() {
	return estimationAllowance;
    }

    public void setEstimationAllowance(EstimationAllowance estimationAllowance) {
	this.estimationAllowance = estimationAllowance;
    }

    public TableCHBDimensions getChbDimensions() {
	return chbDimensions;
    }

    public void setChbDimensions(TableCHBDimensions chbDimensions) {
	this.chbDimensions = chbDimensions;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof MasonryCHBEstimationSummary ? ((MasonryCHBEstimationSummary) obj)
		.getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

}
