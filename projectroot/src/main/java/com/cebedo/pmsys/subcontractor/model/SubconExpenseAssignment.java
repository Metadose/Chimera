package com.cebedo.pmsys.subcontractor.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.cashflow.expense.model.Expense;

@Entity
@Table(name = SubconExpenseAssignment.TABLE_NAME)
public class SubconExpenseAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_subcon_expense";
	private static final long serialVersionUID = 1L;

	private long expenseID;
	private long subconID;

	@Id
	@Column(name = Expense.COLUMN_PRIMARY_KEY, nullable = false)
	public long getExpenseID() {
		return expenseID;
	}

	public void setExpenseID(long expenseID) {
		this.expenseID = expenseID;
	}

	@Id
	@Column(name = Subcontractor.COLUMN_PRIMARY_KEY, nullable = false)
	public long getSubcontractorID() {
		return subconID;
	}

	public void setSubcontractorID(long subconID) {
		this.subconID = subconID;
	}
}
