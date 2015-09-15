package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.model.Project;

public interface DeliveryService {

    public HSSFWorkbook exportXLS(long projID);

    public String set(Delivery obj, BindingResult result);

    public String delete(String key);

    public Delivery get(String uuid);

    public List<Delivery> listDesc(Project proj);

    public List<Delivery> listAsc(Project proj);

}
