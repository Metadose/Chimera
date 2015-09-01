package com.cebedo.pmsys.service;

import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.bean.EstimateComputationInputBean;

public interface EstimateService {

    public String estimate(EstimateComputationInputBean estimateInput, BindingResult result);

}
