package com.cebedo.pmsys.constants;

public class RegistryResponseMessage {

    public static final String ERROR_EMPTY_EXTRA_INFO = "You <b>cannot</b> set an <b>empty information</b>.";

    public static final String ERROR_USERNAME_INVALID_PATTERN = "A <b>Username</b> must contain the following:<br/>1.) Lowercase letters from <b>a-z</b>.<br/>2.) Numbers <b>0-9</b> or symbols <b>underscore</b> or <b>hyphen</b>.<br/>3.) Length at least <b>4 characters</b> and maximum length of 32.";

    public static final String ERROR_PASSWORD_INVALID_PATTERN = "Password must be between <b>8 and 16 digits long</b> and includes <b>at least one numeric digit</b>.";

    public static final String ERROR_PASSWORDS_NOT_EQUAL = "The <b>passwords</b> you entered were <b>not the same</b>.";

    public static final String ERROR_USERNAME_NOT_AVAILABLE = "The <b>username</b> you provided is <b>no longer available</b>. Please pick a different one.";

    public static final String ERROR_ADD_MATERIAL_MORE_THAN_ONE_UNIT = "You <b>cannot</b> choose <b>more than one unit of measure</b> for a material.";

    public static final String ERROR_PULLOUT_EXCEED = "You <b>cannot</b> pull-out <b>more than the available</b> material.";

    public static final String ERROR_INVALID_QUANTITY = "Quantity must be <b>greater than zero</b>.";

    public static final String ERROR_INVALID_DATE = "Please provide a <b>valid date and time</b>.";

    public static final String ERROR_PAYROLL_NO_STAFF = "You <b>cannot</b> create a payroll <b>without staff</b> assigned to this project. <b>Assign staff members first</b>.";

    public static final String ERROR_START_DATE_GT_END_DATE = "The <b>start date</b> must be <b>before the end date</b>.";

    public static final String ERROR_GENERIC = "There was an <b>error</b> on your previous request. Please try again.";

}
