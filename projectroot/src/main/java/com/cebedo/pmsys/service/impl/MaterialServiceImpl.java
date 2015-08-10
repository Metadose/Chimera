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
import com.cebedo.pmsys.repository.PullOutValueRepo;
import com.cebedo.pmsys.service.MaterialService;
import com.cebedo.pmsys.service.ProjectAuxService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class MaterialServiceImpl implements MaterialService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private MaterialValueRepo materialValueRepo;
    private DeliveryValueRepo deliveryValueRepo;
    private ProjectAuxService projectAuxService;
    private PullOutValueRepo pullOutValueRepo;

    public void setPullOutValueRepo(PullOutValueRepo pullOutValueRepo) {
	this.pullOutValueRepo = pullOutValueRepo;
    }

    public void setProjectAuxService(ProjectAuxService projectAuxService) {
	this.projectAuxService = projectAuxService;
    }

    public void setDeliveryValueRepo(DeliveryValueRepo deliveryValueRepo) {
	this.deliveryValueRepo = deliveryValueRepo;
    }

    public void setMaterialValueRepo(MaterialValueRepo materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    @Override
    @Transactional
    public String create(Material obj) {

	// If company is null.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	else if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_MATERIAL, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// If we're creating.
	if (obj.getUuid() == null) {

	    // Set the UUID.
	    obj.setUuid(UUID.randomUUID());

	    // Set available = quantity.
	    obj.setAvailable(obj.getQuantity());

	    // Set the total cost.
	    // total = quantity * cost per unit.
	    double totalCost = obj.getQuantity() * obj.getCostPerUnitMaterial();
	    obj.setTotalCostPerUnitMaterial(totalCost);

	    // Do actual service.
	    this.materialValueRepo.set(obj);

	    // Add the grand total to the delivery.
	    // Update the delivery object.
	    Delivery delivery = obj.getDelivery();
	    double newGrandTotal = delivery.getGrandTotalOfMaterials() + totalCost;
	    delivery.setGrandTotalOfMaterials(newGrandTotal);
	    this.deliveryValueRepo.set(delivery);

	    // Update the project auxillary.
	    // Add material total to project grand total.
	    ProjectAux projectAux = this.projectAuxService.get(delivery);
	    double projectTotalDelivery = projectAux.getGrandTotalDelivery() + totalCost;
	    projectAux.setGrandTotalDelivery(projectTotalDelivery);
	    this.projectAuxService.set(projectAux);

	    // Log.
	    this.messageHelper.send(AuditAction.ACTION_CREATE, RedisConstants.OBJECT_MATERIAL, obj.getKey());

	    // Return.
	    return AlertBoxGenerator.SUCCESS.generateAdd(RedisConstants.OBJECT_MATERIAL, obj.getName());
	}

	// This service used only for adding.
	// Not updating.
	return AlertBoxGenerator.ERROR;
    }

    @Override
    @Transactional
    public Material get(String key) {

	Material obj = this.materialValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_MATERIAL, obj.getKey());
	    return new Material();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, RedisConstants.OBJECT_MATERIAL, obj.getKey());

	return obj;
    }

    @Override
    @Transactional
    public List<Material> list(Delivery delivery) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(delivery)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_DELIVERY, delivery.getKey());
	    return new ArrayList<Material>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, RedisConstants.OBJECT_DELIVERY, delivery.getKey(),
		RedisConstants.OBJECT_MATERIAL);

	String pattern = Material.constructPattern(delivery);
	Set<String> keys = this.materialValueRepo.keys(pattern);
	return this.materialValueRepo.multiGet(keys);
    }

    /**
     * Delete a material.
     */
    @Transactional
    @Override
    public String delete(String key) {

	// Get the material.
	Material material = this.materialValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(material)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_MATERIAL, material.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, RedisConstants.OBJECT_MATERIAL, material.getKey());

	// Get the updated version of the objects.
	Delivery delivery = this.deliveryValueRepo.get(material.getDelivery().getKey());
	ProjectAux projectAux = this.projectAuxService.get(material.getProject());

	// If the object will be deleted,
	// remove this material's total from delivery,
	// remove this material's total from project aux.
	double materialTotal = material.getTotalCostPerUnitMaterial();
	double deliveryTotal = delivery.getGrandTotalOfMaterials();
	double grandTotal = projectAux.getGrandTotalDelivery();

	// Set the values,
	// then set it to repo.
	delivery.setGrandTotalOfMaterials(deliveryTotal - materialTotal);
	projectAux.setGrandTotalDelivery(grandTotal - materialTotal);
	this.deliveryValueRepo.set(delivery);
	this.projectAuxService.set(projectAux);

	// Do the delete.
	this.materialValueRepo.delete(key);

	// Delete also all related pull-outs.
	String pattern = PullOut.constructPattern(material);
	Set<String> keys = this.pullOutValueRepo.keys(pattern);
	this.pullOutValueRepo.delete(keys);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateDelete(RedisConstants.OBJECT_MATERIAL,
		material.getName());
    }

    /**
     * List all materials in project.
     */
    @Transactional
    @Override
    public List<Material> list(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Material>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		RedisConstants.OBJECT_MATERIAL);

	String pattern = Material.constructPattern(proj);
	Set<String> keys = this.materialValueRepo.keys(pattern);
	return this.materialValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String update(Material material) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(material)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_MATERIAL, material.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, RedisConstants.OBJECT_MATERIAL, material.getKey());

	// Set the material.
	this.materialValueRepo.set(material);
	return AlertBoxGenerator.SUCCESS.generateUpdate(RedisConstants.OBJECT_MATERIAL,
		material.getName());
    }

}
