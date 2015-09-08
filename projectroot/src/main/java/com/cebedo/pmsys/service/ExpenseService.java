package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.model.Project;

public interface ExpenseService {

    public String delete(String key);

    public Expense get(String uuid);

    public List<Expense> list(Project proj);

    public String set(Expense cost);

}