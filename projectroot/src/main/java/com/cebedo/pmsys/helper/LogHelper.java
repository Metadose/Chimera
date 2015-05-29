package com.cebedo.pmsys.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

public class LogHelper {

    private FileHelper fileHelper = new FileHelper();

    public String generateLogMessage(String message) {
	return generateLogMessage(null, null, null, null, null, message);
    }

    public String generateLogUnauthorizedMessage(AuthenticationToken auth,
	    AuditAction action, String objectName, long id, String name) {
	return generateLogUnauthorizedMessage(auth, action.label()
		.toLowerCase(), objectName, id, name);
    }

    public String generateLogUnauthorizedMessage(AuthenticationToken auth,
	    String action, String objectName, long id, String name) {
	action = action.toLowerCase();
	objectName = objectName.toLowerCase();
	String message = "Not authorized to " + action + " " + objectName
		+ ": " + id + " = " + name;
	return generateLogMessage(auth, message);
    }

    public String constructLogText(AuditAction action, String objName,
	    String name) {
	return action.label() + " " + objName + ": " + name;
    }

    /**
     * Generate a log message using the auth.
     * 
     * @param auth
     * @param message
     * @return
     */
    public String generateLogMessage(AuthenticationToken auth, String message) {
	if (auth == null) {
	    return generateLogMessage(message);
	}
	// IP address.
	String ip = "<td>" + auth.getIpAddress() + "</td>\n";

	// Company.
	Company company = auth.getCompany();
	String companyStr = company == null ? "<td></td>\n" : "<td>"
		+ company.getId() + " = " + company.getName() + "</td>\n";

	// User.
	SystemUser user = auth.getUser();
	String userStr = user == null ? "<td></td>\n" : "<td>" + user.getId()
		+ " = " + user.getUsername() + "</td>\n";

	// Staff.
	Staff staff = auth.getStaff();
	String staffStr = staff == null ? "<td></td>\n" : "<td>"
		+ staff.getId() + " = " + staff.getFullName() + "</td>\n";

	// Authorities.
	String authorityStr = "";
	Collection<GrantedAuthority> auths = auth.getAuthorities();
	if (auths == null) {
	    authorityStr = "<td></td>\n";
	} else {
	    authorityStr = "<td>";
	    for (GrantedAuthority authority : auths) {
		authorityStr += authority.getAuthority() + "<br/>\n";
	    }
	    authorityStr += "</td>\n";
	}

	return ip + userStr + staffStr + companyStr + authorityStr + "<td>"
		+ message + "</td>";
    }

    public String generateLogMessage(String ipAddr, Company company,
	    SystemUser user, Staff staff, Collection<GrantedAuthority> auths,
	    String message) {
	// IP address.
	String ip = "<td>" + (ipAddr == null ? "" : ipAddr) + "</td>\n";

	// Company.
	String companyStr = company == null ? "<td></td>\n" : "<td>"
		+ company.getId() + " = " + company.getName() + "</td>\n";

	// User.
	String userStr = user == null ? "<td></td>\n" : "<td>" + user.getId()
		+ " = " + user.getUsername() + "</td>\n";

	// Staff.
	String staffStr = staff == null ? "<td></td>\n" : "<td>"
		+ staff.getId() + " = " + staff.getFullName() + "</td>\n";

	// Authorities.
	String authorityStr = "";
	if (auths == null) {
	    authorityStr = "<td></td>\n";
	} else {
	    authorityStr = "<td>";
	    for (GrantedAuthority authority : auths) {
		authorityStr += authority.getAuthority() + "<br/>\n";
	    }
	    authorityStr += "</td>\n";
	}
	return ip + userStr + staffStr + companyStr + authorityStr + "<td>"
		+ message + "</td>";
    }

    public boolean isSpecialView(String logPath, String sysHome, String module) {
	return logPath.replace("//", "/")
		.replace(getLogRootDir(sysHome).replace("//", "/"), "")
		.split("/")[0].equals(module);
    }

    public String getLogContents(String logPath) {
	try {
	    return this.fileHelper.getFileContents(logPath);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return "";
	}
    }

    public String getLogRootDir(String rootPath) {
	return rootPath + "/system/logs/";
    }

    /**
     * Generate a tree json data for the logs. TODO Use GSON to generate JSON
     * like "String json = new Gson().toJson(logBeanInstance, LogBean.class);"
     * 
     * @return
     */
    public String getLogTree(String rootPath) {
	String root = getLogRootDir(rootPath);
	File rootDir = new File(root);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	String jsonData = "{'data' : [";
	String rootDirID = rootDir.getAbsolutePath().replace("\\", "/");
	jsonData += "{ 'id' : '"
		+ rootDirID
		+ "', 'parent' : '#', 'text' : 'Logs', 'icon': 'fa fa-angle-right' },";

	for (File file : rootDir.listFiles()) {

	    if (file.isDirectory()) {
		String id = file.getAbsolutePath().replace("\\", "/");
		String parentID = file.getParentFile().getAbsolutePath()
			.replace("\\", "/");

		jsonData += "{ 'id' : '" + id + "', 'parent' : '" + parentID
			+ "', 'text' : '" + file.getName()
			+ "', 'icon': 'fa fa-folder-open-o' },";

		for (File logFile : file.listFiles()) {

		    String logFileID = logFile.getAbsolutePath().replace("\\",
			    "/");
		    String logFileParentID = logFile.getParentFile()
			    .getAbsolutePath().replace("\\", "/");

		    jsonData += "{ 'id' : '" + logFileID + "', 'parent' : '"
			    + logFileParentID + "', 'text' : '"
			    + logFile.getName() + " ("
			    + (logFile.length() / 1024) + "KB) "
			    + sdf.format(logFile.lastModified())
			    + "', 'icon': 'fa fa-file-o' },";
		}
	    } else {
		String id = file.getAbsolutePath().replace("\\", "/");
		String parentID = file.getParentFile().getAbsolutePath()
			.replace("\\", "/");

		jsonData += "{ 'id' : '" + id + "', 'parent' : '" + parentID
			+ "', 'text' : '" + file.getName()
			+ "', 'icon': 'fa fa-file-o' },";
	    }
	}
	jsonData += "]}";
	jsonData = jsonData.replace("},]", "}]");
	return jsonData;
    }

}
