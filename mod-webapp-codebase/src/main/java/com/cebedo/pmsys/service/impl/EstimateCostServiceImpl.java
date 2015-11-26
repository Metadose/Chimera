package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFRow;
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
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.TypeEstimateCost;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.ExcelHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.impl.EstimateCostValueRepoImpl;
import com.cebedo.pmsys.repository.impl.ProjectAuxValueRepoImpl;
import com.cebedo.pmsys.service.EstimateCostService;
import com.cebedo.pmsys.validator.EstimateCostValidator;

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

    private EstimateCostValueRepoImpl estimateCostValueRepo;
    private ProjectAuxValueRepoImpl projectAuxValueRepo;
    private ProjectDAO projectDAO;

    @Autowired
    EstimateCostValidator estimateCostValidator;

    @Autowired
    @Qualifier(value = "projectDAO")
    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    @Autowired
    @Qualifier(value = "projectAuxValueRepo")
    public void setProjectAuxValueRepo(ProjectAuxValueRepoImpl projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    @Autowired
    @Qualifier(value = "estimateCostValueRepo")
    public void setEstimateCostValueRepo(EstimateCostValueRepoImpl estimateCostValueRepo) {
	this.estimateCostValueRepo = estimateCostValueRepo;
    }

    @Override
    @Transactional
    public HSSFWorkbook exportXLS(long projID) {

	Project proj = this.projectDAO.getByIDWithAllCollections(projID);

	// Security check.
	if (!this.authHelper.hasAccess(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new HSSFWorkbook();
	}
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_EXPORT,
		ConstantsRedis.OBJECT_ESTIMATE_COST, projID);
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFSheet sheet = wb.createSheet("Estimated Costs");
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));

	// For headers.
	int rowIndex = 0;
	double estDirect = aux.getGrandTotalCostsDirect();
	double actDirect = aux.getGrandTotalActualCostsDirect();
	double estIndirect = aux.getGrandTotalCostsIndirect();
	double actIndirect = aux.getGrandTotalActualCostsIndirect();

	// Direct.
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Estimated Direct");
	row.createCell(1).setCellValue(estDirect);
	row.createCell(2).setCellValue("Actual Direct");
	row.createCell(3).setCellValue(actDirect);
	row.createCell(4).setCellValue("Difference");
	row.createCell(5).setCellValue(estDirect - actDirect);
	rowIndex++;

	// Indirect.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Estimated Indirect");
	row.createCell(1).setCellValue(estIndirect);
	row.createCell(2).setCellValue("Actual Indirect");
	row.createCell(3).setCellValue(actIndirect);
	row.createCell(4).setCellValue("Difference");
	row.createCell(5).setCellValue(estIndirect - actIndirect);
	rowIndex++;

	// Grand total.
	double estTotal = estDirect + estIndirect;
	double actTotal = actDirect + actIndirect;
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Estimated Total");
	row.createCell(1).setCellValue(estTotal);
	row.createCell(2).setCellValue("Actual Total");
	row.createCell(3).setCellValue(actTotal);
	row.createCell(4).setCellValue("Difference");
	row.createCell(5).setCellValue(estTotal - actTotal);
	rowIndex++;
	rowIndex++;

	// Create a cell and put a value in it.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Cost Type");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Estimated");
	row.createCell(3).setCellValue("Actual");
	row.createCell(4).setCellValue("Difference");
	rowIndex++;

	// Setup the table.
	// Staff list data.
	List<EstimateCost> costs = list(proj);
	List<EstimateCost> indirectCosts = new ArrayList<EstimateCost>();

	// Direct costs.
	for (EstimateCost cost : costs) {

	    TypeEstimateCost costType = cost.getCostType();
	    if (costType == TypeEstimateCost.INDIRECT) {
		indirectCosts.add(cost);
		continue;
	    }

	    HSSFRow costRow = sheet.createRow(rowIndex);
	    double estCost = cost.getCost();
	    double actCost = cost.getActualCost();

	    costRow.createCell(0).setCellValue(costType.getLabel());
	    costRow.createCell(1).setCellValue(cost.getName());
	    costRow.createCell(2).setCellValue(estCost);
	    costRow.createCell(3).setCellValue(actCost);
	    costRow.createCell(4).setCellValue(estCost - actCost);

	    rowIndex++;
	}

	// Indirect costs.
	rowIndex++;
	for (EstimateCost cost : indirectCosts) {

	    HSSFRow costRow = sheet.createRow(rowIndex);
	    double estCost = cost.getCost();
	    double actCost = cost.getActualCost();
	    TypeEstimateCost costType = cost.getCostType();

	    costRow.createCell(0).setCellValue(costType.getLabel());
	    costRow.createCell(1).setCellValue(cost.getName());
	    costRow.createCell(2).setCellValue(estCost);
	    costRow.createCell(3).setCellValue(actCost);
	    costRow.createCell(4).setCellValue(estCost - actCost);

	    rowIndex++;
	}
	return wb;
    }

    @Override
    @Transactional
    public String createMassCosts(List<EstimateCost> costs, BindingResult result) {

	// Security check.
	if (costs.size() > 0 && !this.authHelper.hasAccess(costs.get(0))) {
	    long projectID = costs.get(0).getProject().getId();
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, projectID);
	    return AlertBoxFactory.ERROR;
	}

	for (EstimateCost cost : costs) {
	    // Service layer form validation.
	    this.estimateCostValidator.validate(cost, result);
	    if (result.hasErrors()) {
		return this.validationHelper.errorMessageHTML(result);
	    }
	}

	// Log.
	if (costs.size() > 0) {
	    Project proj = costs.get(0).getProject();
	    this.messageHelper.auditableKey(AuditAction.ACTION_CREATE_MASS, Project.OBJECT_NAME,
		    proj.getId(), ConstantsRedis.OBJECT_ESTIMATE_COST, "Mass", proj, "Mass");
	}

	// If reaches this point, do actual service.
	for (EstimateCost cost : costs) {
	    set(cost, result);
	}
	return null;
    }

    @Override
    @Transactional
    public List<EstimateCost> convertExcelToCostList(MultipartFile multipartFile, Project project) {

	// Security check.
	if (!this.authHelper.hasAccess(project)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return null;
	}

	// Service layer form validation.
	boolean valid = this.validationHelper.fileIsNotNullOrEmpty(multipartFile);
	if (!valid) {
	    return null;
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_CONVERT_FILE,
		Project.OBJECT_NAME, project.getId(), MultipartFile.class.getName());

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
			String name = (String) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? ""
					: this.excelHelper.getValueAsExpected(workbook, cell));
			cost.setName(name);
			continue;

		    case EXCEL_COLUMN_COST_ESTIMATED:
			Double costVal = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			cost.setCost(costVal);
			continue;

		    case EXCEL_COLUMN_COST_ACTUAL:
			costVal = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null
				? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			cost.setActualCost(costVal);
			continue;

		    case EXCEL_COLUMN_COST_TYPE:
			String costType = (String) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? ""
					: this.excelHelper.getValueAsExpected(workbook, cell));
			cost.setCostType(TypeEstimateCost.of(costType));
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
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	    return AlertBoxFactory.ERROR;
	}

	// Log.
	Project proj = obj.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey(), proj, obj.getName());

	// Project auxiliary on grand totals of costs.
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(obj.getProject()));
	TypeEstimateCost costType = obj.getCostType();
	double actualCost = obj.getActualCost();
	double estimatedCost = obj.getCost();
	// Direct cost.
	if (costType == TypeEstimateCost.DIRECT) {
	    aux.setGrandTotalCostsDirect(aux.getGrandTotalCostsDirect() - estimatedCost);
	    aux.setGrandTotalActualCostsDirect(aux.getGrandTotalActualCostsDirect() - actualCost);
	}
	// If cost is indirect.
	else if (costType == TypeEstimateCost.INDIRECT) {
	    aux.setGrandTotalCostsIndirect(aux.getGrandTotalCostsIndirect() - estimatedCost);
	    aux.setGrandTotalActualCostsIndirect(aux.getGrandTotalActualCostsIndirect() - actualCost);
	}

	this.estimateCostValueRepo.delete(key);
	this.projectAuxValueRepo.set(aux);
	return AlertBoxFactory.SUCCESS.generateDelete(ConstantsRedis.OBJECT_ESTIMATE_COST,
		obj.getName());
    }

    @Transactional
    @Override
    public EstimateCost get(String key) {
	EstimateCost obj = this.estimateCostValueRepo.get(key);
	// Security check.
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	    return new EstimateCost();
	}
	// Log.
	this.messageHelper.nonAuditableKeyNoAssoc(AuditAction.ACTION_GET,
		ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	return obj;
    }

    @Override
    @Transactional
    public List<EstimateCost> list(Project proj) {
	return list(proj, false);
    }

    @Transactional
    @Override
    public List<EstimateCost> list(Project proj, boolean override) {
	// Security check.
	if (!override && !this.authHelper.hasAccess(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<EstimateCost>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_ESTIMATE_COST);

	String pattern = EstimateCost.constructPattern(proj);
	Set<String> keys = this.estimateCostValueRepo.keys(pattern);

	List<EstimateCost> costs = this.estimateCostValueRepo.multiGet(keys);
	return costs;
    }

    @Transactional
    @Override
    public String set(EstimateCost obj, BindingResult result) {
	if (!this.authHelper.hasAccess(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey());
	    return AlertBoxFactory.ERROR;
	}

	this.estimateCostValidator.validate(obj, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
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
	TypeEstimateCost costType = obj.getCostType();
	double actualCost = obj.getActualCost();
	double estimatedCost = obj.getCost();
	// Direct cost.
	if (costType == TypeEstimateCost.DIRECT) {
	    aux.setGrandTotalCostsDirect(aux.getGrandTotalCostsDirect() + estimatedCost);
	    aux.setGrandTotalActualCostsDirect(aux.getGrandTotalActualCostsDirect() + actualCost);
	}
	// If cost is indirect.
	else if (costType == TypeEstimateCost.INDIRECT) {
	    aux.setGrandTotalCostsIndirect(aux.getGrandTotalCostsIndirect() + estimatedCost);
	    aux.setGrandTotalActualCostsIndirect(aux.getGrandTotalActualCostsIndirect() + actualCost);
	}

	// Do the action.
	// Return success.
	this.estimateCostValueRepo.set(obj);
	this.projectAuxValueRepo.set(aux);

	Project proj = obj.getProject();

	if (isCreate) {
	    this.messageHelper.auditableKey(AuditAction.ACTION_CREATE, Project.OBJECT_NAME, proj.getId(),
		    ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey(), proj, obj.getName());
	    return AlertBoxFactory.SUCCESS.generateCreate(ConstantsRedis.OBJECT_ESTIMATE_COST,
		    obj.getName());
	}
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_ESTIMATE_COST, obj.getKey(), proj, obj.getName());
	return AlertBoxFactory.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_ESTIMATE_COST,
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
	TypeEstimateCost costType = oldCost.getCostType();
	if (costType == TypeEstimateCost.DIRECT) {
	    aux.setGrandTotalCostsDirect(aux.getGrandTotalCostsDirect() - oldEstimatedCost);
	    aux.setGrandTotalActualCostsDirect(aux.getGrandTotalActualCostsDirect() - oldActualCost);
	}
	// If cost is indirect.
	else if (costType == TypeEstimateCost.INDIRECT) {
	    aux.setGrandTotalCostsIndirect(aux.getGrandTotalCostsIndirect() - oldEstimatedCost);
	    aux.setGrandTotalActualCostsIndirect(aux.getGrandTotalActualCostsIndirect() - oldActualCost);
	}
	this.projectAuxValueRepo.set(aux);
    }

}
