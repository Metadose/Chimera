package com.cebedo.pmsys.enums;

public enum TableEstimationAllowance {

    ALLOWANCE_1("1%", 0.01),

    ALLOWANCE_2("2%", 0.02),

    ALLOWANCE_3("3%", 0.03),

    ALLOWANCE_4("4%", 0.04),

    ALLOWANCE_5("5%", 0.05),

    ALLOWANCE_6("6%", 0.06),

    ALLOWANCE_7("7%", 0.07),

    ALLOWANCE_8("8%", 0.08),

    ALLOWANCE_9("9%", 0.09),

    ALLOWANCE_10("10%", 0.10),

    ALLOWANCE_11("11%", 0.11),

    ALLOWANCE_12("12%", 0.12),

    ALLOWANCE_13("13%", 0.13),

    ALLOWANCE_14("14%", 0.14),

    ALLOWANCE_15("15%", 0.15),

    ALLOWANCE_16("16%", 0.16),

    ALLOWANCE_17("17%", 0.17),

    ALLOWANCE_18("18%", 0.18),

    ALLOWANCE_19("19%", 0.19),

    ALLOWANCE_20("20%", 0.20),

    ALLOWANCE_21("21%", 0.21),

    ALLOWANCE_22("22%", 0.22),

    ALLOWANCE_23("23%", 0.23),

    ALLOWANCE_24("24%", 0.24),

    ALLOWANCE_25("25%", 0.25),

    ALLOWANCE_26("26%", 0.26),

    ALLOWANCE_27("27%", 0.27),

    ALLOWANCE_28("28%", 0.28),

    ALLOWANCE_29("29%", 0.29),

    ALLOWANCE_30("30%", 0.30),

    ALLOWANCE_31("31%", 0.31),

    ALLOWANCE_32("32%", 0.32),

    ALLOWANCE_33("33%", 0.33),

    ALLOWANCE_34("34%", 0.34),

    ALLOWANCE_35("35%", 0.35),

    ALLOWANCE_36("36%", 0.36),

    ALLOWANCE_37("37%", 0.37),

    ALLOWANCE_38("38%", 0.38),

    ALLOWANCE_39("39%", 0.39),

    ALLOWANCE_40("40%", 0.40),

    ALLOWANCE_41("41%", 0.41),

    ALLOWANCE_42("42%", 0.42),

    ALLOWANCE_43("43%", 0.43),

    ALLOWANCE_44("44%", 0.44),

    ALLOWANCE_45("45%", 0.45),

    ALLOWANCE_46("46%", 0.46),

    ALLOWANCE_47("47%", 0.47),

    ALLOWANCE_48("48%", 0.48),

    ALLOWANCE_49("49%", 0.49),

    ALLOWANCE_50("50%", 0.50);

    private String label;
    private double allowance;

    TableEstimationAllowance(String label, double allowance) {
	this.label = label;
	this.allowance = allowance;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public double getAllowance() {
	return allowance;
    }

    public void setAllowance(double allowance) {
	this.allowance = allowance;
    }

}
