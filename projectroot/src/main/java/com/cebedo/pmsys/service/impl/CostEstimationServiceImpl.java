package com.cebedo.pmsys.service.impl;

import com.cebedo.pmsys.bean.CostEstimationBean;
import com.cebedo.pmsys.domain.ConcreteEstimationSummary;
import com.cebedo.pmsys.domain.MasonryCHBEstimationSummary;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.service.ConcreteEstimationSummaryService;
import com.cebedo.pmsys.service.CostEstimationService;
import com.cebedo.pmsys.service.MasonryCHBEstimationSummaryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

public class CostEstimationServiceImpl implements CostEstimationService {

    private ConcreteEstimationSummaryService concreteEstimationSummaryService;
    private MasonryCHBEstimationSummaryService masonryCHBEstimationSummaryService;

    public void setConcreteEstimationSummaryService(
	    ConcreteEstimationSummaryService concreteEstimationSummaryService) {
	this.concreteEstimationSummaryService = concreteEstimationSummaryService;
    }

    public void setMasonryCHBEstimationSummaryService(
	    MasonryCHBEstimationSummaryService masonryCHBEstimationSummaryService) {
	this.masonryCHBEstimationSummaryService = masonryCHBEstimationSummaryService;
    }

    @Override
    public String estimateCosts(Project proj,
	    CostEstimationBean costEstimationBean) {

	// Construct objects.
	ConcreteEstimationSummary estimationSummary = new ConcreteEstimationSummary(
		proj, costEstimationBean);
	MasonryCHBEstimationSummary masonryCHBEstimationSummary = new MasonryCHBEstimationSummary(
		proj, costEstimationBean);

	// Do service.
	// Concrete.
	this.concreteEstimationSummaryService.set(estimationSummary);

	// Masonry.
	this.masonryCHBEstimationSummaryService.set(masonryCHBEstimationSummary);

	return AlertBoxGenerator.SUCCESS.generateCreate(
		CostEstimationBean.BEAN_NAME_DISPLAY,
		costEstimationBean.getName());
    }

}
