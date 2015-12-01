package com.cebedo.pmsys.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.base.IObjectExpense;
import com.cebedo.pmsys.constants.RegistryCache;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.model.Project;

public interface EquipmentExpenseService {

    public HSSFWorkbook exportXLS(long projID);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String delete(String key, long projectId);

    public EquipmentExpense get(String uuid);

    public List<EquipmentExpense> listDesc(Project proj);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#obj.project.id")
    public String set(EquipmentExpense obj, BindingResult result);

    public List<EquipmentExpense> listAsc(Project proj);

    public List<EquipmentExpense> listDesc(Project proj, Date startDate, Date endDate);

    public List<IObjectExpense> listDescExpense(Project proj);

    public List<IObjectExpense> listDescExpense(Project proj, Date startDate, Date endDate);

    public List<EquipmentExpense> listAsc(Project proj, boolean override);
}