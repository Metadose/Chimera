package com.cebedo.pmsys.bean;

import java.io.Serializable;

public class ShapeBean implements Serializable {

    private static final long serialVersionUID = -8565430166993517307L;

    private double area;
    private double volume;
    private double originalArea;
    private double originalVolume;
    private double footingLength;

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

    public double getOriginalArea() {
	return originalArea;
    }

    public void setOriginalArea(double originalArea) {
	this.originalArea = originalArea;
    }

    public double getOriginalVolume() {
	return originalVolume;
    }

    public void setOriginalVolume(double originalVolume) {
	this.originalVolume = originalVolume;
    }
}
