package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.cebedo.pmsys.base.IRunnableModeler;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.service.ExpenseService;
import com.cebedo.pmsys.utils.DateUtils;
import com.google.gson.Gson;

@Component
public class RunnableModelerOtherExpenses implements IRunnableModeler {

    public static final String ATTR_DATA_SERIES_OTHER_EXPENSES = "dataSeriesOtherExpenses";
    public static final String ATTR_DATA_SERIES_OTHER_EXPENSES_CUMULATIVE = "dataSeriesOtherExpensesCumulative";
    public static final String ATTR_EXPENSE_LIST = "expenseList";

    private Project proj;
    private Model model;
    private List<HighchartsDataPoint> dataSeries;
    private List<HighchartsDataPoint> otherExpensesCumulative;
    private boolean alive = false;

    private static ApplicationContext ctx;
    private static RunnableModelerOtherExpenses MODELER;

    private ExpenseService expenseService;

    @Autowired(required = true)
    @Qualifier(value = "expenseService")
    public void setExpenseService(ExpenseService expenseService) {
	this.expenseService = expenseService;
    }

    /**
     * Set attributes of Other Expenses tab.
     * 
     * @param proj
     * @param model
     * @param dataSeries
     * @param otherExpensesCumulative
     * @return
     */
    private void setAttributesOtherExpenses() {
	double total = 0;
	List<Expense> expenses = this.expenseService.listAsc(proj, true);
	for (Expense expense : expenses) {

	    // Y-axis.
	    double yValue = expense.getCost();
	    total += yValue;

	    // Tool tip.
	    Date datetime = expense.getDate();
	    String name = String.format("(Other Expenses) %s<br/>%s",
		    DateUtils.formatDate(datetime, DateUtils.PATTERN_DATE_TIME), expense.getName());

	    HighchartsDataPoint point = new HighchartsDataPoint(name, datetime.getTime(), yValue);
	    dataSeries.add(point);

	    // Cumulative.
	    HighchartsDataPoint pointCumulative = new HighchartsDataPoint(name, datetime.getTime(),
		    total);
	    otherExpensesCumulative.add(pointCumulative);
	}
	model.addAttribute(ATTR_DATA_SERIES_OTHER_EXPENSES,
		new Gson().toJson(dataSeries, ArrayList.class));
	model.addAttribute(ATTR_DATA_SERIES_OTHER_EXPENSES_CUMULATIVE,
		new Gson().toJson(otherExpensesCumulative, ArrayList.class));
	model.addAttribute(ATTR_EXPENSE_LIST, expenses);
	model.addAttribute(ConstantsRedis.OBJECT_EXPENSE, new Expense(proj));
    }

    /**
     * Get an instance from the Spring context.
     * 
     * @param proj2
     * @param model2
     * @return
     */
    public static RunnableModelerOtherExpenses getCtxInstance(Project p, Model m,
	    List<HighchartsDataPoint> dS, List<HighchartsDataPoint> dSC) {
	RunnableModelerOtherExpenses modeler = null;
	try {
	    modeler = (RunnableModelerOtherExpenses) MODELER.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
	modeler.proj = p;
	modeler.model = m;
	modeler.dataSeries = dS;
	modeler.otherExpensesCumulative = dSC;
	return modeler;
    }

    @Override
    public void run() {
	alive = true;
	setAttributesOtherExpenses();
	alive = false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	ctx = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	MODELER = (RunnableModelerOtherExpenses) ctx.getBean("runnableModelerOtherExpenses");
    }

    @Override
    public boolean isAlive() {
	return alive;
    }

}
