package com.cebedo.pmsys.enums;

public enum EstimateType {
    CONCRETE(1, "Concrete"), MASONRY(2, "Masonry"), METAL_REINFORCEMENT(3,
	    "Metal Reinforcement");

    int id;
    String label;

    EstimateType(int id, String lbl) {
	this.id = id;
	this.label = lbl;
    }

    public static EstimateType of(int typeID) {
	if (typeID == MASONRY.id()) {
	    return MASONRY;
	}

	else if (typeID == CONCRETE.id()) {
	    return CONCRETE;
	}

	else if (typeID == METAL_REINFORCEMENT.id()) {
	    return METAL_REINFORCEMENT;
	}

	return MASONRY;
    }

    public String label() {
	return this.label;
    }

    public int id() {
	return this.id;
    }

}