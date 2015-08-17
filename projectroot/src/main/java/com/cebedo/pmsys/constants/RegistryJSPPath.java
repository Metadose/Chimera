package com.cebedo.pmsys.constants;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

public class RegistryJSPPath {

    public static final String JSP_EDIT_ESTIMATION_OUTPUT = Project.OBJECT_NAME
	    + "/estimationOutputEdit";

    public static final String JSP_EDIT_DELIVERY = Project.OBJECT_NAME + "/deliveryEdit";

    public static final String JSP_EDIT_PAYROLL = Project.OBJECT_NAME + "/payrollEdit";

    public static final String JSP_EDIT_MATERIAL = Project.OBJECT_NAME + "/materialEdit";

    public static final String JSP_EDIT_STAFF = Staff.OBJECT_NAME + "/staffEdit";

    public static final String JSP_LIST_STAFF = Staff.OBJECT_NAME + "/staffList";

    public static final String JSP_EDIT_MATERIAL_PULLOUT = Project.OBJECT_NAME + "/materialPulloutEdit";

}
