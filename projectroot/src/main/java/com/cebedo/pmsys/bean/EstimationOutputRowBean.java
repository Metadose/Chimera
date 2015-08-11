package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.util.UUID;

public class EstimationOutputRowBean implements Serializable {

    private static final long serialVersionUID = 5377007792468931713L;

    // Details.
    private String uuid;
    private String name;
    private String remarks;
    private double area;
    private double volume;
    private double chbFoundationHeight;
    private double footingLength;

    // Concrete.
    private double concreteCement40kg;
    private double concreteCement50kg;
    private double concreteSand;
    private double concreteGravel;

    // CHB.
    private double totalCHB;

    // CHB Laying.
    private double chbLayingBags40kg;
    private double chbLayingBags50kg;
    private double chbLayingSand;

    // Platering.
    private double plasteringCement40kg;
    private double plasteringCement50kg;
    private double plasteringSand;

    // CHB Footing.
    private double footingCement40kg;
    private double footingCement50kg;
    private double footingSand;
    private double footingGravel;

    public EstimationOutputRowBean() {
	;
    }

    public EstimationOutputRowBean(EstimateBean estimateBean) {

	ShapeBean shapeBean = estimateBean.getShape();
	ConcreteEstimateResults concrete = estimateBean.getResultConcreteEstimate();
	MasonryCHBEstimateResults chb = estimateBean.getResultCHBEstimate();
	MasonryCHBLayingEstimateResults chbLaying = estimateBean.getResultCHBLayingEstimate();
	MasonryPlasteringEstimateResults plaster = estimateBean.getResultPlasteringEstimate();
	MasonryCHBFootingEstimateResults footing = estimateBean.getResultCHBFootingEstimate();

	this.uuid = UUID.randomUUID().toString();
	this.name = estimateBean.getName();
	this.remarks = estimateBean.getRemarks();
	this.area = shapeBean.getOriginalArea();
	this.volume = shapeBean.getOriginalVolume();
	this.chbFoundationHeight = estimateBean.getChbFoundationHeight();
	this.footingLength = shapeBean.getFootingLength();

	this.concreteCement40kg = concrete.getCement40kg();
	this.concreteCement50kg = concrete.getCement50kg();
	this.concreteSand = concrete.getSand();
	this.concreteGravel = concrete.getGravel();

	this.totalCHB = chb.getTotalCHB();

	double chbBags40 = chbLaying.getCement40kg();
	this.chbLayingBags40kg = chbBags40;
	this.chbLayingBags50kg = chbBags40 - (chbBags40 * 0.2);
	this.chbLayingSand = chbLaying.getSand();

	this.plasteringCement40kg = plaster.getCement40kg();
	this.plasteringCement50kg = plaster.getCement50kg();
	this.plasteringSand = plaster.getSand();

	double footingBags40 = footing.getCement40kg();
	this.footingCement40kg = footingBags40;
	this.footingCement50kg = footingBags40 - (footingBags40 * 0.2);
	this.footingSand = footing.getSand();
	this.footingGravel = footing.getGravel();
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

    public double getChbFoundationHeight() {
	return chbFoundationHeight;
    }

    public void setChbFoundationHeight(double chbFoundationHeight) {
	this.chbFoundationHeight = chbFoundationHeight;
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

    public double getTotalCHB() {
	return totalCHB;
    }

    public void setTotalCHB(double totalCHB) {
	this.totalCHB = totalCHB;
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

}
