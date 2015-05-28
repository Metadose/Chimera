package com.cebedo.pmsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.model.Expense;
import com.cebedo.pmsys.service.ExpenseService;

@Controller
@RequestMapping(Expense.OBJECT_NAME)
public class ExpenseController {

    public static final String JSP_LIST = Expense.OBJECT_NAME + "/expenseList";
    public static final String JSP_EDIT = Expense.OBJECT_NAME + "/expenseEdit";

    private ExpenseService expenseService;

    @Autowired(required = true)
    @Qualifier(value = "expenseService")
    public void setExpenseService(ExpenseService s) {
	this.expenseService = s;
    }

    @RequestMapping(value = SystemConstants.REQUEST_CREATE + "/"
	    + SystemConstants.FROM + "/{" + SystemConstants.ORIGIN + "}/{"
	    + SystemConstants.ORIGIN_ID + "}")
    public String createExpenseFromOrigin(
	    @ModelAttribute(Expense.OBJECT_NAME) Expense expense,
	    @PathVariable(SystemConstants.ORIGIN) String origin,
	    @PathVariable(SystemConstants.ORIGIN_ID) long originID,
	    SessionStatus status) {
	if (expense.getId() == 0) {
	    this.expenseService.create(expense);
	} else {
	    this.expenseService.update(expense);
	}
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + origin + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + originID;
    }

}
