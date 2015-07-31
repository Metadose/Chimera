package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Shape;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.repository.ShapeValueRepo;
import com.cebedo.pmsys.service.ShapeService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.StringUtils;

@Service
public class ShapeServiceImpl implements ShapeService {

    private AuthHelper authHelper = new AuthHelper();
    private ShapeValueRepo shapeValueRepo;

    public void setShapeValueRepo(ShapeValueRepo repo) {
	this.shapeValueRepo = repo;
    }

    @Override
    @Transactional
    public void rename(Shape obj, String newKey) {
	this.shapeValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Shape> m) {
	this.shapeValueRepo.multiSet(m);
    }

    /**
     * Set the formula object.
     */
    @Override
    @Transactional
    public String set(Shape obj) {

	// If the object is not valid.
	if (!obj.isValid()) {

	    // If we're creating.
	    if (obj.getUuid() == null) {
		return AlertBoxGenerator.FAILED.generateCreate(RedisConstants.OBJECT_SHAPE,
			obj.getName());
	    }
	    // Else, we're updating.
	    return AlertBoxGenerator.FAILED.generateUpdate(RedisConstants.OBJECT_SHAPE, obj.getName());
	}

	// If company is null,
	// set it based on auth.
	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	// Set the variable names in this formula.
	obj.setAreaVariableNames(getAllVariableNames(obj.getAreaFormula()));
	obj.setVolumeVariableNames(getAllVariableNames(obj.getVolumeFormula()));

	// If we're creating.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.shapeValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(RedisConstants.OBJECT_SHAPE, obj.getName());
	}
	this.shapeValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(RedisConstants.OBJECT_SHAPE, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.shapeValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Shape obj) {
	this.shapeValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Shape get(String key) {
	return this.shapeValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.shapeValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Shape> multiGet(Collection<String> keys) {
	return this.shapeValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public List<Shape> list() {
	AuthenticationToken auth = this.authHelper.getAuth();
	String pattern = Shape.constructPattern(auth.getCompany());
	Set<String> keys = this.shapeValueRepo.keys(pattern);
	List<Shape> fList = this.shapeValueRepo.multiGet(keys);
	return fList;
    }

    /**
     * Get all variable names of this formula.
     * 
     * @return
     */
    @Transactional
    @Override
    public List<String> getAllVariableNames(String formula) {

	// Get all indices of open and close variables.
	List<Integer> openIndices = StringUtils.getAllIndicesOfSubstring(formula,
		Shape.DELIMITER_OPEN_VARIABLE);
	List<Integer> closeIndices = StringUtils.getAllIndicesOfSubstring(formula,
		Shape.DELIMITER_CLOSE_VARIABLE);

	// Proceed only if legal.
	if (openIndices.size() == closeIndices.size()) {

	    List<String> variableNames = new ArrayList<String>();

	    for (int i = 0; i < openIndices.size(); i++) {

		// Get the variable name.
		int indexOpen = openIndices.get(i);
		int indexClose = closeIndices.get(i);
		String variableName = formula.substring(indexOpen, indexClose);

		// Clean the variable name.
		variableName = org.apache.commons.lang.StringUtils.remove(variableName,
			Shape.DELIMITER_OPEN_VARIABLE);
		variableName = org.apache.commons.lang.StringUtils.remove(variableName,
			Shape.DELIMITER_CLOSE_VARIABLE);

		// Add the name to the output list.
		variableNames.add(variableName);
	    }
	    return variableNames;
	}
	return new ArrayList<String>();
    }

    @Transactional
    @Override
    public void delete(String key) {
	this.shapeValueRepo.delete(key);
    }
}
