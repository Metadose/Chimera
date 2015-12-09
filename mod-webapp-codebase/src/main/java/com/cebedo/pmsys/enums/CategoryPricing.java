package com.cebedo.pmsys.enums;

public enum CategoryPricing {

    /**
     * Projects owned in-house.
     */
    DEMO(0, "Demo", 0, false),

    /**
     * Projects owned by partner companies.
     */
    PARTNER_1(1, "Partner 1", 1, false),

    // Trial categories.

    TRIAL_1(2, "Trial Version 1", 1, false),

    // Paid categories.

    PER_PROJECT_1(3, "Per Project 1", 1, true),

    PER_YEAR_1(4, "Per Year 1", 1, true);

    private int id;
    private String label;
    private int tier;
    private boolean paid;

    CategoryPricing(int i, String lbl, int t, boolean pd) {
	this.id = i;
	this.label = lbl;
	this.tier = t;
	this.setPaid(pd);
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public boolean isPaid() {
	return paid;
    }

    public void setPaid(boolean paid) {
	this.paid = paid;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getTier() {
	return tier;
    }

    public void setTier(int tier) {
	this.tier = tier;
    }

}
