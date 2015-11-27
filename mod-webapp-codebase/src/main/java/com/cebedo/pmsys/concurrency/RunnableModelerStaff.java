package com.cebedo.pmsys.concurrency;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.cebedo.pmsys.base.IRunnableModeler;
import com.cebedo.pmsys.controller.ProjectController;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.pojo.FormMassUpload;
import com.cebedo.pmsys.pojo.FormStaffAssignment;
import com.cebedo.pmsys.service.StaffService;

@Component
public class RunnableModelerStaff implements IRunnableModeler {

    // Staff.
    public static final String ATTR_STAFF_LIST_AVAILABLE = "availableStaffToAssign";
    public static final String ATTR_STAFF_POSITION = "staffPosition";

    private Project proj;
    private Model model;
    private boolean alive = false;

    private static ApplicationContext ctx;
    private static RunnableModelerStaff MODELER;

    private StaffService staffService;

    @Autowired(required = true)
    @Qualifier(value = "staffService")
    public void setStaffService(StaffService s) {
	this.staffService = s;
    }

    public RunnableModelerStaff() {
	;
    }

    /**
     * Set attributes used by Staff tab.
     * 
     * @param proj
     * @param model
     */
    private void setAttributesStaff() {
	// Get list of staff members.
	Long companyID = proj.getCompany().getId();

	// Used in the "assign staff controls".
	// Get the list of staff not yet assigned in this project.
	// Company staff, minus assigned.
	List<Staff> availableStaffToAssign = this.staffService.listUnassignedInProject(companyID, proj,
		true);

	// Get lists for selectors.
	model.addAttribute(ATTR_STAFF_LIST_AVAILABLE, availableStaffToAssign);
	model.addAttribute(ATTR_STAFF_POSITION, new FormStaffAssignment());
	model.addAttribute(ProjectController.ATTR_MASS_UPLOAD_BEAN, new FormMassUpload(proj));
    }

    /**
     * Get an instance from the Spring context.
     * 
     * @param proj2
     * @param model2
     * @return
     */
    public static RunnableModelerStaff getCtxInstance(Project proj2, Model model2) {
	RunnableModelerStaff modeler = null;
	try {
	    modeler = (RunnableModelerStaff) MODELER.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
	modeler.proj = proj2;
	modeler.model = model2;
	return modeler;
    }

    @Override
    public void run() {
	alive = true;
	setAttributesStaff();
	alive = false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	ctx = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	MODELER = (RunnableModelerStaff) ctx.getBean("runnableModelerStaff");
    }

    @Override
    public boolean isAlive() {
	return alive;
    }
}