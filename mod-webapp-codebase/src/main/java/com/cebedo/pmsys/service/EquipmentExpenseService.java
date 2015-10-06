package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.model.Project;

public interface EquipmentExpenseService {

    public HSSFWorkbook exportXLS(long projID);

    public String delete(String key);

    public EquipmentExpense get(String uuid);

    public List<EquipmentExpense> listDesc(Project proj);

    public String set(EquipmentExpense cost, BindingResult result);

}