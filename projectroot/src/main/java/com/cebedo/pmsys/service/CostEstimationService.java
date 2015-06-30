package com.cebedo.pmsys.service;

import com.cebedo.pmsys.bean.CostEstimationBean;
import com.cebedo.pmsys.model.Project;

public interface CostEstimationService {

    public String estimateCosts(Project proj,
	    CostEstimationBean costEstimationBean);

}
