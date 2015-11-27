package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.cebedo.pmsys.controller.ProjectController;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.helper.BeanHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.service.DeliveryService;
import com.cebedo.pmsys.service.MaterialService;
import com.cebedo.pmsys.service.PullOutService;
import com.cebedo.pmsys.utils.DateUtils;
import com.google.gson.Gson;

@Component
public class RunnableModelerInventory implements Runnable {

    public static final String ATTR_DELIVERY_LIST = "deliveryList";
    public static final String ATTR_DATA_SERIES_INVENTORY = "dataSeriesInventory";
    public static final String ATTR_DATA_SERIES_INVENTORY_CUMULATIVE = "dataSeriesInventoryCumulative";
    public static final String ATTR_PULL_OUT_LIST = "pullOutList";

    public static final String[] ATTRS_LIST = { ATTR_DELIVERY_LIST, ATTR_DATA_SERIES_INVENTORY,
	    ATTR_DATA_SERIES_INVENTORY_CUMULATIVE, ProjectController.ATTR_MATERIAL_LIST,
	    ATTR_PULL_OUT_LIST };

    private Project proj;
    private Model model;
    private List<HighchartsDataPoint> dataSeries;
    private List<HighchartsDataPoint> dataSeriesCumulative;

    private DeliveryService deliveryService;
    private PullOutService pullOutService;
    private MaterialService materialService;

    @Autowired(required = true)
    @Qualifier(value = "materialService")
    public void setMaterialService(MaterialService materialService) {
	this.materialService = materialService;
    }

    @Autowired(required = true)
    @Qualifier(value = "pullOutService")
    public void setPullOutService(PullOutService pullOutService) {
	this.pullOutService = pullOutService;
    }

    @Autowired(required = true)
    @Qualifier(value = "deliveryService")
    public void setDeliveryService(DeliveryService deliveryService) {
	this.deliveryService = deliveryService;
    }

    /**
     * Set inventory attributes.
     * 
     * @param proj
     * @param model
     * @param projectAccumulation
     * @param projectCumulative
     */
    private void setAttributesInventory() {
	// Get all deliveries.
	// Get all pull-outs.
	// Get inventory.
	// Then add to model.
	List<Delivery> deliveryList = this.deliveryService.listAsc(proj, true);
	model.addAttribute(ATTR_DELIVERY_LIST, deliveryList);

	// Graph.
	double accumulation = 0;
	for (Delivery delivery : deliveryList) {
	    double yValue = delivery.getGrandTotalOfMaterials();
	    Date datetime = delivery.getDatetime();
	    String name = String.format("(Inventory) %s<br/>%s",
		    DateUtils.formatDate(datetime, DateUtils.PATTERN_DATE_TIME), delivery.getName());

	    HighchartsDataPoint point = new HighchartsDataPoint(name, datetime.getTime(), yValue);
	    dataSeries.add(point);

	    // Cumulative.
	    accumulation += yValue;
	    HighchartsDataPoint pointCumulative = new HighchartsDataPoint(name, datetime.getTime(),
		    accumulation);
	    dataSeriesCumulative.add(pointCumulative);
	}
	model.addAttribute(ATTR_DATA_SERIES_INVENTORY, new Gson().toJson(dataSeries, ArrayList.class));
	model.addAttribute(ATTR_DATA_SERIES_INVENTORY_CUMULATIVE,
		new Gson().toJson(dataSeriesCumulative, ArrayList.class));

	// Get all materials.
	// Add to model.
	List<Material> materialList = this.materialService.listDesc(proj, true);
	model.addAttribute(ProjectController.ATTR_MATERIAL_LIST, materialList);

	// Get all pull-outs.
	// Add to model.
	List<PullOut> pullOutList = this.pullOutService.listDesc(proj, true);
	model.addAttribute(ATTR_PULL_OUT_LIST, pullOutList);
    }

    /**
     * Get an instance from the Spring context.
     * 
     * @param proj2
     * @param model2
     * @return
     */
    public static RunnableModelerInventory getCtxInstance(Project p, Model m,
	    List<HighchartsDataPoint> dS, List<HighchartsDataPoint> dSC) {
	BeanHelper beanHelper = new BeanHelper();
	RunnableModelerInventory modeler = (RunnableModelerInventory) beanHelper
		.getBean("runnableModelerInventory");
	modeler.proj = p;
	modeler.model = m;
	modeler.dataSeries = dS;
	modeler.dataSeriesCumulative = dSC;
	return modeler;
    }

    @Override
    public void run() {
	setAttributesInventory();
    }

}
