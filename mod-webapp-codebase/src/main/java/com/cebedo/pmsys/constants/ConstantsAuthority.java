package com.cebedo.pmsys.constants;

public class ConstantsAuthority {

    public static final String ADMIN_COMPANY = "ADMIN_COMPANY";
    public static final String ADMIN_SUPER = "ADMIN_SUPER";

    /**
     * Project Modules.
     */
    public enum AuthorizedProjectModule {

	CONTRACT("Contract"),

	ESTIMATE("Estimate"),

	STAFF("Staff"),

	PAYROLL("Payroll"),

	INVENTORY("Inventory"),

	EQUIPMENT("Equipment"),

	OTHER_EXPENSES("Other Expenses"),

	PROGRAM_OF_WORKS("Program of Works"),

	DOWNLOADS("Downloads");

	private String label;

	AuthorizedProjectModule(String lbl) {
	    this.label = lbl;
	}

	public String getLabel() {
	    return label;
	}
    }

    /**
     * Actions.
     */
    public enum AuthorizedAction {
	VIEW("View"), CREATE("Create"), UPDATE("Update"), DELETE("Delete");

	private String label;

	AuthorizedAction(String lbl) {
	    this.label = lbl;
	}

	public String getLabel() {
	    return label;
	}
    }

}
