package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.EstimateCostValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.EstimateCostService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class EstimateCostServiceImpl implements EstimateCostService {

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();

    private EstimateCostValueRepo estimateCostValueRepo;
    private ProjectAuxValueRepo projectAuxValueRepo;

    @Autowired
    @Qualifier(value = "projectAuxValueRepo")
    public void setProjectAuxValueRepo(ProjectAuxValueRepo projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    @Autowired
    @Qualifier(value = "estimateCostValueRepo")
    public void setEstimateCostValueRepo(EstimateCostValueRepo estimateCostValueRepo) {
	this.estimateCostValueRepo = estimateCostValueRepo;
    }

    @Transactional
    @Override
    public String delete(String key) {
	EstimateCost obj = get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getKey());

	// Project auxiliary on grand totals of costs.
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(obj.getProject()));
	EstimateCostType costType = obj.getCostType();
	double actualCost = obj.getActualCost();
	double estimatedCost = obj.getCost();
	// Direct cost.
	if (costType == EstimateCostType.DIRECT) {
	    aux.setGrandTotalCostsDirect(aux.getGrandTotalCostsDirect() - estimatedCost);
	    aux.setGrandTotalActualCostsDirect(aux.getGrandTotalActualCostsDirect() - actualCost);
	}
	// If cost is indirect.
	else if (costType == EstimateCostType.INDIRECT) {
	    aux.setGrandTotalCostsIndirect(aux.getGrandTotalCostsIndirect() - estimatedCost);
	    aux.setGrandTotalActualCostsIndirect(aux.getGrandTotalActualCostsIndirect() - actualCost);
	}

	this.estimateCostValueRepo.delete(key);
	this.projectAuxValueRepo.set(aux);
	return AlertBoxGenerator.SUCCESS.generateDelete(ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getName());
    }

    @Transactional
    @Override
    public EstimateCost get(String uuid) {
	// TODO Auto-generated method stub
	return null;
    }

    @Transactional
    @Override
    public List<EstimateCost> list(Project proj) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<EstimateCost>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_ESTIMATE_COST);

	String pattern = EstimateCost.constructPattern(proj);
	Set<String> keys = this.estimateCostValueRepo.keys(pattern);

	List<EstimateCost> costs = this.estimateCostValueRepo.multiGet(keys);
	return costs;
    }

    @Transactional
    @Override
    public String set(EstimateCost obj) {
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// If we're creating.
	boolean isCreate = false;
	if (obj.getUuid() == null) {
	    isCreate = true;
	    obj.setUuid(UUID.randomUUID());
	}

	// Project auxiliary on grand totals of costs.
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(obj.getProject()));
	EstimateCostType costType = obj.getCostType();
	double actualCost = obj.getActualCost();
	double estimatedCost = obj.getCost();
	// Direct cost.
	if (costType == EstimateCostType.DIRECT) {
	    aux.setGrandTotalCostsDirect(aux.getGrandTotalCostsDirect() + estimatedCost);
	    aux.setGrandTotalActualCostsDirect(aux.getGrandTotalActualCostsDirect() + actualCost);
	}
	// If cost is indirect.
	else if (costType == EstimateCostType.INDIRECT) {
	    aux.setGrandTotalCostsIndirect(aux.getGrandTotalCostsIndirect() + estimatedCost);
	    aux.setGrandTotalActualCostsIndirect(aux.getGrandTotalActualCostsIndirect() + actualCost);
	}

	// Do the action.
	// Return success.
	this.estimateCostValueRepo.set(obj);
	this.projectAuxValueRepo.set(aux);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_SET, ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getKey());

	if (isCreate) {
	    return AlertBoxGenerator.SUCCESS.generateCreate(ConstantsRedis.OBJECT_ESTIMATE_COST,
		    obj.getName());
	}
	return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getName());
    }

}
