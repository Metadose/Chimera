package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.MaterialValueRepo;
import com.cebedo.pmsys.repository.PullOutValueRepo;
import com.cebedo.pmsys.service.PullOutService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class PullOutServiceImpl implements PullOutService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private PullOutValueRepo pullOutValueRepo;
    private StaffDAO staffDAO;
    private MaterialValueRepo materialValueRepo;

    public void setMaterialValueRepo(MaterialValueRepo materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setPullOutValueRepo(PullOutValueRepo pullOutValueRepo) {
	this.pullOutValueRepo = pullOutValueRepo;
    }

    /**
     * Create a pull-out.
     */
    @Override
    @Transactional
    public String create(PullOut obj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	Material material = obj.getMaterial();

	// You are not allowed to pull-out
	// if quantity is greater than what is available.
	double quantity = obj.getQuantity();
	double available = material.getAvailable();

	// Error: Pullout more than the available.
	if (quantity > available) {
	    return AlertBoxGenerator.FAILED.generateHTML(RegistryResponseMessage.ERROR_PROJECT_PULLOUT_EXCEED);
	}
	// Error: Invalid quantity value.
	else if (quantity <= 0) {
	    return AlertBoxGenerator.FAILED.generateHTML(RegistryResponseMessage.ERROR_COMMON_INVALID_QUANTITY);
	}
	// Error: Invalid date time.
	else if (obj.getDatetime() == null) {
	    return AlertBoxGenerator.FAILED.generateHTML(RegistryResponseMessage.ERROR_COMMON_INVALID_DATE_TIME);
	}
	// Error: Pull out date is before the delivery date.
	else if (obj.getDatetime().before(obj.getDelivery().getDatetime())) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_PULLOUT_DATE_BEFORE_DELIVERY);
	}
	// Error: Etc.
	else if (available <= 0) {
	    return AlertBoxGenerator.ERROR;
	}

	// If we're creating.
	// Assign a new uuid.
	// Note: UUID is already set if we're coming from function
	// update(PullOut newPullout).
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	}

	// Assign the staff.
	Staff staff = this.staffDAO.getByID(obj.getStaffID());
	obj.setStaff(staff);

	// Get the pulled-out quantity.
	// Update the material's used and available.
	double pulledOut = obj.getQuantity();
	material.setUsed(material.getUsed() + pulledOut);
	material.setAvailable(available - pulledOut);

	// Update the material object.
	// Set the pull-out.
	obj.setMaterial(material);
	this.pullOutValueRepo.set(obj);
	this.materialValueRepo.set(material);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE, ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());

	// Return.
	return AlertBoxGenerator.SUCCESS.generatePullout(obj.getQuantity(), material.getUnitSymbol(),
		material.getName());
    }

    /**
     * Get a pull-out.
     */
    @Override
    @Transactional
    public PullOut get(String key) {

	PullOut obj = this.pullOutValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());
	    return new PullOut();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());

	return obj;
    }

    @Transactional
    @Override
    public String delete(String key) {

	PullOut obj = this.pullOutValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());

	// Do delete.
	doDelete(key, obj);

	// Return.
	return AlertBoxGenerator.SUCCESS.generatePulloutDelete(obj.getMaterial().getName(),
		obj.getDatetime(), obj.getStaff().getFullName());
    }

    /**
     * Do the delete logic.
     * 
     * @param key
     * @param obj
     */
    private void doDelete(String key, PullOut obj) {
	// Get the object.
	// And the material.
	// The material's used and available will be updated.
	// A revert of "how it was" before the pull-out was created.
	Material material = obj.getMaterial();

	// Get the pulled-out quantity.
	// Update the material's used and available.
	double pulledOut = obj.getQuantity();
	material.setUsed(material.getUsed() - pulledOut);
	material.setAvailable(material.getAvailable() + pulledOut);
	this.materialValueRepo.set(material);

	// Update the material object.
	// Set the pull-out.
	this.pullOutValueRepo.delete(key);
    }

    @Override
    @Transactional
    public List<PullOut> list(Project proj) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<PullOut>();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_PULL_OUT);

	String pattern = PullOut.constructPattern(proj);
	Set<String> keys = this.pullOutValueRepo.keys(pattern);
	return this.pullOutValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String update(PullOut newPullout) {

	double quantity = newPullout.getQuantity();
	double available = newPullout.getMaterial().getAvailable();

	// Error: Pullout more than the available.
	if (quantity > available) {
	    return AlertBoxGenerator.FAILED.generateHTML(RegistryResponseMessage.ERROR_PROJECT_PULLOUT_EXCEED);
	}
	// Error: Invalid quantity value.
	else if (quantity <= 0) {
	    return AlertBoxGenerator.FAILED.generateHTML(RegistryResponseMessage.ERROR_COMMON_INVALID_QUANTITY);
	}
	// Error: Invalid date time.
	else if (newPullout.getDatetime() == null) {
	    return AlertBoxGenerator.FAILED.generateHTML(RegistryResponseMessage.ERROR_COMMON_INVALID_DATE_TIME);
	}
	// Error: Etc.
	else if (available <= 0) {
	    return AlertBoxGenerator.ERROR;
	}

	PullOut oldPullOut = this.pullOutValueRepo.get(newPullout.getKey());

	// Security check.
	if (!this.authHelper.isActionAuthorized(oldPullOut)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_PULL_OUT, oldPullOut.getKey());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, ConstantsRedis.OBJECT_PULL_OUT,
		oldPullOut.getKey());

	// If the quantity has been changed.
	// Just delete then commit new one.
	if (oldPullOut.getQuantity() != newPullout.getQuantity()) {

	    // Delete.
	    doDelete(oldPullOut.getKey(), oldPullOut);

	    // Get the updated Material,
	    // "After the deletion of the oldPullOut".
	    Material updatedMaterial = this.materialValueRepo.get(newPullout.getMaterial().getKey());
	    newPullout.setMaterial(updatedMaterial);

	    // Create new entry.
	    create(newPullout);

	    // Return.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_PULL_OUT, newPullout
		    .getMaterial().getName());
	}

	// Do service.
	this.pullOutValueRepo.set(newPullout);

	// Return.
	return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_PULL_OUT, newPullout
		.getMaterial().getName());
    }

}
