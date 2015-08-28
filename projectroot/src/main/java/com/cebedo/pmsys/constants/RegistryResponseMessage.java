package com.cebedo.pmsys.constants;

public class RegistryResponseMessage {

    // Project.
    public static final String ERROR_PROJECT_EMPTY_EXTRA_INFO = "You <b>cannot</b> set an <b>empty information</b>.";

    public static final String ERROR_PROJECT_ESTIMATION_EMPTY_FILE = "You <b>cannot</b> upload an <b>empty file</b>.";

    public static final String ERROR_PROJECT_PAYROLL_NO_STAFF_CHECK = "Please <b>check</b> at least <b>one staff</b> in the <b>checklist</b>.";

    public static final String ERROR_PROJECT_PULLOUT_NO_PROJECT_STAFF = "You <b>cannot</b> pull-out materials if <b>no staff is assigned</b> to this project.";

    public static final String ERROR_PROJECT_MATERIAL_MORE_THAN_ONE_UNIT = "You <b>cannot</b> choose <b>more than one unit of measure</b> for a material.";

    public static final String ERROR_PROJECT_PULLOUT_EXCEED = "You <b>cannot</b> pull-out <b>more than the available</b> material.";

    public static final String ERROR_PROJECT_PULLOUT_DATE_BEFORE_DELIVERY = "<b>Pull-out date</b> must be <b>after the delivery date</b>.";

    public static final String ERROR_PROJECT_PAYROLL_NO_STAFF = "You <b>cannot</b> create a payroll <b>without staff</b> assigned to this project. <b>Assign staff members first</b>.";

    public static final String ERROR_PROJECT_STAFF_ATTENDANCE_NEGATIVE_WAGE = "<b>Wage</b> must be <b>a positive number</b>.";

    public static final String ERROR_PROJECT_STAFF_MASS_UPLOAD_GENERIC = "There was a problem with the <b>list of staff</b> you provided. Please <b>review</b> the list and <b>try again</b>.";

    // Auth.
    public static final String ERROR_AUTH_USERNAME_INVALID_PATTERN = "A <b>username</b> must contain the following:<br/>1.) Lowercase letters from <b>a-z</b>.<br/>2.) Numbers <b>0-9</b> or symbols <b>underscore</b> or <b>hyphen</b>.<br/>3.) Length at least <b>4 characters</b> and maximum length of 32.";

    public static final String ERROR_AUTH_PASSWORD_INVALID_PATTERN = "Password must be between <b>8 and 16 digits long</b> and includes <b>at least one numeric digit</b>.";

    public static final String ERROR_AUTH_PASSWORDS_NOT_EQUAL = "The <b>passwords</b> you entered were <b>not the same</b>.";

    public static final String ERROR_AUTH_USERNAME_NOT_AVAILABLE = "The <b>username</b> you provided is <b>no longer available</b>. Please pick a different one.";

    // Commons.
    public static final String ERROR_COMMON_INVALID_QUANTITY = "Quantity must be <b>greater than zero</b>.";

    public static final String ERROR_COMMON_INVALID_DATE_TIME = "Please provide a <b>valid date and time</b>.";

    public static final String ERROR_COMMON_INVALID_DATE = "Please provide a <b>valid date</b>.";

    public static final String ERROR_COMMON_INVALID_NAME = "Please provide a <b>valid name</b>.";

    public static final String ERROR_COMMON_START_DATE_GT_END_DATE = "The <b>start date</b> must be <b>before the end date</b>.";

    public static final String ERROR_COMMON_GENERIC = "There was an <b>error</b> on your previous request. Please try again.";

    public static final String ERROR_COMMON_EMPTY_FILE = "You <b>cannot</b> upload an <b>empty file</b>.";

    public static final String ERROR_COMMON_FILE_CORRUPT_INVALID = "Your file may be <b>corrupted or invalid</b>.";

    // Company.
    public static final String ERROR_COMPANY_EXPIRE_DATE_LT_NOW = "The <b>expiration date</b> must be <b>after the current date</b>.";

    public static final String ERROR_CONFIG_EMPTY_NAME = "You <b>cannot</b> create a configuration with an <b>empty name</b>.";

    public static final String ERROR_TASK_EMPTY_TITLE = "You <b>cannot</b> create a task with an <b>empty title</b>.";

    public static final String ERROR_TASK_DURATION_LTE_ZERO = "Task <b>duration</b> must be <b>greater than zero</b>.";
}
