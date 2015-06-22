package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.MaterialCategory;
import com.cebedo.pmsys.domain.Unit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.MaterialCategoryValueRepo;
import com.cebedo.pmsys.repository.UnitValueRepo;
import com.cebedo.pmsys.service.MaterialCategoryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class MaterialCategoryServiceImpl implements MaterialCategoryService {

    private AuthHelper authHelper = new AuthHelper();
    private MaterialCategoryValueRepo materialCategoryValueRepo;
    private UnitValueRepo unitValueRepo;

    public void setUnitValueRepo(UnitValueRepo unitValueRepo) {
	this.unitValueRepo = unitValueRepo;
    }

    public void setMaterialCategoryValueRepo(
	    MaterialCategoryValueRepo materialCategoryValueRepo) {
	this.materialCategoryValueRepo = materialCategoryValueRepo;
    }

    @Override
    @Transactional
    public void rename(MaterialCategory obj, String newKey) {
	this.materialCategoryValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, MaterialCategory> m) {
	this.materialCategoryValueRepo.multiSet(m);
    }

    /**
     * Set the materialCategory.
     */
    @Override
    @Transactional
    public String set(MaterialCategory obj) {

	// Set the unit.
	Unit unit = this.unitValueRepo.get(obj.getUnitKey());
	obj.setUnit(unit);

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.materialCategoryValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_MATERIAL_CATEGORY_DISPLAY,
		    obj.getName());
	}

	// If update.
	this.materialCategoryValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_MATERIAL_CATEGORY_DISPLAY, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.materialCategoryValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(MaterialCategory obj) {
	this.materialCategoryValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public MaterialCategory get(String key) {
	return this.materialCategoryValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.materialCategoryValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<MaterialCategory> multiGet(Collection<String> keys) {
	return this.materialCategoryValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.materialCategoryValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<MaterialCategory> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = MaterialCategory.constructPattern(company);
	Set<String> keys = this.materialCategoryValueRepo.keys(pattern);
	return this.materialCategoryValueRepo.multiGet(keys);
    }

}
