package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.MaterialValueRepo;
import com.cebedo.pmsys.repository.PullOutValueRepo;
import com.cebedo.pmsys.service.PullOutService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class PullOutServiceImpl implements PullOutService {

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

    @Override
    @Transactional
    public void rename(PullOut obj, String newKey) {
	this.pullOutValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, PullOut> m) {
	this.pullOutValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public String create(PullOut obj) {

	Material material = obj.getMaterial();

	// You are not allowed to pull-out
	// if quantity is greater than what is available.
	double quantity = obj.getQuantity();
	double available = material.getAvailable();

	// You're also not allowed if you already have a uuid.
	if (quantity <= 0 || obj.getDatetime() == null || available <= 0 || quantity > available) {
	    return AlertBoxGenerator.FAILED.generatePullout(obj.getQuantity(), "TODO",
		    material.getName());
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

	// Return.
	return AlertBoxGenerator.SUCCESS.generatePullout(obj.getQuantity(), "TODO", material.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.pullOutValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(PullOut obj) {
	this.pullOutValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public PullOut get(String key) {
	return this.pullOutValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.pullOutValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<PullOut> multiGet(Collection<String> keys) {
	return this.pullOutValueRepo.multiGet(keys);
    }

    @Transactional
    @Override
    public String delete(String key) {

	PullOut obj = this.pullOutValueRepo.get(key);

	// Do delete.
	doDelete(key, obj);

	// Return.
	return AlertBoxGenerator.SUCCESS.generatePulloutDelete(obj.getDatetime(), obj.getStaff()
		.getFullName());
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
	String pattern = PullOut.constructPattern(proj);
	Set<String> keys = this.pullOutValueRepo.keys(pattern);
	return this.pullOutValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String update(PullOut newPullout) {
	PullOut oldPullOut = this.pullOutValueRepo.get(newPullout.getKey());

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
	    return AlertBoxGenerator.SUCCESS.generateUpdate(RedisConstants.OBJECT_PULL_OUT, newPullout
		    .getMaterial().getName());
	}

	// Do service.
	this.pullOutValueRepo.set(newPullout);

	// Return.
	return AlertBoxGenerator.SUCCESS.generateUpdate(RedisConstants.OBJECT_PULL_OUT, newPullout
		.getMaterial().getName());
    }

}
