package com.cebedo.pmsys.constants;

public class RegistryURL {

    public static final String ADD_ATTENDACE = "/add/attendance";

    public static final String MASS_ADD_ATTENDACE = "/mass/add/attendance";

    public static final String MASS_UPLOAD_AND_ASSIGN_STAFF = "/mass/upload-and-assign/staff";

    public static final String MASS_UPLOAD_AND_ASSIGN_TASK = "/mass/upload-and-assign/task";

    public static final String CLEAR_ACTUAL_COMPLETION_DATE = "/clear/actual-completion-date";

    public static final String CREATE_TASK = "/create/task";

    public static final String CREATE_STAFF = "/create/staff";

    public static final String CREATE_EXPENSE = "/create/expense";

    public static final String CREATE_ESTIMATE_COST = "/create/cost";

    public static final String EDIT_ESTIMATE_COST = "/edit/cost/{cost}-end";

    public static final String DELETE_ESTIMATE_COST = "/delete/cost/{cost}-end";

    public static final String DELETE_EXPENSE = "/delete/expense/{expense}-end";

    public static final String EXPORT_XLS_PAYROLL = "/export-xls/payroll/{payroll}-end";

    public static final String EXPORT_XLS_PAYROLL_ALL = "/export-xls/payroll/all";

    public static final String EDIT_EXPENSE = "/edit/expense/{expense}-end";

    public static final String EDIT_STAFF = "/edit/staff/{staff}";

    public static final String REDIRECT_EDIT_SYSTEM_USER = "redirect:/systemuser/edit/%s";

    public static final String REDIRECT_LIST_SYSTEM_USER = "redirect:/systemuser/list";

    public static final String REDIRECT_EDIT_SYS_CONFIG = "redirect:/config/edit/%s";

    public static final String REDIRECT_LIST_SYS_CONFIG = "redirect:/config/list";

    public static final String REDIRECT_EDIT_COMPANY = "redirect:/company/edit/%s";

    public static final String REDIRECT_LIST_COMPANY = "redirect:/company/list";

    public static final String REDIRECT_EDIT_PROJECT_STAFF = "redirect:/project/edit/staff/%s";

    public static final String REDIRECT_EDIT_PROJECT_TASK = "redirect:/project/edit/task/%s";

    public static final String EDIT_TASK = "/edit/task/{task}";

    public static final String EDIT_ATTENDANCE_RANGE = "/edit/attendance/range";

    public static final String VIEW_ESTIMATION_RESULTS = "/view/estimation/{estimationoutput}-end";

    public static final String DELETE_ESTIMATION_RESULTS = "/delete/estimation/{estimationoutput}-end";

    public static final String DELETE_TASK = "/delete/task/{task}";

    public static final String MARK_TASK = "/mark/task/";

    public static final String DELETE_TASK_ALL = "/delete/task/all";

    public static final String REDIRECT_EDIT_PROJECT = "redirect:/project/edit/%s";

    public static final String REDIRECT_EDIT_STAFF = "redirect:/staff/edit/%s";

    public static final String REDIRECT_DASHBOARD = "redirect:/dashboard/";

    public static final String REDIRECT_LIST_PROJECT = "redirect:/project/list";

    public static final String ASSIGN_TASK_STAFF = "/assign/task/staff";

    public static final String UNASSIGN_TASK_STAFF = "/unassign/task/staff/{staff}";

    public static final String UNASSIGN_ALL_TASK_STAFF = "/unassign/task/staff/all";

}
