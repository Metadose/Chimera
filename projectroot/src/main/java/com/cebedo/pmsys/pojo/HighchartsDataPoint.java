package com.cebedo.pmsys.pojo;

public class HighchartsDataPoint {

    private String name;
    private double y;
    private String color;

    // Pie.
    private boolean sliced;
    private boolean selected;

    public HighchartsDataPoint(String n, double y, String col) {
	setName(n);
	setY(y);
	setColor(col);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public double getY() {
	return y;
    }

    public void setY(double y) {
	this.y = y;
    }

    public boolean isSliced() {
	return sliced;
    }

    public void setSliced(boolean sliced) {
	this.sliced = sliced;
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

}
