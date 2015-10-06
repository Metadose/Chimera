package com.cebedo.pmsys.constants;

import com.cebedo.pmsys.model.Company;
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

    public static final String JSP_EDIT_COST = Project.OBJECT_NAME + "/costEdit";

    public static final String JSP_EDIT_EXPENSE = Project.OBJECT_NAME + "/expenseEdit";

    public static final String JSP_EDIT_EQUIPMENT_EXPENSE = Project.OBJECT_NAME
	    + "/equipmentExpenseEdit";

    public static final String JSP_EDIT_STAFF = Staff.OBJECT_NAME + "/staffEdit";

    public static final String JSP_LIST_STAFF = Staff.OBJECT_NAME + "/staffList";

    public static final String JSP_EDIT_MATERIAL_PULLOUT = Project.OBJECT_NAME + "/materialPulloutEdit";

    public static final String JSP_LIST_SYSTEM_USER = SystemUser.OBJECT_NAME + "/systemUserList";

    public static final String JSP_EDIT_SYSTEM_USER = SystemUser.OBJECT_NAME + "/systemUserEdit";

    public static final String JSP_LIST_SYS_CONFIG = SystemConfiguration.OBJECT_NAME
	    + "/systemConfigurationList";

    public static final String JSP_EDIT_SYS_CONFIG = SystemConfiguration.OBJECT_NAME
	    + "/systemConfigurationEdit";

    public static final String JSP_LIST_PROJECT = Project.OBJECT_NAME + "/projectList";

    public static final String JSP_EDIT_PROJECT = Project.OBJECT_NAME + "/projectEdit";

    public static final String JSP_LOGS_PROJECT = Project.OBJECT_NAME + "/projectLogs";

    public static final String JSP_LOGS_COMPANY = Company.OBJECT_NAME + "/companyLogs";

    public static final String JSP_EDIT_PROJECT_FIELD = Project.OBJECT_NAME + "/assignedFieldEdit";

    public static final String JSP_LIST_COMPANY = Company.OBJECT_NAME + "/companyList";

    public static final String JSP_EDIT_COMPANY = Company.OBJECT_NAME + "/companyEdit";

    public static final String JSP_EDIT_TASK = Project.OBJECT_NAME + "/taskEdit";

}
