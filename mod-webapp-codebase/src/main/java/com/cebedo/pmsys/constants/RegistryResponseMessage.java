package com.cebedo.pmsys.constants;

public class RegistryResponseMessage {

    // Project.
    public static final String ERROR_PROJECT_PAYROLL_NO_STAFF_CHECK = "Please <b>check</b> at least <b>one staff</b> in the <b>checklist</b>.";

    public static final String ERROR_PROJECT_PULLOUT_NO_PROJECT_STAFF = "You <b>cannot</b> pull-out materials if <b>no staff is assigned</b> to this project.";

    public static final String ERROR_PROJECT_PAYROLL_NO_STAFF = "You <b>cannot</b> create a payroll <b>without staff</b> assigned to this project. <b>Assign staff members first</b>.";

    public static final String ERROR_PROJECT_STAFF_MASS_UPLOAD_GENERIC = "There was a problem with the <b>list of staff</b> you provided. Please <b>review</b> the list and <b>try again</b>.";

    // Auth.
    public static final String ERROR_AUTH_USERNAME_INVALID_PATTERN = "A <b>username</b> must contain the following:<br/>1.) Lowercase letters from <b>a-z</b>.<br/>2.) Numbers <b>0-9</b> or symbols <b>underscore</b> or <b>hyphen</b>.<br/>3.) Length at least <b>4 characters</b> and maximum length of 32.";

    public static final String ERROR_AUTH_PASSWORD_INVALID_PATTERN = "Password must be between <b>8 and 16 digits long</b> and includes <b>at least one numeric digit</b>.";

    public static final String ERROR_AUTH_USERNAME_NOT_AVAILABLE = "The <b>username</b> you provided is <b>no longer available</b>. Please pick a different one.";

    public static final String ERROR_AUTH_LOGIN_GENERIC = "Login failed";

    public static final String SUCCESS_AUTH_LOGIN_GENERIC = "You have successfully updated your Company settings. Please login again.";

    // Commons.
    public static final String ERROR_COMMON_CONVERT_XLSX = "Please <b>convert</b> the <b>Excel *.xlsx</b> file to <b>Excel *.xls</b>.";

    public static final String ERROR_COMMON_NOT_EQUAL_STRINGS = "<b>%s and %s</b> must <b>not be the same</b>.";

    public static final String ERROR_COMMON_EQUAL_STRINGS = "<b>%s and %s</b> must be <b>the same</b>.";

    public static final String ERROR_COMMON_INVALID_PROPERTY = "Please provide a <b>valid %s</b>.";

    public static final String ERROR_COMMON_X_DATE_BEFORE_Y_DATE = "The <b>%s</b> must be <b>before the %s</b>.";

    public static final String ERROR_COMMON_X_GT_Y_VALUE = "<b>%s</b> must be greater than or equal to <b>%s</b>.";

    public static final String ERROR_COMMON_GENERIC = "There was an <b>error</b> on your previous request. Please try again.";

    public static final String ERROR_COMMON_400 = "Please <b>make sure</b> that you have entered <b>all needed information</b> and <b>all information is valid</b>.";

    public static final String ERROR_COMMON_MISSING_REQUIRED = "Please <b>do not</b> provide <b>blank values</b> to required fields, enter <b>zero</b> instead. Thank you.";

    public static final String ERROR_COMMON_FILE_CORRUPT_INVALID = "Your file may be <b>corrupted, invalid</b> or may contain <b>negative numbers</b>.";

    public static final String ERROR_COMMON_MAX_LENGTH = "Maximum length of <b>%s</b> is <b>%s</b> characters.";

    public static final String ERROR_COMMON_ZERO_OR_POSITIVE = "<b>%s</b> must be <b>zero or a positive number</b>.";

    public static final String ERROR_COMMON_POSITIVE = "<b>%s</b> must be <b>a positive number</b>.";

}
