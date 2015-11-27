package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;

import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.google.gson.Gson;

public class GrapherProportionalComparison implements Runnable {

    public static final String ATTR_DATA_SERIES_PIE_DASHBOARD = "dataSeriesDashboardPie";

    private Model model;
    private ProjectAux projectAux;

    public GrapherProportionalComparison(Model model, ProjectAux projectAux) {
	this.model = model;
	this.projectAux = projectAux;
    }

    private void dashboardProportionalComparison() {
	// Proportional comparison.
	double materialsAccumulated = projectAux.getGrandTotalDelivery();
	double payrollAccumulated = projectAux.getGrandTotalPayroll();
	double otherExpensesAccumulated = projectAux.getGrandTotalOtherExpenses();
	double equipmentAccumulated = projectAux.getGrandTotalEquipmentExpenses();

	boolean isEmpty = materialsAccumulated == 0 && payrollAccumulated == 0
		&& otherExpensesAccumulated == 0 && equipmentAccumulated == 0;

	List<HighchartsDataPoint> projectPie = new ArrayList<HighchartsDataPoint>();
	projectPie.add(new HighchartsDataPoint("Inventory", materialsAccumulated));
	projectPie.add(new HighchartsDataPoint("Payroll", payrollAccumulated));
	projectPie.add(new HighchartsDataPoint("Equipment", equipmentAccumulated));
	projectPie.add(new HighchartsDataPoint("Other Expenses", otherExpensesAccumulated));
	model.addAttribute(ATTR_DATA_SERIES_PIE_DASHBOARD,
		isEmpty ? "[]" : new Gson().toJson(projectPie, ArrayList.class));
    }

    @Override
    public void run() {
	dashboardProportionalComparison();
    }

}
