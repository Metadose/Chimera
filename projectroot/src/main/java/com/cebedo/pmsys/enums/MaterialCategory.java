package com.cebedo.pmsys.enums;

public enum MaterialCategory {

    CEMENT("Cement"),

    SAND("Gravel"),

    GRAVEL("Sand"),

    CHB("Concrete Hollow Blocks (CHB)");

    private String label;

    MaterialCategory(String lbl) {
	this.setLabel(lbl);
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

}
