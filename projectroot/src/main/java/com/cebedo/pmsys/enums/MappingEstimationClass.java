package com.cebedo.pmsys.enums;

public enum MappingEstimationClass {

    CLASS_A("Class A", "A", TableProportionConcrete.CLASS_AA);

    private String label;
    private String mixClass;
    private TableProportionConcrete concreteProportion;

    MappingEstimationClass(String label, String mixClass, TableProportionConcrete concreteProportion) {
	this.mixClass = mixClass;
	this.label = label;
	this.concreteProportion = concreteProportion;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public TableProportionConcrete getConcreteProportion() {
	return concreteProportion;
    }

    public void setConcreteProportion(TableProportionConcrete concreteProportion) {
	this.concreteProportion = concreteProportion;
    }

    public String getMixClass() {
	return mixClass;
    }

    public void setMixClass(String mixClass) {
	this.mixClass = mixClass;
    }

    public TableMixturePlaster getPlasterMixture() {
	String proportionMixClass = getConcreteProportion().getMixClass();

	// Find the plaster mix.
	TableMixturePlaster plasterMixture = TableMixturePlaster.CLASS_A;
	for (TableMixturePlaster plasterMix : TableMixturePlaster.class.getEnumConstants()) {

	    String plasterMixClass = plasterMix.getMixClass();
	    if (plasterMixClass.equals(proportionMixClass)) {
		plasterMixture = plasterMix;
		break;
	    }
	}
	return plasterMixture;
    }

}
