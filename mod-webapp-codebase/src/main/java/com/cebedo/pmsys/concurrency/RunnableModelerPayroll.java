package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.cebedo.pmsys.bean.PayrollResultComputation;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.service.ProjectPayrollService;
import com.google.gson.Gson;

@Component
public class RunnableModelerPayroll
	implements Runnable, InitializingBean, ApplicationContextAware, Cloneable {

    public static final String ATTR_PAYROLL_LIST = "payrollList";
    public static final String ATTR_DATA_SERIES_PAYROLL = "dataSeriesPayroll";
    public static final String ATTR_DATA_SERIES_PAYROLL_CUMULATIVE = "dataSeriesPayrollCumulative";

    public static final String[] ATTRS_LIST = { ATTR_PAYROLL_LIST, ATTR_DATA_SERIES_PAYROLL,
	    ATTR_DATA_SERIES_PAYROLL_CUMULATIVE };

    private Project proj;
    private Model model;
    private List<HighchartsDataPoint> dataSeries;
    private List<HighchartsDataPoint> dataSeriesCumulative;

    private static ApplicationContext ctx;
    private static RunnableModelerPayroll MODELER;

    private ProjectPayrollService projectPayrollService;

    @Autowired(required = true)
    @Qualifier(value = "projectPayrollService")
    public void setProjectPayrollService(ProjectPayrollService projectPayrollService) {
	this.projectPayrollService = projectPayrollService;
    }

    /**
     * Set payroll attributes.
     * 
     * @param proj
     * @param model
     * @param projectCumulative
     * @param projectAccumulation
     */
    private void setAttributesPayroll() {
	// Get all payrolls.
	// Add to model.
	List<ProjectPayroll> payrollList = this.projectPayrollService.listAsc(proj, true);
	model.addAttribute(ATTR_PAYROLL_LIST, payrollList);

	// Graph data.
	double accumulation = 0;
	for (ProjectPayroll payroll : payrollList) {

	    // If it doesn't have a payroll JSON,
	    // then it has not been computed.
	    String payrollJSON = payroll.getPayrollJSON();
	    if (payrollJSON != null && !payrollJSON.isEmpty()) {
		String name = "(Payroll) " + payroll.getStartEndDisplay();
		PayrollResultComputation result = payroll.getPayrollComputationResult();
		double yValue = result.getOverallTotalOfStaff();

		HighchartsDataPoint point = new HighchartsDataPoint(name, result.getEndDate().getTime(),
			yValue);
		dataSeries.add(point);

		// Cumulative.
		accumulation += yValue;
		// HighchartsDataPoint pointCumulative = new
		// HighchartsDataPoint(name,
		// result.getEndDate().getTime(), accumulation,
		// HTMLCSSDetails.backgroundColorOf(payroll.getStatus().css()));
		HighchartsDataPoint pointCumulative = new HighchartsDataPoint(name,
			result.getEndDate().getTime(), accumulation);
		dataSeriesCumulative.add(pointCumulative);
	    }
	}

	model.addAttribute(ATTR_DATA_SERIES_PAYROLL, new Gson().toJson(dataSeries, ArrayList.class));
	model.addAttribute(ATTR_DATA_SERIES_PAYROLL_CUMULATIVE,
		new Gson().toJson(dataSeriesCumulative, ArrayList.class));
    }

    /**
     * Get an instance from the Spring context.
     * 
     * @param proj2
     * @param model2
     * @return
     */
    public static RunnableModelerPayroll getCtxInstance(Project p, Model m, List<HighchartsDataPoint> dS,
	    List<HighchartsDataPoint> dSC) {
	RunnableModelerPayroll modeler = null;
	try {
	    modeler = (RunnableModelerPayroll) MODELER.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
	modeler.proj = p;
	modeler.model = m;
	modeler.dataSeries = dS;
	modeler.dataSeriesCumulative = dSC;
	return modeler;
    }

    @Override
    public void run() {
	setAttributesPayroll();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	ctx = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	MODELER = (RunnableModelerPayroll) ctx.getBean("runnableModelerPayroll");
    }

}
