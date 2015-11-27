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
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.impl.MaterialValueRepoImpl;
import com.cebedo.pmsys.repository.impl.PullOutValueRepoImpl;
import com.cebedo.pmsys.service.PullOutService;
import com.cebedo.pmsys.validator.PullOutValidator;

@Service
public class PullOutServiceImpl implements PullOutService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private PullOutValueRepoImpl pullOutValueRepo;
    private StaffDAO staffDAO;
    private MaterialValueRepoImpl materialValueRepo;

    public void setMaterialValueRepo(MaterialValueRepoImpl materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setPullOutValueRepo(PullOutValueRepoImpl pullOutValueRepo) {
	this.pullOutValueRepo = pullOutValueRepo;
    }

    @Autowired
    PullOutValidator pullOutValidator;

    /**
     * Create a pull-out.
     */
    @Override
    @Transactional
    public String create(PullOut obj, BindingResult result) {

	// Security check.
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.pullOutValidator.validate(obj, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
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
	Material material = obj.getMaterial();
	double available = material.getAvailable();
	double pulledOut = obj.getQuantity();
	material.setUsed(material.getUsed() + pulledOut);
	material.setAvailable(available - pulledOut);

	// Update the material object.
	// Set the pull-out.
	obj.setMaterial(material);
	this.pullOutValueRepo.set(obj);
	this.materialValueRepo.set(material);

	// Log.
	Project proj = obj.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_CREATE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_PULL_OUT, obj.getKey(), proj, material.getName());

	// Return.
	return AlertBoxFactory.SUCCESS.generatePullout(obj.getQuantity(), material.getUnitSymbol(),
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
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());
	    return new PullOut();
	}
	// Log.
	this.messageHelper.nonAuditableKeyNoAssoc(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_PULL_OUT,
		obj.getKey());

	return obj;
    }

    @Transactional
    @Override
    public String delete(String key) {

	PullOut obj = this.pullOutValueRepo.get(key);

	// Security check.
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_PULL_OUT, obj.getKey());
	    return AlertBoxFactory.ERROR;
	}
	// Log.
	Project proj = obj.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_PULL_OUT, obj.getKey(), proj, obj.getMaterial().getName());

	// Do delete.
	doDelete(key, obj);

	// Return.
	return AlertBoxFactory.SUCCESS.generatePulloutDelete(obj.getMaterial().getName(),
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

    @Transactional
    @Override
    public List<PullOut> listDesc(Project proj) {
	return listDesc(proj, false);
    }

    @Override
    @Transactional
    public List<PullOut> listDesc(Project proj, boolean override) {
	// Security check.
	if (!override && !this.authHelper.hasAccess(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<PullOut>();
	}
	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_PULL_OUT);

	String pattern = PullOut.constructPattern(proj);
	Set<String> keys = this.pullOutValueRepo.keys(pattern);
	List<PullOut> pullOuts = this.pullOutValueRepo.multiGet(keys);
	Collections.sort(pullOuts, new Comparator<PullOut>() {
	    @Override
	    public int compare(PullOut aObj, PullOut bObj) {
		Date aStart = aObj.getDatetime();
		Date bStart = bObj.getDatetime();

		// To sort in descending.
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});
	return pullOuts;
    }

}
