package com.cebedo.pmsys.bean;

public class CostEstimationBean {

    public static final String BEAN_NAME = "costestimation";

    /**
     * Bean-backed form.
     */
    // Basic details.
    private String name;
    private String description;
    private String[] estimationToCompute;

    // Concrete estimation.
    private double costPerUnitCement40kg;
    private double costPerUnitCement50kg;
    private double costPerUnitSand;
    private double costPerUnitGravel;

    // Masonry estimation.
    private double costPerPieceCHB;

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

    public double getCostPerPieceCHB() {
	return costPerPieceCHB;
    }

    public void setCostPerPieceCHB(double costPerPieceCHB) {
	this.costPerPieceCHB = costPerPieceCHB;
    }

}
