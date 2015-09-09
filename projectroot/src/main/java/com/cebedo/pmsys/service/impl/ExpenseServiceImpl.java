package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
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
	// TODO Auto-generated method stub
	return null;
    }

    @Transactional
    @Override
    public Expense get(String uuid) {
	// TODO Auto-generated method stub
	return null;
    }

    @Transactional
    @Override
    public List<Expense> list(Project proj) {
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
	    // TODO revertOldValues(obj);
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

	// Set the staff.
	long staffID = obj.getStaffID();
	Staff staff = this.staffDAO.getByID(staffID);
	obj.setStaff(staff);

	// Do the action.
	// Return success.
	this.expenseValueRepo.set(obj);
	this.projectAuxValueRepo.set(aux);

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
