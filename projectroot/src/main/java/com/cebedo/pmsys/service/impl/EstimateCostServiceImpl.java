package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.ExcelHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.repository.EstimateCostValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.EstimateCostService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class EstimateCostServiceImpl implements EstimateCostService {

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private ExcelHelper excelHelper = new ExcelHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private static final int EXCEL_COLUMN_NAME = 1;
    private static final int EXCEL_COLUMN_COST_ESTIMATED = 2;
    private static final int EXCEL_COLUMN_COST_ACTUAL = 3;
    private static final int EXCEL_COLUMN_COST_TYPE = 4;

    private EstimateCostValueRepo estimateCostValueRepo;
    private ProjectAuxValueRepo projectAuxValueRepo;

    @Autowired
    @Qualifier(value = "projectAuxValueRepo")
    public void setProjectAuxValueRepo(ProjectAuxValueRepo projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    @Autowired
    @Qualifier(value = "estimateCostValueRepo")
    public void setEstimateCostValueRepo(EstimateCostValueRepo estimateCostValueRepo) {
	this.estimateCostValueRepo = estimateCostValueRepo;
    }

    @Override
    @Transactional
    public String createMassCosts(List<EstimateCost> costs, BindingResult result) {

	// Security check.
	if (costs.size() > 0 && !this.authHelper.isActionAuthorized(costs.get(0))) {
	    long projectID = costs.get(0).getProject().getId();
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, projectID);
	    return AlertBoxGenerator.ERROR;
	}

	// TODO Optimize. Maybe throw exception to fail?
	// for (EstimateCost cost : costs) {
	// // Service layer form validation.
	// this.taskValidator.validate(task, result);
	// if (result.hasErrors()) {
	// return this.validationHelper.errorMessageHTML(result);
	// }
	// }

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE_MASS, Task.OBJECT_NAME);

	// If reaches this point, do actual service.
	for (EstimateCost cost : costs) {
	    set(cost);
	}
	return null;
    }

    @Override
    @Transactional
    public List<EstimateCost> convertExcelToCostList(MultipartFile multipartFile, Project project) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return null;
	}

	// Service layer form validation.
	boolean valid = this.validationHelper.fileIsNotNullOrEmpty(multipartFile);
	if (!valid) {
	    return null;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CONVERT_FILE, Project.OBJECT_NAME, project.getId(),
		MultipartFile.class.getName());

	try {

	    // Create Workbook instance holding reference to .xls file
	    // Get first/desired sheet from the workbook.
	    HSSFWorkbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Iterate through each rows one by one.
	    Iterator<Row> rowIterator = sheet.iterator();

	    // Construct estimate containers.
	    List<EstimateCost> costList = new ArrayList<EstimateCost>();
	    while (rowIterator.hasNext()) {

		Row row = rowIterator.next();
		int rowCountDisplay = row.getRowNum() + 1;

		// Skip first line.
		if (rowCountDisplay <= 1) {
		    continue;
		}

		// For each row, iterate through all the columns
		Iterator<Cell> cellIterator = row.cellIterator();

		// Every row, is a Staff object.
		EstimateCost cost = new EstimateCost(project);

		while (cellIterator.hasNext()) {

		    // Cell in this row and column.
		    Cell cell = cellIterator.next();
		    int colCountDisplay = cell.getColumnIndex() + 1;

		    switch (colCountDisplay) {

		    case EXCEL_COLUMN_NAME:
			String name = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			cost.setName(name);
			continue;

		    case EXCEL_COLUMN_COST_ESTIMATED:
			Double costVal = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			cost.setCost(costVal);
			continue;

		    case EXCEL_COLUMN_COST_ACTUAL:
			costVal = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			cost.setActualCost(costVal);
			continue;

		    case EXCEL_COLUMN_COST_TYPE:
			String costType = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			cost.setCostType(EstimateCostType.of(costType));
			continue;

		    }
		}
		costList.add(cost);
	    }
	    return costList;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Transactional
    @Override
    public String delete(String key) {
	EstimateCost obj = this.estimateCostValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getKey());

	// Project auxiliary on grand totals of costs.
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(obj.getProject()));
	EstimateCostType costType = obj.getCostType();
	double actualCost = obj.getActualCost();
	double estimatedCost = obj.getCost();
	// Direct cost.
	if (costType == EstimateCostType.DIRECT) {
	    aux.setGrandTotalCostsDirect(aux.getGrandTotalCostsDirect() - estimatedCost);
	    aux.setGrandTotalActualCostsDirect(aux.getGrandTotalActualCostsDirect() - actualCost);
	}
	// If cost is indirect.
	else if (costType == EstimateCostType.INDIRECT) {
	    aux.setGrandTotalCostsIndirect(aux.getGrandTotalCostsIndirect() - estimatedCost);
	    aux.setGrandTotalActualCostsIndirect(aux.getGrandTotalActualCostsIndirect() - actualCost);
	}

	this.estimateCostValueRepo.delete(key);
	this.projectAuxValueRepo.set(aux);
	return AlertBoxGenerator.SUCCESS.generateDelete(ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getName());
    }

    @Transactional
    @Override
    public EstimateCost get(String key) {
	EstimateCost obj = this.estimateCostValueRepo.get(key);
	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	    return new EstimateCost();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getKey());
	return obj;
    }

    @Transactional
    @Override
    public List<EstimateCost> list(Project proj) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<EstimateCost>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_ESTIMATE_COST);

	String pattern = EstimateCost.constructPattern(proj);
	Set<String> keys = this.estimateCostValueRepo.keys(pattern);

	List<EstimateCost> costs = this.estimateCostValueRepo.multiGet(keys);
	return costs;
    }

    @Transactional
    @Override
    public String set(EstimateCost obj) {
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// If we're updating, revert old values first.
	boolean isCreate = true;
	if (obj.getUuid() != null) {
	    revertOldValues(obj);
	    isCreate = false;
	}
	// If we're creating.
	else {
	    obj.setUuid(UUID.randomUUID());
	}

	// Project auxiliary on grand totals of costs.
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(obj.getProject()));
	EstimateCostType costType = obj.getCostType();
	double actualCost = obj.getActualCost();
	double estimatedCost = obj.getCost();
	// Direct cost.
	if (costType == EstimateCostType.DIRECT) {
	    aux.setGrandTotalCostsDirect(aux.getGrandTotalCostsDirect() + estimatedCost);
	    aux.setGrandTotalActualCostsDirect(aux.getGrandTotalActualCostsDirect() + actualCost);
	}
	// If cost is indirect.
	else if (costType == EstimateCostType.INDIRECT) {
	    aux.setGrandTotalCostsIndirect(aux.getGrandTotalCostsIndirect() + estimatedCost);
	    aux.setGrandTotalActualCostsIndirect(aux.getGrandTotalActualCostsIndirect() + actualCost);
	}

	// Do the action.
	// Return success.
	this.estimateCostValueRepo.set(obj);
	this.projectAuxValueRepo.set(aux);

	if (isCreate) {
	    this.messageHelper.send(AuditAction.ACTION_CREATE, ConstantsRedis.OBJECT_ESTIMATE_COST,
		    obj.getKey());
	    return AlertBoxGenerator.SUCCESS.generateCreate(ConstantsRedis.OBJECT_ESTIMATE_COST,
		    obj.getName());
	}
	this.messageHelper.send(AuditAction.ACTION_UPDATE, ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getKey());
	return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getName());
    }

    /**
     * Revert old values.
     * 
     * @param obj
     */
    private void revertOldValues(EstimateCost obj) {
	// Revert old values.
	EstimateCost oldCost = this.estimateCostValueRepo.get(obj.getKey());
	double oldActualCost = oldCost.getActualCost();
	double oldEstimatedCost = oldCost.getCost();

	// Direct cost.
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(oldCost.getProject()));
	EstimateCostType costType = oldCost.getCostType();
	if (costType == EstimateCostType.DIRECT) {
	    aux.setGrandTotalCostsDirect(aux.getGrandTotalCostsDirect() - oldEstimatedCost);
	    aux.setGrandTotalActualCostsDirect(aux.getGrandTotalActualCostsDirect() - oldActualCost);
	}
	// If cost is indirect.
	else if (costType == EstimateCostType.INDIRECT) {
	    aux.setGrandTotalCostsIndirect(aux.getGrandTotalCostsIndirect() - oldEstimatedCost);
	    aux.setGrandTotalActualCostsIndirect(aux.getGrandTotalActualCostsIndirect() - oldActualCost);
	}
	this.projectAuxValueRepo.set(aux);
    }

}
