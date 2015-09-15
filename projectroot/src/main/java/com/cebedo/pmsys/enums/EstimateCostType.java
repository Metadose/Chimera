package com.cebedo.pmsys.enums;

public enum EstimateCostType {

    DIRECT(0, "Direct"),

    INDIRECT(1, "Indirect");

    EstimateCostType(int i, String lbl) {
	setId(i);
	setLabel(lbl);
    }

    private int id;
    private String label;

    public static EstimateCostType of(String label) {
	for (EstimateCostType costType : EstimateCostType.values()) {
	    if (costType.getLabel().equals(label)) {
		return costType;
	    }
	}
	return DIRECT;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

}
