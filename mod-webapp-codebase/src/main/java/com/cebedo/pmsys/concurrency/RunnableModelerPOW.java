package com.cebedo.pmsys.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.cebedo.pmsys.base.IRunnableModeler;
import com.cebedo.pmsys.controller.ProjectController;
import com.cebedo.pmsys.enums.HTMLGanttElement;
import com.cebedo.pmsys.enums.StatusTask;
import com.cebedo.pmsys.enums.TypeCalendarEvent;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.HighchartsDataPoint;
import com.cebedo.pmsys.service.ProjectService;
import com.google.gson.Gson;

@Component
public class RunnableModelerPOW implements IRunnableModeler {

    public static final String ATTR_GANTT_JSON = "ganttJSON";
    public static final String ATTR_TIMELINE_TASK_STATUS_MAP = "taskStatusMap";
    public static final String ATTR_CALENDAR_EVENT_TYPES_LIST = "calendarEventTypes";
    public static final String ATTR_GANTT_TYPE_LIST = "ganttElemTypeList";

    private Project proj;
    private Model model;
    private boolean alive = false;

    private static ApplicationContext ctx;
    private static RunnableModelerPOW MODELER;

    private ProjectService projectService;

    @Autowired(required = true)
    @Qualifier(value = "projectService")
    public void setProjectService(ProjectService s) {
	this.projectService = s;
    }

    /**
     * Set attributes before forwarding back to JSP.
     * 
     * @param proj
     * @param model
     */
    private void setAttributesProgramOfWorks() {

	// Task status selector.
	model.addAttribute(ProjectController.ATTR_TASK_STATUS_LIST, StatusTask.class.getEnumConstants());

	// Gantt JSON to be used by the chart in timeline.
	// Get calendar JSON.
	model.addAttribute(ATTR_GANTT_JSON, this.projectService.getGanttJSON(proj, true));
	model.addAttribute(ProjectController.ATTR_CALENDAR_JSON,
		this.projectService.getCalendarJSON(proj, true));

	// Timeline tasks status and count map.
	// Summary map found in timeline tab.
	Map<StatusTask, Integer> taskStatusMap = this.projectService.getTaskStatusCountMap(proj, true);
	model.addAttribute(ATTR_TIMELINE_TASK_STATUS_MAP, taskStatusMap);
	model.addAttribute(ATTR_CALENDAR_EVENT_TYPES_LIST, TypeCalendarEvent.class.getEnumConstants());
	model.addAttribute(ATTR_GANTT_TYPE_LIST, HTMLGanttElement.class.getEnumConstants());

	// Construct Highcharts data series.
	List<HighchartsDataPoint> dataSeries = new ArrayList<HighchartsDataPoint>();
	int taskCount = 0;
	for (StatusTask status : taskStatusMap.keySet()) {
	    Integer count = taskStatusMap.get(status);
	    taskCount += count;
	    HighchartsDataPoint point = new HighchartsDataPoint(status.label(),
		    NumberUtils.toDouble(count.toString()));
	    dataSeries.add(point);
	}
	model.addAttribute(ProjectController.ATTR_DATA_SERIES_PIE_TASKS,
		taskCount == 0 ? "[]" : new Gson().toJson(dataSeries, ArrayList.class));
    }

    /**
     * Get an instance from the Spring context.
     * 
     * @param proj2
     * @param model2
     * @return
     */
    public static RunnableModelerPOW getCtxInstance(Project p, Model m) {
	RunnableModelerPOW modeler = null;
	try {
	    modeler = (RunnableModelerPOW) MODELER.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
	modeler.proj = p;
	modeler.model = m;
	return modeler;
    }

    @Override
    public void run() {
	alive = true;
	setAttributesProgramOfWorks();
	alive = false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	ctx = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	MODELER = (RunnableModelerPOW) ctx.getBean("runnableModelerPOW");
    }

    @Override
    public boolean isAlive() {
	return alive;
    }

}
