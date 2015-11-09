package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.impl.EstimationOutputValueRepoImpl;
import com.cebedo.pmsys.service.EstimationOutputService;

@Service
public class EstimationOutputServiceImpl implements EstimationOutputService {

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private EstimationOutputValueRepoImpl estimationOutputValueRepo;

    public void setEstimationOutputValueRepo(EstimationOutputValueRepoImpl estimationOutputValueRepo) {
	this.estimationOutputValueRepo = estimationOutputValueRepo;
    }

    @Override
    @Transactional
    public EstimationOutput get(String key) {
	EstimationOutput obj = this.estimationOutputValueRepo.get(key);

	// Security check.
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_ESTIMATION_OUTPUT, obj.getKey());
	    return new EstimationOutput();
	}

	// Log.
	this.messageHelper.nonAuditableKeyNoAssoc(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_ESTIMATION_OUTPUT,
		obj.getKey());

	return obj;
    }

    @Override
    @Transactional
    public String delete(String key) {
	EstimationOutput obj = this.estimationOutputValueRepo.get(key);

	// Security check.
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_ESTIMATION_OUTPUT, obj.getKey());
	    return AlertBoxFactory.ERROR;
	}

	// Log.
	Project proj = obj.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_ESTIMATION_OUTPUT, obj.getKey(), proj, obj.getName());

	this.estimationOutputValueRepo.delete(key);
	return AlertBoxFactory.SUCCESS.generateDelete(ConstantsRedis.OBJECT_ESTIMATE, obj.getName());
    }

    @Override
    @Transactional
    public List<EstimationOutput> listDesc(Project proj) {

	// Security check.
	if (!this.authHelper.hasAccess(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<EstimationOutput>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_ESTIMATION_OUTPUT);

	String pattern = EstimationOutput.constructPattern(proj);
	Set<String> keys = this.estimationOutputValueRepo.keys(pattern);

	List<EstimationOutput> estimateList = this.estimationOutputValueRepo.multiGet(keys);

	// Sort the list in descending order.
	Collections.sort(estimateList, new Comparator<EstimationOutput>() {
	    @Override
	    public int compare(EstimationOutput aObj, EstimationOutput bObj) {
		Date aStart = aObj.getLastComputed();
		Date bStart = bObj.getLastComputed();

		// To sort in ascending,
		// remove Not's.
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});
	return estimateList;
    }

}
