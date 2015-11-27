package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;

import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.pojo.HighchartsDataSeries;
import com.google.gson.Gson;

public class GrapherSeriesComparison implements Runnable {

    private static final String ATTR_DATA_SERIES_DASHBOARD_NOT_CUMULATIVE = "dataSeriesDashboardNotCumulative";

    private Model model;
    private List<HighchartsDataPoint> inventorySeries;
    private List<HighchartsDataPoint> payrollSeries;
    private List<HighchartsDataPoint> otherExpensesSeries;
    private List<HighchartsDataPoint> equipmentSeries;

    public GrapherSeriesComparison(Model model, List<HighchartsDataPoint> inventorySeries,
	    List<HighchartsDataPoint> payrollSeries, List<HighchartsDataPoint> otherExpensesSeries,
	    List<HighchartsDataPoint> equipmentSeries) {
	this.model = model;
	this.inventorySeries = inventorySeries;
	this.payrollSeries = payrollSeries;
	this.otherExpensesSeries = otherExpensesSeries;
	this.equipmentSeries = equipmentSeries;
    }

    /**
     * Construct comparison bar graph.
     * 
     * @param model
     * @param inventorySeries
     * @param payrollSeries
     * @param otherExpensesSeries
     * @param equipmentSeries
     */
    private void dashboardSeriesComparison() {

	// Construct comparison bar graph.
	List<HighchartsDataSeries> dashboardSeries = new ArrayList<HighchartsDataSeries>();
	dashboardSeries.add(new HighchartsDataSeries("Inventory", inventorySeries));
	dashboardSeries.add(new HighchartsDataSeries("Payroll", payrollSeries));
	dashboardSeries.add(new HighchartsDataSeries("Equipment", equipmentSeries));
	dashboardSeries.add(new HighchartsDataSeries("Other Expenses", otherExpensesSeries));
	model.addAttribute(ATTR_DATA_SERIES_DASHBOARD_NOT_CUMULATIVE,
		new Gson().toJson(dashboardSeries, ArrayList.class));
    }

    @Override
    public void run() {
	dashboardSeriesComparison();
    }

}
