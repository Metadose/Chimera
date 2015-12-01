package com.cebedo.pmsys.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.base.IObjectExpense;
import com.cebedo.pmsys.constants.RegistryCache;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.model.Project;

public interface ExpenseService {

    public HSSFWorkbook exportXLS(long projID);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String delete(String key, long projectId);

    public Expense get(String uuid);

    public List<Expense> listDesc(Project proj);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#obj.project.id")
    public String set(Expense obj, BindingResult result);

    public List<Expense> listAsc(Project proj);

    public List<Expense> listDesc(Project proj, Date startDate, Date endDate);

    public List<IObjectExpense> listDescExpense(Project proj);

    public List<IObjectExpense> listDescExpense(Project proj, Date startDate, Date endDate);

    public List<Expense> listAsc(Project proj, boolean override);

}