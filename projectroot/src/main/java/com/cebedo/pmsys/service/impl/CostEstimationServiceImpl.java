package com.cebedo.pmsys.service.impl;

import com.cebedo.pmsys.bean.CostEstimationBean;
import com.cebedo.pmsys.domain.ConcreteEstimationSummary;
import com.cebedo.pmsys.domain.MasonryEstimationSummary;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.service.ConcreteEstimationSummaryService;
import com.cebedo.pmsys.service.CostEstimationService;
import com.cebedo.pmsys.service.MasonryEstimationSummaryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

public class CostEstimationServiceImpl implements CostEstimationService {

    private ConcreteEstimationSummaryService concreteEstimationSummaryService;
    private MasonryEstimationSummaryService masonryEstimationSummaryService;

    public void setConcreteEstimationSummaryService(
	    ConcreteEstimationSummaryService concreteEstimationSummaryService) {
	this.concreteEstimationSummaryService = concreteEstimationSummaryService;
    }

    public void setMasonryEstimationSummaryService(
	    MasonryEstimationSummaryService masonryEstimationSummaryService) {
	this.masonryEstimationSummaryService = masonryEstimationSummaryService;
    }

    @Override
    public String estimateCosts(Project proj,
	    CostEstimationBean costEstimationBean) {

	// Construct objects.
	ConcreteEstimationSummary estimationSummary = new ConcreteEstimationSummary(
		proj, costEstimationBean);
	MasonryEstimationSummary masonryEstimationSummary = new MasonryEstimationSummary(
		proj, costEstimationBean);

	// Do service.
	// Concrete.
	this.concreteEstimationSummaryService.set(estimationSummary);

	// Masonry.
	this.masonryEstimationSummaryService.set(masonryEstimationSummary);

	return AlertBoxGenerator.SUCCESS.generateCreate(
		CostEstimationBean.BEAN_NAME_DISPLAY,
		costEstimationBean.getName());
    }

}
