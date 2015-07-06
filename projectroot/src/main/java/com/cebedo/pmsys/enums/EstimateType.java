package com.cebedo.pmsys.enums;

public enum EstimateType {

    CONCRETE(1, "Concrete"),

    MASONRY_CHB(2, "Masonry (CHB)"),

    MASONRY_BLOCK_LAYING(3, "Masonry (Block Laying)"),

    MASONRY_PLASTERING(4, "Masonry (Plastering)"),

    MASONRY_CHB_FOOTING(5, "Masonry (CHB Footing)"),

    METAL_REINFORCEMENT(6, "Metal Reinforcement");

    int id;
    String label;

    EstimateType(int id, String lbl) {
	this.id = id;
	this.label = lbl;
    }

    public static EstimateType of(int typeID) {
	if (typeID == MASONRY_CHB.id()) {
	    return MASONRY_CHB;
	}

	if (typeID == MASONRY_BLOCK_LAYING.id()) {
	    return MASONRY_BLOCK_LAYING;
	}

	if (typeID == MASONRY_PLASTERING.id()) {
	    return MASONRY_PLASTERING;
	}

	else if (typeID == CONCRETE.id()) {
	    return CONCRETE;
	}

	else if (typeID == MASONRY_CHB_FOOTING.id()) {
	    return MASONRY_CHB_FOOTING;
	}

	else if (typeID == METAL_REINFORCEMENT.id()) {
	    return METAL_REINFORCEMENT;
	}

	return MASONRY_CHB;
    }

    public String label() {
	return this.label;
    }

    public int id() {
	return this.id;
    }

}