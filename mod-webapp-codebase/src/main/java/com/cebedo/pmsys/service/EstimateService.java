package com.cebedo.pmsys.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.bean.EstimateComputationInput;

public interface EstimateService {

    public HSSFWorkbook exportXLS(String key);

    public String estimate(EstimateComputationInput estimateInput, BindingResult result);

}
