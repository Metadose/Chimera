package com.cebedo.pmsys.enums;

public enum CategoryMaterial {

    CEMENT("Cement"),

    SAND("Gravel"),

    GRAVEL("Sand"),

    CHB("Concrete Hollow Blocks (CHB)"),

    TIE_WIRE("Tie Wire"),

    METAL_BAR("Metal Bar"),

    EQUIPMENT("Equipment"),

    LUMBER("Lumber"),

    PLYWOOD("Plywood"),

    NYLON_STRING("Nylon String"),

    NAIL("Nail"),

    SCREW("Screw"),

    DOOR("Door"),

    WINDOW("Window"),

    ROOFING("Roofing"),

    WATER_LINES("Water Lines"),

    TILES("Tile"),

    SEALANT("Sealant"),

    ADHESIVE("Adhesive"),

    SCREEN("Screen"),

    SEPTIC_TANK("Septic Tank"),

    PLUMBING_FIXTURES("Plumbing Fixtures"),

    SANITARY_PIPELINES("Sanitary Pipelines"),

    ELECTRICAL_WIRES("Electrical Wires"),

    ELECTRICAL_FIXTURES("Electrical Fixtures"),

    AMENITIES("Amenities"),

    SIGNAGE("Signage"),

    OTHERS("Others");

    private String label;

    CategoryMaterial(String lbl) {
	this.setLabel(lbl);
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

}
