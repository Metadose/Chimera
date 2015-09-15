package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.model.Project;

public interface ExpenseService {

    public HSSFWorkbook exportXLS(long projID);

    public String delete(String key);

    public Expense get(String uuid);

    public List<Expense> listAsc(Project proj);

    public String set(Expense cost, BindingResult result);

}