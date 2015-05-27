package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.ExpenseDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Expense;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private AuthHelper authHelper = new AuthHelper();
    private ExpenseDAO expenseDAO;

    public void setExpenseDAO(ExpenseDAO expenseDAO) {
	this.expenseDAO = expenseDAO;
    }

    @Override
    @Transactional
    public void create(Expense expense) {
	if (this.authHelper.isActionAuthorized(expense)) {
	    this.expenseDAO.create(expense);
	}
    }

    @Override
    @Transactional
    public Expense getByID(long id) {
	Expense expense = this.expenseDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(expense)) {
	    return expense;
	}
	return new Expense();
    }

    @Override
    @Transactional
    public void update(Expense expense) {
	if (this.authHelper.isActionAuthorized(expense)) {
	    this.expenseDAO.update(expense);
	}
    }

    @Override
    @Transactional
    public void delete(long id) {
	Expense expense = this.expenseDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(expense)) {
	    this.expenseDAO.delete(id);
	}
    }

    @Override
    @Transactional
    public List<Expense> list() {
	return this.expenseDAO.list();
    }

}
