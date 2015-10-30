package com.cebedo.pmsys.constants;

import java.io.Serializable;

public class ConstantsAuthority implements Serializable {

    private static final long serialVersionUID = 3115704947882396322L;

    public static final String ADMIN_COMPANY = "ADMIN_COMPANY";

    public static final String ADMIN_SUPER = "ADMIN_SUPER";

    /**
     * Modules.
     */
    public enum AuthorizedModule {

	CONTRACT("Contract"),

	ESTIMATE("Estimate"),

	STAFF("Staff"),

	PAYROLL("Payroll"),

	INVENTORY("Inventory"),

	EQUIPMENT("Equipment"),

	OTHER_EXPENSES("Other Expenses"),

	PROGRAM_OF_WORKS("Program of Works"),

	DOWNLOADS("Downloads"),

	LOGS("Logs");

	private String label;

	AuthorizedModule(String lbl) {
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
