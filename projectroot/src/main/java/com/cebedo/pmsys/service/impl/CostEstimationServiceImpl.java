package com.cebedo.pmsys.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.CostEstimationBean;
import com.cebedo.pmsys.domain.ConcreteEstimationSummary;
import com.cebedo.pmsys.domain.MasonryCHBEstimationSummary;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.service.ConcreteEstimationSummaryService;
import com.cebedo.pmsys.service.CostEstimationService;
import com.cebedo.pmsys.service.MasonryCHBEstimationSummaryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

public class CostEstimationServiceImpl implements CostEstimationService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

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

    @Transactional
    @Override
    public String estimateCosts(Project proj, CostEstimationBean costEstimationBean) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ESTIMATE, Project.OBJECT_NAME, proj.getId(),
		CostEstimationBean.class.getName());

	// Construct objects.
	ConcreteEstimationSummary estimationSummary = new ConcreteEstimationSummary(proj,
		costEstimationBean);
	MasonryCHBEstimationSummary masonryCHBEstimationSummary = new MasonryCHBEstimationSummary(proj,
		costEstimationBean);

	// Do service.
	// Concrete.
	this.concreteEstimationSummaryService.set(estimationSummary);

	// Masonry.
	this.masonryCHBEstimationSummaryService.set(masonryCHBEstimationSummary);

	return AlertBoxGenerator.SUCCESS.generateCreate(CostEstimationBean.BEAN_NAME_DISPLAY,
		costEstimationBean.getName());
    }

}
