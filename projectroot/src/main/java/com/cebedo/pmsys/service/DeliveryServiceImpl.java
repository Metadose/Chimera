package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.DeliveryValueRepo;
import com.cebedo.pmsys.repository.MaterialValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.repository.PullOutValueRepo;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private AuthHelper authHelper = new AuthHelper();
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
    public void rename(Delivery obj, String newKey) {
	this.deliveryValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Delivery> m) {
	this.deliveryValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public String set(Delivery obj) {

	// If company is null.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
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
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_DELIVERY, obj.getName());
	}
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_DELIVERY, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.deliveryValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Delivery obj) {
	this.deliveryValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Delivery get(String key) {
	return this.deliveryValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.deliveryValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Delivery> multiGet(Collection<String> keys) {
	return this.deliveryValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public List<Delivery> list(Project proj) {
	String pattern = Delivery.constructPattern(proj);
	Set<String> keys = this.deliveryValueRepo.keys(pattern);
	return this.deliveryValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {

	// Get the necessary objects.
	Delivery delivery = this.deliveryValueRepo.get(key);
	Project proj = delivery.getProject();
	ProjectAux projAux = this.projectAuxValueRepo.get(ProjectAux
		.constructKey(proj));

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
	Set<String> materialKeys = this.materialValueRepo
		.keys(materialsPattern);
	Set<String> pulloutKeys = this.pullOutValueRepo.keys(pulloutPattern);
	this.materialValueRepo.delete(materialKeys);
	this.pullOutValueRepo.delete(pulloutKeys);

	// Delete this object.
	this.deliveryValueRepo.delete(key);

	return AlertBoxGenerator.SUCCESS.generateDelete(
		RedisConstants.OBJECT_DELIVERY, delivery.getName());
    }

}
