package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.helper.BeanHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.service.EquipmentExpenseService;
import com.cebedo.pmsys.utils.DateUtils;
import com.google.gson.Gson;

@Component
public class RunnableModelerEquipment implements Runnable {

    public static final String ATTR_DATA_SERIES_EQUIPMENT = "dataSeriesEquipment";
    public static final String ATTR_DATA_SERIES_EQUIPMENT_CUMULATIVE = "dataSeriesEquipmentCumulative";
    public static final String ATTR_EQUIPMENT_EXPENSE_LIST = "equipmentExpenseList";

    public static final String[] ATTRS_LIST = { ATTR_DATA_SERIES_EQUIPMENT,
	    ATTR_DATA_SERIES_EQUIPMENT_CUMULATIVE, ATTR_EQUIPMENT_EXPENSE_LIST,
	    ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE };

    private Project proj;
    private Model model;
    private List<HighchartsDataPoint> equipmentSeries;
    private List<HighchartsDataPoint> equipmentCumulative;

    private EquipmentExpenseService equipmentExpenseService;

    @Autowired
    @Qualifier(value = "equipmentExpenseService")
    public void setEquipmentExpenseService(EquipmentExpenseService equipmentExpenseService) {
	this.equipmentExpenseService = equipmentExpenseService;
    }

    /**
     * Set equipment attributes.
     * 
     * @param proj
     * @param model
     * @param equipmentSeries
     * @param equipmentCumulative
     */
    private void setAttributesEquipment() {
	double total = 0;
	List<EquipmentExpense> equipmentExpenses = this.equipmentExpenseService.listAsc(proj, true);
	for (EquipmentExpense expense : equipmentExpenses) {

	    // Y-axis.
	    double yValue = expense.getCost();
	    total += yValue;

	    // Tool tip.
	    Date datetime = expense.getDate();
	    String name = String.format("(Equipment) %s<br/>%s",
		    DateUtils.formatDate(datetime, DateUtils.PATTERN_DATE_TIME), expense.getName());

	    HighchartsDataPoint point = new HighchartsDataPoint(name, datetime.getTime(), yValue);
	    equipmentSeries.add(point);

	    // Cumulative.
	    HighchartsDataPoint pointCumulative = new HighchartsDataPoint(name, datetime.getTime(),
		    total);
	    equipmentCumulative.add(pointCumulative);
	}
	model.addAttribute(ATTR_DATA_SERIES_EQUIPMENT,
		new Gson().toJson(equipmentSeries, ArrayList.class));
	model.addAttribute(ATTR_DATA_SERIES_EQUIPMENT_CUMULATIVE,
		new Gson().toJson(equipmentCumulative, ArrayList.class));
	model.addAttribute(ATTR_EQUIPMENT_EXPENSE_LIST, equipmentExpenses);
	model.addAttribute(ConstantsRedis.OBJECT_EQUIPMENT_EXPENSE, new EquipmentExpense(proj));
    }

    /**
     * Get an instance from the Spring context.
     * 
     * @param proj2
     * @param model2
     * @return
     */
    public static RunnableModelerEquipment getCtxInstance(Project p, Model m,
	    List<HighchartsDataPoint> dS, List<HighchartsDataPoint> dSC) {
	BeanHelper beanHelper = new BeanHelper();
	RunnableModelerEquipment modeler = (RunnableModelerEquipment) beanHelper
		.getBean("runnableModelerEquipment");
	modeler.proj = p;
	modeler.model = m;
	modeler.equipmentSeries = dS;
	modeler.equipmentCumulative = dSC;
	return modeler;
    }

    @Override
    public void run() {
	setAttributesEquipment();
    }

}
