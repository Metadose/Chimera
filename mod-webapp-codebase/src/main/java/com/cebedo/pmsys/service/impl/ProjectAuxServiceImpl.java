package com.cebedo.pmsys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.impl.ProjectAuxValueRepoImpl;
import com.cebedo.pmsys.service.ProjectAuxService;

@Service
public class ProjectAuxServiceImpl implements ProjectAuxService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ProjectAuxValueRepoImpl projectAuxValueRepo;

    public void setProjectAuxValueRepo(ProjectAuxValueRepoImpl projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    @Override
    @Transactional
    public ProjectAux get(String key) {
	ProjectAux obj = this.projectAuxValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_AUX_PROJECT, obj.getKey());
	    return new ProjectAux();
	}
	// Log.
	this.messageHelper.nonAuditableKeyNoAssoc(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_AUX_PROJECT, obj.getKey());

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
