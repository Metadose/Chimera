package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.DeliveryValueRepo;
import com.cebedo.pmsys.repository.MaterialValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.repository.PullOutValueRepo;
import com.cebedo.pmsys.service.DeliveryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private DeliveryValueRepo deliveryValueRepo;
    private ProjectAuxValueRepo projectAuxValueRepo;
    private MaterialValueRepo materialValueRepo;
    private PullOutValueRepo pullOutValueRepo;

    public void setMaterialValueRepo(MaterialValueRepo materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    public void setPullOutValueRepo(PullOutValueRepo pullOutValueRepo) {
	this.pullOutValueRepo = pullOutValueRepo;
    }

    public void setProjectAuxValueRepo(ProjectAuxValueRepo projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    public void setDeliveryValueRepo(DeliveryValueRepo deliveryValueRepo) {
	this.deliveryValueRepo = deliveryValueRepo;
    }

    @Override
    @Transactional
    public String set(Delivery obj) {

	// If company is null.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	else if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_DELIVERY, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// If we're creating.
	boolean isCreate = false;
	if (obj.getUuid() == null) {
	    isCreate = true;
	    obj.setUuid(UUID.randomUUID());
	}

	// Do the action.
	// Return success.
	this.deliveryValueRepo.set(obj);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_SET, RedisConstants.OBJECT_DELIVERY, obj.getKey());

	if (isCreate) {
	    return AlertBoxGenerator.SUCCESS.generateCreate(RedisConstants.OBJECT_DELIVERY,
		    obj.getName());
	}
	return AlertBoxGenerator.SUCCESS.generateUpdate(RedisConstants.OBJECT_DELIVERY, obj.getName());
    }

    @Override
    @Transactional
    public Delivery get(String key) {
	Delivery obj = get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_DELIVERY, obj.getKey());
	    return new Delivery();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, RedisConstants.OBJECT_DELIVERY, obj.getKey());

	return obj;
    }

    @Override
    @Transactional
    public List<Delivery> list(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Delivery>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		RedisConstants.OBJECT_DELIVERY);

	String pattern = Delivery.constructPattern(proj);
	Set<String> keys = this.deliveryValueRepo.keys(pattern);
	return this.deliveryValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {

	Delivery delivery = this.deliveryValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(delivery)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_DELIVERY, delivery.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, RedisConstants.OBJECT_DELIVERY, delivery.getKey());

	// Get the necessary objects.
	Project proj = delivery.getProject();
	ProjectAux projAux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));

	// Solve for the old grand total.
	double deliveryTotal = delivery.getGrandTotalOfMaterials();
	double grandTotal = projAux.getGrandTotalDelivery();
	double newTotal = grandTotal - deliveryTotal;

	// Revert the grand total.
	projAux.setGrandTotalDelivery(newTotal);
	this.projectAuxValueRepo.set(projAux);

	// If we're deleting this delivery,
	// delete also all materials inside,
	// and all pullouts involved.
	String materialsPattern = Material.constructPattern(delivery);
	String pulloutPattern = PullOut.constructPattern(delivery);

	// Get all keys.
	// Delete all keys.
	Set<String> materialKeys = this.materialValueRepo.keys(materialsPattern);
	Set<String> pulloutKeys = this.pullOutValueRepo.keys(pulloutPattern);
	this.materialValueRepo.delete(materialKeys);
	this.pullOutValueRepo.delete(pulloutKeys);

	// Delete this object.
	this.deliveryValueRepo.delete(key);

	return AlertBoxGenerator.SUCCESS.generateDelete(RedisConstants.OBJECT_DELIVERY,
		delivery.getName());
    }

}
