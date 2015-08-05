package com.cebedo.pmsys.enums;

public enum MaterialCategory {

    CEMENT("Cement"),

    SAND("Gravel"),

    GRAVEL("Sand"),

    CHB("Concrete Hollow Blocks (CHB)");

    private String label;

    MaterialCategory(String lbl) {
	this.label = lbl;
    }

}
