package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.MaterialValueRepo;
import com.cebedo.pmsys.repository.PullOutValueRepo;
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

	// If we're creating.
	// Assign a new uuid.
	Material material = obj.getMaterial();

	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());

	    // Assign the staff.
	    Staff staff = this.staffDAO.getByID(obj.getStaffID());
	    obj.setStaff(staff);

	    // Get the pulled-out quantity.
	    // Update the material's used and available.
	    double pulledOut = obj.getQuantity();
	    material.setUsed(material.getUsed() + pulledOut);
	    material.setAvailable(material.getAvailable() - pulledOut);

	    // Update the material object.
	    // Set the pull-out.
	    obj.setMaterial(material);
	    this.pullOutValueRepo.set(obj);
	    this.materialValueRepo.set(material);

	    // Return.
	    return AlertBoxGenerator.SUCCESS.generatePullout(obj.getQuantity(),
		    material.getUnit(), material.getName());
	}
	return AlertBoxGenerator.FAILED.generatePullout(obj.getQuantity(),
		material.getUnit(), material.getName());
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
    public void delete(String key) {
	this.pullOutValueRepo.delete(key);
    }

}
