package com.cebedo.pmsys.resolver;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class LoggingExceptionResolver extends SimpleMappingExceptionResolver {

    private Logger logger = Logger.getLogger("webappExceptionLogger");
    private AuthHelper authHelper = new AuthHelper();

    @SuppressWarnings("rawtypes")
    @Override
    protected void logException(Exception ex, HttpServletRequest request) {

	// Log the time.
	String logString = "EXCEPTION LOG START\n";
	AuthenticationToken auth = null;

	try {
	    // Prepare vars.
	    auth = this.authHelper.getAuth();
	    Company company = auth.getCompany() == null ? new Company() : auth.getCompany();
	    SystemUser user = auth.getUser();
	    Staff staff = auth.getStaff();

	    // Identification.
	    String identification = "IP Address: %s\n";
	    identification += "Company ID: %s\n";
	    identification += "Company Name: %s\n";
	    identification += "User ID: %s\n";
	    identification += "User Name: %s\n";
	    identification += "Staff ID: %s\n";
	    identification += "Staff Name: %s\n";
	    identification += "Company Admin: %s\n";
	    identification += "Super Admin: %s\n";
	    identification += "\n";

	    String ipAddr = auth.getIpAddress();
	    long companyID = company.getId();
	    String companyName = company.getName();
	    long userID = user.getId();
	    String userName = user.getUsername();
	    long staffID = staff.getId();
	    String staffName = staff.getFullName();
	    boolean companyAdmin = auth.isCompanyAdmin();
	    boolean superAdmin = auth.isSuperAdmin();

	    logString += String.format(identification, ipAddr, companyID, companyName, userID, userName,
		    staffID, staffName, companyAdmin, superAdmin);
	} catch (Exception e) {
	    ;
	}
	logString += "Message: " + buildLogMessage(ex, request) + "\n";
	logString += "URL: " + request.getRequestURL() + "\n";
	logString += "\n";

	// Headers.
	logString += "HEADERS\n";
	Enumeration<?> enums = request.getHeaderNames();
	while (enums.hasMoreElements()) {
	    String nextElem = enums.nextElement().toString();
	    logString += nextElem + " = " + request.getHeader(nextElem) + "\n";
	}
	logString += "\n";

	// Attributes.
	logString += "ATTRIBUTES\n";
	enums = request.getAttributeNames();
	while (enums.hasMoreElements()) {
	    String nextElem = enums.nextElement().toString();
	    logString += nextElem + " = " + request.getAttribute(nextElem) + "\n";
	}
	logString += "\n";

	// Parameters.
	logString += "PARAMETERS\n";
	Map paramMap = request.getParameterMap();
	for (Object key : paramMap.keySet()) {
	    logString += key + " = " + request.getParameter(String.valueOf(key)) + "\n";
	}
	logString += "\n\n";

	this.logger.error(logString, ex);
    }
}