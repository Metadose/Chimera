package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
import com.cebedo.pmsys.enums.TypeSystemModule;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.service.EstimateCostService;
import com.cebedo.pmsys.service.EstimationOutputService;
import com.google.gson.Gson;

@Component
public class CallableModeler implements Callable<Model> {

    public static final String ATTR_DATA_SERIES_PIE_COSTS_ESTIMATED = "dataSeriesCostsEstimated";
    public static final String ATTR_DATA_SERIES_PIE_COSTS_ACTUAL = "dataSeriesCostsActual";
    public static final String ATTR_ESTIMATE_DIRECT_COST_LIST = "directCostList";
    public static final String ATTR_ESTIMATE_INDIRECT_COST_LIST = "indirectCostList";
    public static final String ATTR_ESTIMATE_OUTPUT_LIST = "estimationOutputList";

    private TypeSystemModule systemModule;
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

    public CallableModeler() {
	;
    }

    public void set(TypeSystemModule type, Project p, ProjectAux pA, Model m) {
	this.systemModule = type;
	this.proj = p;
	this.projectAux = pA;
	this.model = m;
    }

    @Override
    public Model call() throws Exception {
	if (this.systemModule == TypeSystemModule.ESTIMATE) {
	    setAttributesEstimate();
	}
	return model;
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

}