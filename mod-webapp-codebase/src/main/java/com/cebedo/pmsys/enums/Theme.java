package com.cebedo.pmsys.enums;

public enum Theme {

    ORANGE("orange", "Industrial"),

    BLUE("blue", "Corporate"),

    GREEN("green", "Growth"),

    RED("red", "Deep"),

    YELLOW("yellow", "Voltage");

    public static final Theme DEFAULT = ORANGE;

    private String id;
    private String label;

    Theme(String id, String label) {
	this.id = id;
	this.label = label;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

}
