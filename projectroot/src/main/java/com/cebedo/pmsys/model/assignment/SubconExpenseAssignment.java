package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

//@Entity
//@Table(name = SubconExpenseAssignment.TABLE_NAME)
public class SubconExpenseAssignment implements Serializable {

    private static final long serialVersionUID = -7126025190921443816L;

    public static final String TABLE_NAME = "assignments_subcon_expense";

    private long expenseID;
    private long subconID;

    // @Id
    // @Column(name = Expense.COLUMN_PRIMARY_KEY, nullable = false)
    public long getExpenseID() {
	return expenseID;
    }

    public void setExpenseID(long expenseID) {
	this.expenseID = expenseID;
    }

    // @Id
    // @Column(name = Subcontractor.COLUMN_PRIMARY_KEY, nullable = false)
    public long getSubcontractorID() {
	return subconID;
    }

    public void setSubcontractorID(long subconID) {
	this.subconID = subconID;
    }
}
