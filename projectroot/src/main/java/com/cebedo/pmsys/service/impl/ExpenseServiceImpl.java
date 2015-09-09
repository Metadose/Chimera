package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.ExpenseValueRepo;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.ExpenseService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();

    private ExpenseValueRepo expenseValueRepo;
    private ProjectAuxValueRepo projectAuxValueRepo;
    private StaffDAO staffDAO;

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

    @Transactional
    @Override
    public String delete(String key) {
	Expense obj = this.expenseValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_EXPENSE, obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, ConstantsRedis.OBJECT_EXPENSE, obj.getKey());

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
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_EXPENSE, obj.getKey());
	    return new Expense();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, ConstantsRedis.OBJECT_EXPENSE, obj.getKey());
	return obj;
    }

    @Transactional
    @Override
    public List<Expense> listAsc(Project proj) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<Expense>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, proj.getId(),
		ConstantsRedis.OBJECT_EXPENSE);
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
    public String set(Expense obj) {
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_EXPENSE, obj.getKey());
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

	if (isCreate) {
	    this.messageHelper.send(AuditAction.ACTION_CREATE, ConstantsRedis.OBJECT_EXPENSE,
		    obj.getKey());
	    return AlertBoxGenerator.SUCCESS
		    .generateCreate(ConstantsRedis.OBJECT_EXPENSE, obj.getName());
	}
	this.messageHelper.send(AuditAction.ACTION_UPDATE, ConstantsRedis.OBJECT_EXPENSE, obj.getKey());
	return AlertBoxGenerator.SUCCESS.generateUpdate(ConstantsRedis.OBJECT_EXPENSE, obj.getName());
    }

}
