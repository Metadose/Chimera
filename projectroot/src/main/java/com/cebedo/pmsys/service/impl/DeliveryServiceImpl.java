package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.DeliveryValueRepo;
import com.cebedo.pmsys.repository.MaterialValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.repository.PullOutValueRepo;
import com.cebedo.pmsys.service.DeliveryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.validator.DeliveryValidator;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

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

    @Autowired
    DeliveryValidator deliveryValidator;

    @Override
    @Transactional
    public String set(Delivery obj, BindingResult result) {

	// If company is null.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	else if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_DELIVERY, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	this.deliveryValidator.validate(obj, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
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

	if (isCreate) {
	    this.messageHelper.send(AuditAction.ACTION_CREATE, ConstantsRedis.OBJECT_DELIVERY,
		    obj.getKey());
	    return AlertBoxGenerator.SUCCESS.generateCreate(ConstantsRedis.OBJECT_DELIVERY,
		    obj.getName());
	}
	this.messageHelper.send(AuditAction.ACTION_UPDATE, ConstantsRedis.OBJECT_DELIVERY, obj.getKey());
	return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_DELIVERY, obj.getName());
    }

    @Override
    @Transactional
    public Delivery get(String key) {
	Delivery obj = this.deliveryValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_DELIVERY, obj.getKey());
	    return new Delivery();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_DELIVERY, obj.getKey());

	return obj;
    }

    @Override
    @Transactional
    public List<Delivery> listDesc(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Delivery>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_DELIVERY);

	String pattern = Delivery.constructPattern(proj);
	Set<String> keys = this.deliveryValueRepo.keys(pattern);

	List<Delivery> deliveries = this.deliveryValueRepo.multiGet(keys);

	// Sort the list in descending order.
	Collections.sort(deliveries, new Comparator<Delivery>() {
	    @Override
	    public int compare(Delivery aObj, Delivery bObj) {
		Date aStart = aObj.getDatetime();
		Date bStart = bObj.getDatetime();

		// To sort in ascending,
		// remove Not's.
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});
	return deliveries;
    }

    @Override
    @Transactional
    public String delete(String key) {

	Delivery delivery = this.deliveryValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(delivery)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_DELIVERY, delivery.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, ConstantsRedis.OBJECT_DELIVERY,
		delivery.getKey());

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

	return AlertBoxGenerator.SUCCESS.generateDelete(ConstantsRedis.OBJECT_DELIVERY,
		delivery.getName());
    }

    @Transactional
    @Override
    public List<Delivery> listAsc(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Delivery>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_DELIVERY);

	String pattern = Delivery.constructPattern(proj);
	Set<String> keys = this.deliveryValueRepo.keys(pattern);

	List<Delivery> deliveries = this.deliveryValueRepo.multiGet(keys);

	// Sort the list in descending order.
	Collections.sort(deliveries, new Comparator<Delivery>() {
	    @Override
	    public int compare(Delivery aObj, Delivery bObj) {
		Date aStart = aObj.getDatetime();
		Date bStart = bObj.getDatetime();

		// To sort in ascending,
		// remove Not's.
		return aStart.before(bStart) ? -1 : aStart.after(bStart) ? 1 : 0;
	    }
	});
	return deliveries;
    }

}
