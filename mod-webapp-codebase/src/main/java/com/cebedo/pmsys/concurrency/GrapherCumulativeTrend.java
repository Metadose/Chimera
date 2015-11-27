package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.ui.Model;

import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.google.gson.Gson;

public class GrapherCumulativeTrend implements Runnable {

    public static final String ATTR_DATA_SERIES_PROJECT = "dataSeriesProject";

    private Model model;
    private List<HighchartsDataPoint> inventorySeries;
    private List<HighchartsDataPoint> payrollSeries;
    private List<HighchartsDataPoint> equipmentSeries;
    private List<HighchartsDataPoint> otherExpensesSeries;

    public GrapherCumulativeTrend(Model model, List<HighchartsDataPoint> inventorySeries,
	    List<HighchartsDataPoint> payrollSeries, List<HighchartsDataPoint> equipmentSeries,
	    List<HighchartsDataPoint> otherExpensesSeries) {
	this.model = model;
	this.inventorySeries = inventorySeries;
	this.payrollSeries = payrollSeries;
	this.equipmentSeries = equipmentSeries;
	this.otherExpensesSeries = otherExpensesSeries;
    }

    /**
     * Accumulation of expenses in this project.
     * 
     * @param model
     * @param inventorySeries
     * @param payrollSeries
     * @param equipmentSeries
     * @param otherExpensesSeries
     */
    private void dashboardCumulativeTrend() {
	// Accumulation of expenses in this project.
	List<HighchartsDataPoint> projectCumulative = new ArrayList<HighchartsDataPoint>();
	projectCumulative.addAll(inventorySeries);
	projectCumulative.addAll(payrollSeries);
	projectCumulative.addAll(equipmentSeries);
	projectCumulative.addAll(otherExpensesSeries);

	// To sort in ascending,
	Collections.sort(projectCumulative, new Comparator<HighchartsDataPoint>() {
	    @Override
	    public int compare(HighchartsDataPoint aObj, HighchartsDataPoint bObj) {
		long aStart = aObj.getX();
		long bStart = bObj.getX();
		return aStart < bStart ? -1 : aStart > bStart ? 1 : 0;
	    }
	});

	// Update the values to create progress of expenditures.
	double cumulative = 0;
	for (HighchartsDataPoint point : projectCumulative) {
	    cumulative += point.getY();
	    point.setY(cumulative);
	}
	model.addAttribute(ATTR_DATA_SERIES_PROJECT,
		new Gson().toJson(projectCumulative, ArrayList.class));
    }

    @Override
    public void run() {
	dashboardCumulativeTrend();
    }

}
