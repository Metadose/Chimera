package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;

import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.pojo.HighchartsDataSeries;
import com.google.gson.Gson;

public class GrapherCumulativeComparison implements Runnable {

    public static final String ATTR_DATA_SERIES_DASHBOARD = "dataSeriesDashboard";

    private Model model;
    private List<HighchartsDataPoint> inventoryCumulative;
    private List<HighchartsDataPoint> payrollCumulative;
    private List<HighchartsDataPoint> otherExpensesCumulative;
    private List<HighchartsDataPoint> equipmentCumulative;

    public GrapherCumulativeComparison(Model model, List<HighchartsDataPoint> inventoryCumulative,
	    List<HighchartsDataPoint> payrollCumulative,
	    List<HighchartsDataPoint> otherExpensesCumulative,
	    List<HighchartsDataPoint> equipmentCumulative) {

	this.model = model;
	this.inventoryCumulative = inventoryCumulative;
	this.payrollCumulative = payrollCumulative;
	this.otherExpensesCumulative = otherExpensesCumulative;
	this.equipmentCumulative = equipmentCumulative;
    }

    /**
     * Construct comparison bar graph.
     * 
     * @param model
     * @param inventoryCumulative
     * @param payrollCumulative
     * @param otherExpensesCumulative
     * @param equipmentCumulative
     */
    private void dashboardCumulativeComparison() {
	// Construct comparison bar graph.
	List<HighchartsDataSeries> dashboardSeries = new ArrayList<HighchartsDataSeries>();
	dashboardSeries.add(new HighchartsDataSeries("Inventory Cumulative", inventoryCumulative));
	dashboardSeries.add(new HighchartsDataSeries("Payroll Cumulative", payrollCumulative));
	dashboardSeries.add(new HighchartsDataSeries("Equipment Cumulative", equipmentCumulative));
	dashboardSeries
		.add(new HighchartsDataSeries("Other Expenses Cumulative", otherExpensesCumulative));
	model.addAttribute(ATTR_DATA_SERIES_DASHBOARD,
		new Gson().toJson(dashboardSeries, ArrayList.class));
    }

    @Override
    public void run() {
	dashboardCumulativeComparison();
    }

}
