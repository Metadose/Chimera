package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Expense;

public interface ExpenseDAO {

    public void create(Expense expense);

    public Expense getByID(long id);

    public void update(Expense expense);

    public void delete(long id);

    public List<Expense> list();

}