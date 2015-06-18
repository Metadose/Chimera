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
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.repository.DeliveryValueRepo;
import com.cebedo.pmsys.repository.MaterialValueRepo;
import com.cebedo.pmsys.ui.AlertBoxFactory;

@Service
public class MaterialServiceImpl implements MaterialService {

    private AuthHelper authHelper = new AuthHelper();
    private MaterialValueRepo materialValueRepo;
    private DeliveryValueRepo deliveryValueRepo;

    public void setDeliveryValueRepo(DeliveryValueRepo deliveryValueRepo) {
	this.deliveryValueRepo = deliveryValueRepo;
    }

    public void setMaterialValueRepo(MaterialValueRepo materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    @Override
    @Transactional
    public void rename(Material obj, String newKey) {
	this.materialValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Material> m) {
	this.materialValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public String set(Material obj) {

	// If company is null.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	// If we're creating.
	boolean isCreate = false;
	if (obj.getUuid() == null) {
	    isCreate = true;
	    obj.setUuid(UUID.randomUUID());

	    // Set available = quantity.
	    obj.setAvailable(obj.getQuantity());

	    // Set the total cost.
	    // total = quantity * cost per unit.
	    obj.setTotalCostPerUnitMaterial(obj.getQuantity()
		    * obj.getCostPerUnitMaterial());
	}

	// Do actual service.
	this.materialValueRepo.set(obj);

	// Add the grand total to the delivery.
	// Update the delivery object.
	Delivery delivery = obj.getDelivery();
	double newGrandTotal = delivery.getGrandTotalOfMaterials()
		+ obj.getTotalCostPerUnitMaterial();
	delivery.setGrandTotalOfMaterials(newGrandTotal);
	this.deliveryValueRepo.set(delivery);

	// Return.
	if (isCreate) {
	    return AlertBoxFactory.SUCCESS.generateAdd(
		    RedisConstants.OBJECT_MATERIAL, obj.getName());
	}
	return AlertBoxFactory.SUCCESS.generateAdd(
		RedisConstants.OBJECT_MATERIAL, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.materialValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Material obj) {
	this.materialValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Material get(String key) {
	return this.materialValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.materialValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Material> multiGet(Collection<String> keys) {
	return this.materialValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public List<Material> list(Delivery delivery) {
	String pattern = Material.constructPattern(delivery);
	Set<String> keys = this.materialValueRepo.keys(pattern);
	return this.materialValueRepo.multiGet(keys);
    }

    @Transactional
    @Override
    public void delete(String key) {
	this.materialValueRepo.delete(key);
    }

}
