package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.EquipmentExpenseValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.EquipmentExpenseService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.validator.EquipmentExpenseValidator;

public class EquipmentExpenseServiceImpl implements EquipmentExpenseService {

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private ProjectAuxValueRepo projectAuxValueRepo;
    private EquipmentExpenseValueRepo equipmentExpenseValueRepo;
    private StaffDAO staffDAO;
    private ProjectDAO projectDAO;

    @Autowired
    EquipmentExpenseValidator equipmentExpenseValidator;

    @Autowired
    @Qualifier(value = "projectDAO")
    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    @Autowired
    @Qualifier(value = "staffDAO")
    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    @Autowired
    @Qualifier(value = "projectAuxValueRepo")
    public void setProjectAuxValueRepo(ProjectAuxValueRepo projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    @Autowired
    @Qualifier(value = "equipmentExpenseValueRepo")
    public void setEquipmentExpenseValueRepo(EquipmentExpenseValueRepo equipmentExpenseValueRepo) {
	this.equipmentExpenseValueRepo = equipmentExpenseValueRepo;
    }

    @Transactional
    @Override
    public HSSFWorkbook exportXLS(long projID) {
	Project proj = this.projectDAO.getByIDWithAllCollections(projID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new HSSFWorkbook();
	}
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_EXPORT,
		ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, projID);
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFSheet sheet = wb.createSheet("Equipment Expenses");

	// For grand total.
	int rowIndex = 0;
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Grand Total");
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));
	row.createCell(1).setCellValue(aux.getGrandTotalEquipmentExpenses());
	rowIndex++;
	rowIndex++;

	// For headers.
	row = sheet.createRow(rowIndex);
	rowIndex++;

	// Create a cell and put a value in it.
	row.createCell(0).setCellValue("Date");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Staff");
	row.createCell(3).setCellValue("Cost");

	// Setup the table.
	// Staff list data.
	List<EquipmentExpense> expenses = listDesc(proj);
	for (EquipmentExpense expense : expenses) {
	    HSSFRow expenseRow = sheet.createRow(rowIndex);
	    expenseRow.createCell(0).setCellValue(DateUtils.formatDate(expense.getDate()));
	    expenseRow.createCell(1).setCellValue(expense.getName());
	    expenseRow.createCell(2).setCellValue(expense.getStaff().getFullNameWithMiddleName());
	    expenseRow.createCell(3).setCellValue(expense.getCost());
	    rowIndex++;
	}
	return wb;
    }

    @Transactional
    @Override
    public String delete(String key) {
	EquipmentExpense obj = this.equipmentExpenseValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	Project proj = obj.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, obj.getKey(), proj, obj.getName());

	// Revert old values in the auxiliary.
	revertOldValues(obj);

	this.equipmentExpenseValueRepo.delete(key);
	return AlertBoxGenerator.SUCCESS.generateDelete(ConstantsRedis.DISPLAY_EQUIPMENT_EXPENSE,
		obj.getName());
    }

    @Transactional
    @Override
    public EquipmentExpense get(String key) {
	EquipmentExpense obj = this.equipmentExpenseValueRepo.get(key);
	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, obj.getKey());
	    return new EquipmentExpense();
	}
	// Log.
	this.messageHelper.nonAuditableKeyNoAssoc(AuditAction.ACTION_GET,
		ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, obj.getKey());
	return obj;
    }

    @Transactional
    @Override
    public List<EquipmentExpense> listAsc(Project proj) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<EquipmentExpense>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE);
	String pattern = EquipmentExpense.constructPattern(proj);
	Set<String> keys = this.equipmentExpenseValueRepo.keys(pattern);
	List<EquipmentExpense> expenses = this.equipmentExpenseValueRepo.multiGet(keys);

	// Sort the list in descending order.
	Collections.sort(expenses, new Comparator<EquipmentExpense>() {
	    @Override
	    public int compare(EquipmentExpense aObj, EquipmentExpense bObj) {
		Date aStart = aObj.getDate();
		Date bStart = bObj.getDate();
		return aStart.before(bStart) ? -1 : aStart.after(bStart) ? 1 : 0;
	    }
	});
	return expenses;
    }

    @Transactional
    @Override
    public List<EquipmentExpense> listDesc(Project proj) {
	return listDesc(proj, null, null);
    }

    /**
     * Revert old values in the auxiliary.
     * 
     * @param obj
     */
    private void revertOldValues(EquipmentExpense obj) {
	// Project auxiliary on grand totals of costs.
	EquipmentExpense oldExpense = this.equipmentExpenseValueRepo.get(obj.getKey());
	double oldCost = oldExpense.getCost();
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(oldExpense.getProject()));
	aux.setGrandTotalEquipmentExpenses(aux.getGrandTotalEquipmentExpenses() - oldCost);
	this.projectAuxValueRepo.set(aux);
    }

    @Transactional
    @Override
    public String set(EquipmentExpense obj, BindingResult result) {
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	this.equipmentExpenseValidator.validate(obj, result);
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
	double cost = obj.getCost();
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(obj.getProject()));
	aux.setGrandTotalEquipmentExpenses(aux.getGrandTotalEquipmentExpenses() + cost);
	this.projectAuxValueRepo.set(aux);

	// Set the staff.
	long staffID = obj.getStaffID();
	Staff staff = this.staffDAO.getByID(staffID);
	obj.setStaff(staff);

	// Do the action.
	// Return success.
	this.equipmentExpenseValueRepo.set(obj);
	Project proj = obj.getProject();

	if (isCreate) {
	    this.messageHelper.auditableKey(AuditAction.ACTION_CREATE, Project.OBJECT_NAME, proj.getId(),
		    ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, obj.getKey(), proj, obj.getName());
	    return AlertBoxGenerator.SUCCESS.generateCreate(ConstantsRedis.DISPLAY_EQUIPMENT_EXPENSE,
		    obj.getName());
	}
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, obj.getKey(), proj, obj.getName());
	return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.DISPLAY_EQUIPMENT_EXPENSE,
		obj.getName());
    }

    @Override
    @Transactional
    public double getTotal(List<EquipmentExpense> expenses) {

	double total = 0;
	Project proj = null;
	for (EquipmentExpense obj : expenses) {

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(obj)) {
		this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE,
			obj.getKey());
		return 0.0;
	    }
	    proj = proj == null ? obj.getProject() : proj;
	    total += obj.getCost();
	}
	// Log.
	this.messageHelper.nonAuditableIDWithAssocWithKey(AuditAction.ACTION_GET, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, "Grand Total");
	return total;
    }

    @Transactional
    @Override
    public List<EquipmentExpense> listDesc(Project proj, Date startDate, Date endDate) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<EquipmentExpense>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE);
	String pattern = EquipmentExpense.constructPattern(proj);
	Set<String> keys = this.equipmentExpenseValueRepo.keys(pattern);
	List<EquipmentExpense> expenses = this.equipmentExpenseValueRepo.multiGet(keys);

	// If we are getting a specific range.
	boolean isRange = startDate != null && endDate != null;
	if (isRange) {
	    List<EquipmentExpense> toInclude = new ArrayList<EquipmentExpense>();
	    for (EquipmentExpense obj : expenses) {
		Date objDate = obj.getDate();

		// If the date is equal to the start or end,
		// if date is between start and end.
		// Add to payrolls to include.
		if (objDate.equals(startDate) || objDate.equals(endDate)
			|| (objDate.after(startDate) && objDate.before(endDate))) {
		    toInclude.add(obj);
		}
	    }
	    expenses = toInclude;
	}

	// Sort the list in descending order.
	Collections.sort(expenses, new Comparator<EquipmentExpense>() {
	    @Override
	    public int compare(EquipmentExpense aObj, EquipmentExpense bObj) {
		Date aStart = aObj.getDate();
		Date bStart = bObj.getDate();
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});
	return expenses;
    }

}
