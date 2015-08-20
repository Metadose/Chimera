package com.cebedo.pmsys.constants;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;

public class RegistryJSPPath {

    public static final String JSP_EDIT_ESTIMATION_OUTPUT = Project.OBJECT_NAME
	    + "/estimationOutputEdit";

    public static final String JSP_EDIT_DELIVERY = Project.OBJECT_NAME + "/deliveryEdit";

    public static final String JSP_EDIT_PAYROLL = Project.OBJECT_NAME + "/payrollEdit";

    public static final String JSP_EDIT_MATERIAL = Project.OBJECT_NAME + "/materialEdit";

    public static final String JSP_EDIT_STAFF = Staff.OBJECT_NAME + "/staffEdit";

    public static final String JSP_LIST_STAFF = Staff.OBJECT_NAME + "/staffList";

    public static final String JSP_EDIT_MATERIAL_PULLOUT = Project.OBJECT_NAME + "/materialPulloutEdit";

    public static final String JSP_LIST_SYSTEM_USER = SystemUser.OBJECT_NAME + "/systemUserList";

    public static final String JSP_EDIT_SYSTEM_USER = SystemUser.OBJECT_NAME + "/systemUserEdit";

    public static final String JSP_LIST_SYS_CONFIG = SystemConfiguration.OBJECT_NAME
	    + "/systemConfigurationList";

    public static final String JSP_EDIT_SYS_CONFIG = SystemConfiguration.OBJECT_NAME
	    + "/systemConfigurationEdit";

}