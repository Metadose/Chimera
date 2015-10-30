package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.base.AbstractExpense;

public class StatisticsExpense extends SummaryStatistics {

    private static final long serialVersionUID = -7326896395077708119L;

    private List<AbstractExpense> expenses = new ArrayList<AbstractExpense>();
    private List<AbstractExpense> maxExpenses = new ArrayList<AbstractExpense>();
    private List<AbstractExpense> minExpenses = new ArrayList<AbstractExpense>();

    public StatisticsExpense() {
	;
    }

    @SuppressWarnings("unchecked")
    public StatisticsExpense(List<? extends AbstractExpense> exp) {
	this.expenses = (List<AbstractExpense>) exp;
	initValues();
	initMaxExpenses();
	initMinExpenses();
    }

    private void initMinExpenses() {
	double comparator = getMin();
	for (AbstractExpense expense : this.expenses) {
	    if (comparator == expense.getCost()) {
		this.minExpenses.add(expense);
	    }
	}
    }

    private void initMaxExpenses() {
	double comparator = getMax();
	for (AbstractExpense expense : this.expenses) {
	    if (comparator == expense.getCost()) {
		this.maxExpenses.add(expense);
	    }
	}
    }

    private void initValues() {
	for (AbstractExpense expense : this.expenses) {
	    addValue(expense.getCost());
	}
    }

    public List<AbstractExpense> getMaxExpenses() {
	return maxExpenses;
    }

    public List<AbstractExpense> getMinExpenses() {
	return minExpenses;
    }

}