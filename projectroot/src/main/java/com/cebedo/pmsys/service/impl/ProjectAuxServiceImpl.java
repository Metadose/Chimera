package com.cebedo.pmsys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.ProjectAuxService;

@Service
public class ProjectAuxServiceImpl implements ProjectAuxService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ProjectAuxValueRepo projectAuxValueRepo;

    public void setProjectAuxValueRepo(ProjectAuxValueRepo projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    @Override
    @Transactional
    public void set(ProjectAux obj) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_PROJECT_AUX, obj.getKey());
	    return;
	}
	// Log.
	this.messageHelper.send(AuditAction.SET, RedisConstants.OBJECT_PROJECT_AUX, obj.getKey());
	this.projectAuxValueRepo.set(obj);
    }

    @Override
    @Transactional
    public ProjectAux get(String key) {
	ProjectAux obj = this.projectAuxValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_PROJECT_AUX, obj.getKey());
	    return new ProjectAux();
	}
	// Log.
	this.messageHelper.send(AuditAction.GET, RedisConstants.OBJECT_PROJECT_AUX, obj.getKey());

	return obj;
    }

    @Override
    @Transactional
    public ProjectAux get(Delivery delivery) {
	String key = ProjectAux.constructKey(delivery.getProject());
	return this.get(key);
    }

    @Override
    @Transactional
    public ProjectAux get(Project proj) {
	String key = ProjectAux.constructKey(proj);
	return this.get(key);
    }

}
