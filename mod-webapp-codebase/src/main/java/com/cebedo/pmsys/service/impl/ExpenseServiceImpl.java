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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.ExpenseValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.ExpenseService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.validator.ExpenseValidator;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private ExpenseValueRepo expenseValueRepo;
    private ProjectAuxValueRepo projectAuxValueRepo;
    private StaffDAO staffDAO;
    private ProjectDAO projectDAO;

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
    @Qualifier(value = "expenseValueRepo")
    public void setExpenseValueRepo(ExpenseValueRepo expenseValueRepo) {
	this.expenseValueRepo = expenseValueRepo;
    }

    @Autowired
    ExpenseValidator expenseValidator;

    @Transactional
    @Override
    public String delete(String key) {
	Expense obj = this.expenseValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_EXPENSE, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	Project proj = obj.getProject();
	this.messageHelper.auditableKey(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_EXPENSE, obj.getKey(), proj, obj.getName());

	// Revert old values in the auxiliary.
	revertOldValues(obj);

	this.expenseValueRepo.delete(key);
	return AlertBoxGenerator.SUCCESS.generateDelete(ConstantsRedis.OBJECT_EXPENSE, obj.getName());
    }

    /**
     * Revert old values in the auxiliary.
     * 
     * @param obj
     */
    private void revertOldValues(Expense obj) {
	// Project auxiliary on grand totals of costs.
	Expense oldExpense = this.expenseValueRepo.get(obj.getKey());
	double oldCost = oldExpense.getCost();
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(oldExpense.getProject()));
	aux.setGrandTotalOtherExpenses(aux.getGrandTotalOtherExpenses() - oldCost);
	this.projectAuxValueRepo.set(aux);
    }

    @Transactional
    @Override
    public Expense get(String key) {
	Expense obj = this.expenseValueRepo.get(key);
	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_EXPENSE, obj.getKey());
	    return new Expense();
	}
	// Log.
	this.messageHelper.nonAuditableKeyNoAssoc(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_EXPENSE,
		obj.getKey());
	return obj;
    }

    @Transactional
    @Override
    public List<Expense> listAsc(Project proj) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Expense>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_EXPENSE);
	String pattern = Expense.constructPattern(proj);
	Set<String> keys = this.expenseValueRepo.keys(pattern);
	List<Expense> expenses = this.expenseValueRepo.multiGet(keys);

	// Sort the list in descending order.
	Collections.sort(expenses, new Comparator<Expense>() {
	    @Override
	    public int compare(Expense aObj, Expense bObj) {
		Date aStart = aObj.getDate();
		Date bStart = bObj.getDate();
		return aStart.before(bStart) ? -1 : aStart.after(bStart) ? 1 : 0;
	    }
	});

	return expenses;
    }

    @Transactional
    @Override
    public List<Expense> listDesc(Project proj) {
	return listDesc(proj, null, null);
    }

    @Override
    @Transactional
    public HSSFWorkbook exportXLS(long projID) {

	Project proj = this.projectDAO.getByIDWithAllCollections(projID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new HSSFWorkbook();
	}
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_EXPORT,
		ConstantsRedis.OBJECT_EXPENSE, projID);
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFSheet sheet = wb.createSheet("Other Expenses");

	// For grand total.
	int rowIndex = 0;
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Grand Total");
	ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));
	row.createCell(1).setCellValue(aux.getGrandTotalOtherExpenses());
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
	List<Expense> expenses = listDesc(proj);
	for (Expense expense : expenses) {
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
    public String set(Expense obj, BindingResult result) {
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorizedKey(ConstantsRedis.OBJECT_EXPENSE, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	this.expenseValidator.validate(obj, result);
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
	aux.setGrandTotalOtherExpenses(aux.getGrandTotalOtherExpenses() + cost);
	this.projectAuxValueRepo.set(aux);

	// Set the staff.
	long staffID = obj.getStaffID();
	Staff staff = this.staffDAO.getByID(staffID);
	obj.setStaff(staff);

	// Do the action.
	// Return success.
	this.expenseValueRepo.set(obj);
	Project proj = obj.getProject();

	if (isCreate) {
	    this.messageHelper.auditableKey(AuditAction.ACTION_CREATE, Project.OBJECT_NAME, proj.getId(),
		    ConstantsRedis.OBJECT_EXPENSE, obj.getKey(), proj, obj.getName());
	    return AlertBoxGenerator.SUCCESS.generateCreate(ConstantsRedis.OBJECT_EXPENSE,
		    obj.getName());
	}
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_EXPENSE, obj.getKey(), proj, obj.getName());
	return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_EXPENSE, obj.getName());
    }

    @Override
    @Transactional
    public List<Expense> listDesc(Project proj, Date startDate, Date endDate) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Expense>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_LIST, Project.OBJECT_NAME,
		proj.getId(), ConstantsRedis.OBJECT_EXPENSE);
	String pattern = Expense.constructPattern(proj);
	Set<String> keys = this.expenseValueRepo.keys(pattern);
	List<Expense> expenses = this.expenseValueRepo.multiGet(keys);

	// If we are getting a specific range.
	boolean isRange = startDate != null && endDate != null;
	if (isRange) {
	    List<Expense> toInclude = new ArrayList<Expense>();
	    for (Expense obj : expenses) {
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
	Collections.sort(expenses, new Comparator<Expense>() {
	    @Override
	    public int compare(Expense aObj, Expense bObj) {
		Date aStart = aObj.getDate();
		Date bStart = bObj.getDate();
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});

	return expenses;
    }
}
