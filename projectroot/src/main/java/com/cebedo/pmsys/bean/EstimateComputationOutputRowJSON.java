package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.util.UUID;

public class EstimateComputationOutputRowJSON implements Serializable {

    private static final long serialVersionUID = 5377007792468931713L;

    // Details.
    private String uuid;
    private String name;
    private String remarks;
    private double area;
    private double volume;
    private double areaBelowGround;
    private double footingLength;
    private double footingWidth;
    private double footingHeight;

    // Concrete.
    private double concreteCement40kg;
    private double concreteCement50kg;
    private double concreteSand;
    private double concreteGravel;

    private double concreteCostCement40kg;
    private double concreteCostCement50kg;
    private double concreteCostSand;
    private double concreteCostGravel;

    // CHB.
    private double chbTotal;
    private double chbCostTotal;

    // CHB Laying.
    private double chbLayingBags40kg;
    private double chbLayingBags50kg;
    private double chbLayingSand;

    private double chbLayingCostBags40kg;
    private double chbLayingCostBags50kg;
    private double chbLayingCostSand;

    // Platering.
    private double plasteringCement40kg;
    private double plasteringCement50kg;
    private double plasteringSand;

    private double plasteringCostCement40kg;
    private double plasteringCostCement50kg;
    private double plasteringCostSand;

    // CHB Footing.
    private double footingCement40kg;
    private double footingCement50kg;
    private double footingSand;
    private double footingGravel;

    private double footingCostCement40kg;
    private double footingCostCement50kg;
    private double footingCostSand;
    private double footingCostGravel;

    // Metal reinforcement (CHB).
    private double mrCHBSteelBar;
    private double mrCHBTieWireKg;
    private double mrCHBTieWireRoll;

    private double mrCHBCostSteelBar;
    private double mrCHBCostTieWireKg;
    private double mrCHBCostTieWireRoll;

    public EstimateComputationOutputRowJSON() {
	;
    }

    public EstimateComputationOutputRowJSON(EstimateComputationBean estimateComputationBean) {

	EstimateComputationShape estimateComputationShape = estimateComputationBean.getShape();
	EstimateResultConcrete concrete = estimateComputationBean.getResultConcreteEstimate();
	EstimateResultMasonryCHB chb = estimateComputationBean.getResultCHBEstimate();
	EstimateResultMasonryCHBLaying chbLaying = estimateComputationBean.getResultCHBLayingEstimate();
	EstimateResultMasonryPlastering plaster = estimateComputationBean.getResultPlasteringEstimate();
	EstimateResultMasonryCHBFooting footing = estimateComputationBean.getResultCHBFootingEstimate();
	EstimateResultMRCHB mrCHB = estimateComputationBean.getResultMRCHB();

	// Details.
	this.uuid = UUID.randomUUID().toString();
	this.name = estimateComputationBean.getName();
	this.remarks = estimateComputationBean.getRemarks();
	this.area = estimateComputationShape.getOriginalArea();
	this.volume = estimateComputationShape.getOriginalVolume();
	this.areaBelowGround = estimateComputationBean.getAreaBelowGround();
	this.footingLength = estimateComputationShape.getFootingLength();
	this.footingWidth = estimateComputationShape.getFootingWidth();
	this.footingHeight = estimateComputationShape.getFootingHeight();

	// CHB.
	this.chbTotal = chb.getTotalCHB();
	this.chbCostTotal = chb.getCostCHB();

	// Concrete.
	this.concreteCement40kg = concrete.getCement40kg();
	this.concreteCement50kg = concrete.getCement50kg();
	this.concreteSand = concrete.getSand();
	this.concreteGravel = concrete.getGravel();

	this.concreteCostCement40kg = concrete.getCostCement40kg();
	this.concreteCostCement50kg = concrete.getCostCement50kg();
	this.concreteCostSand = concrete.getCostSand();
	this.concreteCostGravel = concrete.getCostGravel();

	// CHB Laying.
	double chbLayingBags40 = chbLaying.getCement40kg();
	this.chbLayingBags40kg = chbLayingBags40;
	this.chbLayingBags50kg = chbLayingBags40 - (chbLayingBags40 * 0.2);
	this.chbLayingSand = chbLaying.getSand();

	this.chbLayingCostBags40kg = chbLaying.getCostCement40kg();
	this.chbLayingCostBags50kg = chbLaying.getCostCement50kg();
	this.chbLayingCostSand = chbLaying.getCostSand();

	// Plaster.
	this.plasteringCement40kg = plaster.getCement40kg();
	this.plasteringCement50kg = plaster.getCement50kg();
	this.plasteringSand = plaster.getSand();

	this.plasteringCostCement40kg = plaster.getCostCement40kg();
	this.plasteringCostCement50kg = plaster.getCostCement50kg();
	this.plasteringCostSand = plaster.getCostSand();

	// Footing.
	double footingBags40 = footing.getCement40kg();
	this.footingCement40kg = footingBags40;
	this.footingCement50kg = footingBags40 - (footingBags40 * 0.2);
	this.footingSand = footing.getSand();
	this.footingGravel = footing.getGravel();

	this.footingCostCement40kg = footing.getCostCement40kg();
	this.footingCostCement50kg = footing.getCostCement50kg();
	this.footingCostSand = footing.getCostSand();
	this.footingCostGravel = footing.getCostGravel();

	// Metal reinforcement (CHB).
	this.mrCHBSteelBar = mrCHB.getSteelBarsQuantity();
	this.mrCHBTieWireKg = mrCHB.getTieWireKilos();
	this.mrCHBTieWireRoll = mrCHB.getTieWireRolls();

	this.mrCHBCostSteelBar = mrCHB.getCostSteelBars();
	this.mrCHBCostTieWireKg = mrCHB.getCostTieWireKilos();
	this.mrCHBCostTieWireRoll = mrCHB.getCostTieWireRolls();
    }

    // Setters and Getters.

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

    public double getArea() {
	return area;
    }

    public void setArea(double area) {
	this.area = area;
    }

    public double getVolume() {
	return volume;
    }

    public void setVolume(double volume) {
	this.volume = volume;
    }

    public double getFootingLength() {
	return footingLength;
    }

    public void setFootingLength(double footingLength) {
	this.footingLength = footingLength;
    }

    public double getConcreteCement40kg() {
	return concreteCement40kg;
    }

    public void setConcreteCement40kg(double concreteCement40kg) {
	this.concreteCement40kg = concreteCement40kg;
    }

    public double getConcreteCement50kg() {
	return concreteCement50kg;
    }

    public void setConcreteCement50kg(double concreteCement50kg) {
	this.concreteCement50kg = concreteCement50kg;
    }

    public double getConcreteSand() {
	return concreteSand;
    }

    public void setConcreteSand(double concreteSand) {
	this.concreteSand = concreteSand;
    }

    public double getConcreteGravel() {
	return concreteGravel;
    }

    public void setConcreteGravel(double concreteGravel) {
	this.concreteGravel = concreteGravel;
    }

    public double getCHBTotal() {
	return chbTotal;
    }

    public void setCHBTotal(double totalCHB) {
	this.chbTotal = totalCHB;
    }

    public double getChbLayingBags40kg() {
	return chbLayingBags40kg;
    }

    public void setChbLayingBags40kg(double chbLayingBags40kg) {
	this.chbLayingBags40kg = chbLayingBags40kg;
    }

    public double getChbLayingBags50kg() {
	return chbLayingBags50kg;
    }

    public void setChbLayingBags50kg(double chbLayingBags50kg) {
	this.chbLayingBags50kg = chbLayingBags50kg;
    }

    public double getChbLayingSand() {
	return chbLayingSand;
    }

    public void setChbLayingSand(double chbLayingSand) {
	this.chbLayingSand = chbLayingSand;
    }

    public double getPlasteringCement40kg() {
	return plasteringCement40kg;
    }

    public void setPlasteringCement40kg(double plasteringCement40kg) {
	this.plasteringCement40kg = plasteringCement40kg;
    }

    public double getPlasteringCement50kg() {
	return plasteringCement50kg;
    }

    public void setPlasteringCement50kg(double plasteringCement50kg) {
	this.plasteringCement50kg = plasteringCement50kg;
    }

    public double getPlasteringSand() {
	return plasteringSand;
    }

    public void setPlasteringSand(double plasteringSand) {
	this.plasteringSand = plasteringSand;
    }

    public double getFootingCement40kg() {
	return footingCement40kg;
    }

    public void setFootingCement40kg(double footingCement40kg) {
	this.footingCement40kg = footingCement40kg;
    }

    public double getFootingCement50kg() {
	return footingCement50kg;
    }

    public void setFootingCement50kg(double footingCement50kg) {
	this.footingCement50kg = footingCement50kg;
    }

    public double getFootingSand() {
	return footingSand;
    }

    public void setFootingSand(double footingSand) {
	this.footingSand = footingSand;
    }

    public double getFootingGravel() {
	return footingGravel;
    }

    public void setFootingGravel(double footingGravel) {
	this.footingGravel = footingGravel;
    }

    public String getUuid() {
	return uuid;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid;
    }

    public double getConcreteCostCement40kg() {
	return concreteCostCement40kg;
    }

    public void setConcreteCostCement40kg(double concreteCostCement40kg) {
	this.concreteCostCement40kg = concreteCostCement40kg;
    }

    public double getConcreteCostCement50kg() {
	return concreteCostCement50kg;
    }

    public void setConcreteCostCement50kg(double concreteCostCement50kg) {
	this.concreteCostCement50kg = concreteCostCement50kg;
    }

    public double getConcreteCostSand() {
	return concreteCostSand;
    }

    public void setConcreteCostSand(double concreteCostSand) {
	this.concreteCostSand = concreteCostSand;
    }

    public double getConcreteCostGravel() {
	return concreteCostGravel;
    }

    public void setConcreteCostGravel(double concreteCostGravel) {
	this.concreteCostGravel = concreteCostGravel;
    }

    public double getChbCostTotal() {
	return chbCostTotal;
    }

    public void setChbCostTotal(double chbCostTotal) {
	this.chbCostTotal = chbCostTotal;
    }

    public double getChbLayingCostBags40kg() {
	return chbLayingCostBags40kg;
    }

    public void setChbLayingCostBags40kg(double chbLayingCostBags40kg) {
	this.chbLayingCostBags40kg = chbLayingCostBags40kg;
    }

    public double getChbLayingCostBags50kg() {
	return chbLayingCostBags50kg;
    }

    public void setChbLayingCostBags50kg(double chbLayingCostBags50kg) {
	this.chbLayingCostBags50kg = chbLayingCostBags50kg;
    }

    public double getChbLayingCostSand() {
	return chbLayingCostSand;
    }

    public void setChbLayingCostSand(double chbLayingCostSand) {
	this.chbLayingCostSand = chbLayingCostSand;
    }

    public double getPlasteringCostCement40kg() {
	return plasteringCostCement40kg;
    }

    public void setPlasteringCostCement40kg(double plasteringCostCement40kg) {
	this.plasteringCostCement40kg = plasteringCostCement40kg;
    }

    public double getPlasteringCostCement50kg() {
	return plasteringCostCement50kg;
    }

    public void setPlasteringCostCement50kg(double plasteringCostCement50kg) {
	this.plasteringCostCement50kg = plasteringCostCement50kg;
    }

    public double getPlasteringCostSand() {
	return plasteringCostSand;
    }

    public void setPlasteringCostSand(double plasteringCostSand) {
	this.plasteringCostSand = plasteringCostSand;
    }

    public double getFootingCostCement40kg() {
	return footingCostCement40kg;
    }

    public void setFootingCostCement40kg(double footingCostCement40kg) {
	this.footingCostCement40kg = footingCostCement40kg;
    }

    public double getFootingCostCement50kg() {
	return footingCostCement50kg;
    }

    public void setFootingCostCement50kg(double footingCostCement50kg) {
	this.footingCostCement50kg = footingCostCement50kg;
    }

    public double getFootingCostSand() {
	return footingCostSand;
    }

    public void setFootingCostSand(double footingCostSand) {
	this.footingCostSand = footingCostSand;
    }

    public double getFootingCostGravel() {
	return footingCostGravel;
    }

    public void setFootingCostGravel(double footingCostGravel) {
	this.footingCostGravel = footingCostGravel;
    }

    public double getMrCHBSteelBar() {
	return mrCHBSteelBar;
    }

    public void setMrCHBSteelBar(double mrCHBSteelBar) {
	this.mrCHBSteelBar = mrCHBSteelBar;
    }

    public double getMrCHBTieWireKg() {
	return mrCHBTieWireKg;
    }

    public void setMrCHBTieWireKg(double mrCHBTieWireKg) {
	this.mrCHBTieWireKg = mrCHBTieWireKg;
    }

    public double getMrCHBTieWireRoll() {
	return mrCHBTieWireRoll;
    }

    public void setMrCHBTieWireRoll(double mrCHBTieWireRoll) {
	this.mrCHBTieWireRoll = mrCHBTieWireRoll;
    }

    public double getMrCHBCostSteelBar() {
	return mrCHBCostSteelBar;
    }

    public void setMrCHBCostSteelBar(double mrCHBCostSteelBar) {
	this.mrCHBCostSteelBar = mrCHBCostSteelBar;
    }

    public double getMrCHBCostTieWireKg() {
	return mrCHBCostTieWireKg;
    }

    public void setMrCHBCostTieWireKg(double mrCHBCostTieWireKg) {
	this.mrCHBCostTieWireKg = mrCHBCostTieWireKg;
    }

    public double getMrCHBCostTieWireRoll() {
	return mrCHBCostTieWireRoll;
    }

    public void setMrCHBCostTieWireRoll(double mrCHBCostTieWireRoll) {
	this.mrCHBCostTieWireRoll = mrCHBCostTieWireRoll;
    }

    public double getAreaBelowGround() {
	return areaBelowGround;
    }

    public void setAreaBelowGround(double areaBelowGround) {
	this.areaBelowGround = areaBelowGround;
    }

    public double getFootingHeight() {
	return footingHeight;
    }

    public void setFootingHeight(double footingHeight) {
	this.footingHeight = footingHeight;
    }

    public double getFootingWidth() {
	return footingWidth;
    }

    public void setFootingWidth(double footingWidth) {
	this.footingWidth = footingWidth;
    }

}
