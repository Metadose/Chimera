package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.controller.ProjectController;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.TypeEstimateCost;
import com.cebedo.pmsys.helper.BeanHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.service.EstimateCostService;
import com.cebedo.pmsys.service.EstimationOutputService;
import com.google.gson.Gson;

@Component
public class RunnableModelerEstimate implements Runnable {

    // Estimate.
    public static final String ATTR_DATA_SERIES_PIE_COSTS_ESTIMATED = "dataSeriesCostsEstimated";
    public static final String ATTR_DATA_SERIES_PIE_COSTS_ACTUAL = "dataSeriesCostsActual";
    public static final String ATTR_ESTIMATE_DIRECT_COST_LIST = "directCostList";
    public static final String ATTR_ESTIMATE_INDIRECT_COST_LIST = "indirectCostList";
    public static final String ATTR_ESTIMATE_OUTPUT_LIST = "estimationOutputList";

    public static final String[] ATTRS_LIST = { ATTR_DATA_SERIES_PIE_COSTS_ESTIMATED,
	    ATTR_DATA_SERIES_PIE_COSTS_ACTUAL, ATTR_ESTIMATE_DIRECT_COST_LIST,
	    ATTR_ESTIMATE_INDIRECT_COST_LIST, ATTR_ESTIMATE_OUTPUT_LIST,
	    ProjectController.ATTR_ESTIMATE_COST_LIST, ConstantsRedis.OBJECT_ESTIMATE_COST };

    private Project proj;
    private ProjectAux projectAux;
    private Model model;

    private EstimateCostService estimateCostService;
    private EstimationOutputService estimationOutputService;

    @Autowired(required = true)
    @Qualifier(value = "estimateCostService")
    public void setEstimateCostService(EstimateCostService estimateCostService) {
	this.estimateCostService = estimateCostService;
    }

    @Autowired(required = true)
    @Qualifier(value = "estimationOutputService")
    public void setEstimationOutputService(EstimationOutputService estimationOutputService) {
	this.estimationOutputService = estimationOutputService;
    }

    public RunnableModelerEstimate() {
	;
    }

    @Override
    public void run() {
	setAttributesEstimate();
    }

    /**
     * Set attributes used in Estimate.
     * 
     * @param proj
     * @param projectAux
     * @param model
     */
    private void setAttributesEstimate() {

	// Estimated.
	List<HighchartsDataPoint> pie = new ArrayList<HighchartsDataPoint>();
	double direct = projectAux.getGrandTotalCostsDirect();
	double indirect = projectAux.getGrandTotalCostsIndirect();
	pie.add(new HighchartsDataPoint("Direct", direct));
	pie.add(new HighchartsDataPoint("Indirect", indirect));
	model.addAttribute(ATTR_DATA_SERIES_PIE_COSTS_ESTIMATED,
		(direct == 0 && indirect == 0) ? "[]" : new Gson().toJson(pie, ArrayList.class));

	// Actual.
	pie = new ArrayList<HighchartsDataPoint>();
	direct = projectAux.getGrandTotalActualCostsDirect();
	indirect = projectAux.getGrandTotalActualCostsIndirect();
	pie.add(new HighchartsDataPoint("Direct", direct));
	pie.add(new HighchartsDataPoint("Indirect", indirect));
	model.addAttribute(ATTR_DATA_SERIES_PIE_COSTS_ACTUAL,
		(direct == 0 && indirect == 0) ? "[]" : new Gson().toJson(pie, ArrayList.class));

	// Selectors and forms.
	model.addAttribute(ProjectController.ATTR_ESTIMATE_COST_LIST,
		TypeEstimateCost.class.getEnumConstants());
	model.addAttribute(ConstantsRedis.OBJECT_ESTIMATE_COST, new EstimateCost(proj));

	// Prepare the estimate costs.
	List<EstimateCost> allCosts = this.estimateCostService.list(proj, true);
	List<EstimateCost> costsDirect = new ArrayList<EstimateCost>();
	List<EstimateCost> costsIndirect = new ArrayList<EstimateCost>();
	for (EstimateCost cost : allCosts) {
	    TypeEstimateCost costType = cost.getCostType();
	    if (costType == TypeEstimateCost.DIRECT) {
		costsDirect.add(cost);
	    } else if (costType == TypeEstimateCost.INDIRECT) {
		costsIndirect.add(cost);
	    }
	}
	model.addAttribute(ATTR_ESTIMATE_DIRECT_COST_LIST, costsDirect);
	model.addAttribute(ATTR_ESTIMATE_INDIRECT_COST_LIST, costsIndirect);

	// Estimation output list, calculator.
	List<EstimationOutput> estimates = this.estimationOutputService.listDesc(proj, true);
	model.addAttribute(ATTR_ESTIMATE_OUTPUT_LIST, estimates);
    }

    /**
     * Get an instance from the Spring context.
     * 
     * @param proj2
     * @param model2
     * @return
     */
    public static RunnableModelerEstimate getCtxInstance(Project p, ProjectAux pA, Model m) {
	BeanHelper beanHelper = new BeanHelper();
	RunnableModelerEstimate modeler = (RunnableModelerEstimate) beanHelper
		.getBean("runnableModelerEstimate");
	modeler.proj = p;
	modeler.projectAux = pA;
	modeler.model = m;
	return modeler;
    }
}