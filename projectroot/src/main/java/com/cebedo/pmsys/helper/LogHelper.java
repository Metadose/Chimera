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

    public String logMessage(String message) {
	return logMessage(null, null, null, null, null, message);
    }

    /**
     * Not authorized to do action on object.
     * 
     * @param auth
     * @param action
     * @param objectName
     * @param id
     * @param name
     * @return
     */
    public String logUnauthorized(AuthenticationToken auth, AuditAction action,
	    String objectName, long id, String name) {
	String actionStr = action.label().toLowerCase();
	String message = "(" + objectName + ") Not authorized to " + actionStr
		+ ": " + id + " = " + name;
	return logMessage(auth, message);
    }

    public String logUnauthorized(AuthenticationToken auth, AuditAction action,
	    String objectName, String id, String name) {
	String actionStr = action.label().toLowerCase();
	String message = "(" + objectName + ") Not authorized to " + actionStr
		+ ": " + id + " = " + name;
	return logMessage(auth, message);
    }

    public String constructTextActionOnObj(AuditAction action, String objName,
	    String name) {
	return "(" + objName + ") " + action.label() + ": " + name;
    }

    /**
     * Sample: (Team under Project) Assign: Excavators under ABC Dorm
     * 
     * @param action
     * @param objName
     * @param name
     * @param objNameAssoc
     * @param nameAssoc
     * @return
     */
    public String constructTextActionOnObjWithAssoc(AuditAction action,
	    String objName, String name, String objNameAssoc, String nameAssoc) {
	// Sample: (Team under Project) Assign: Excavators under ABC Dorm
	return "(" + objNameAssoc + " under " + objName + ") " + action.label()
		+ ": " + objNameAssoc + " under " + name;
    }

    /**
     * Generate a log message using the auth.
     * 
     * @param auth
     * @param message
     * @return
     */
    public String logMessage(AuthenticationToken auth, String message) {
	if (auth == null) {
	    return logMessage(message);
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

    public String logMessage(String ipAddr, Company company, SystemUser user,
	    Staff staff, Collection<GrantedAuthority> auths, String message) {
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

    private String constructTextListAsSuperAdmin(String objectName) {
	return "(" + objectName + ") Listing all as super admin.";
    }

    private String constructTextListFromCompany(String objectName,
	    Company company) {
	return "(" + objectName + ") Listing all from company: "
		+ company.getId() + " = " + company.getName();
    }

    /**
     * Generate log when listing entries from all companies.
     * 
     * @param token
     * @param constructTextListFromCompany
     * @param company
     * @return
     */
    public String logListAsSuperAdmin(AuthenticationToken token, String objName) {
	return logMessage(token, constructTextListAsSuperAdmin(objName));
    }

    /**
     * Generate log when listing entries from a specific company.
     * 
     * @param token
     * @param constructTextListFromCompany
     * @param company
     * @return
     */
    public String logListFromCompany(AuthenticationToken token, String objName,
	    Company company) {
	return logMessage(token, constructTextListFromCompany(objName, company));
    }

    public String logGetObjectProperty(AuthenticationToken auth,
	    String objName, String objProperty, long id, String name) {
	return logMessage(auth, "(" + objName + ") Get " + objProperty + ": "
		+ id + " = " + name);
    }

    public String logGetObject(AuthenticationToken auth, String objName,
	    long id, String name) {
	return logMessage(auth, "(" + objName + ") Get: " + id + " = " + name);
    }

    public String logGetObject(AuthenticationToken auth, String objName,
	    String id, String name) {
	return logMessage(auth, "(" + objName + ") Get: " + id + " = " + name);
    }

    public String logGetObjectWithAllCollections(AuthenticationToken auth,
	    String objName, long id, String name) {
	return logMessage(auth, "(" + objName + ") Get with all collections: "
		+ id + " = " + name);
    }

    public String logListPartialCollectionsAsSuperAdmin(
	    AuthenticationToken token, String objectName, String initializedObj) {
	String text = "(" + objectName + ") Listing all (initiated with "
		+ initializedObj + ") as super admin.";
	return logMessage(token, text);
    }

    public String logListPartialCollectionsFromCompany(
	    AuthenticationToken token, String objectName,
	    String initializedObj, Company company) {
	String text = "(" + objectName + ") Listing all (initiated with "
		+ initializedObj + ") from company: " + company.getId() + " = "
		+ company.getName();
	return logMessage(token, text);
    }

    public String logListWithCollectionsAsSuperAdmin(AuthenticationToken token,
	    String objectName) {
	String text = "(" + objectName
		+ ") Listing with all collections as super admin.";
	return logMessage(token, text);
    }

    public String logListWithCollectionsFromCompany(AuthenticationToken token,
	    String objectName, Company company) {
	String text = "(" + objectName
		+ ") Listing with all collections from company: "
		+ company.getId() + " = " + company.getName();
	return logMessage(token, text);
    }

    public String logUnauthorizedSuperAdminOnly(AuthenticationToken auth,
	    AuditAction action, String objectName) {
	String actionStr = action.label().toLowerCase();
	String message = "(" + objectName + ") Not authorized to " + actionStr
		+ " if not super admin.";
	return logMessage(auth, message);
    }

    /**
     * Sample: (Team under Project) Unassign All: All Under ABC Dorm.
     * 
     * @param action
     * @param objectName
     * @param name
     * @param objectNameAssoc
     * @return
     */
    public String constructTextActionOnObjWithAssoc(AuditAction action,
	    String objectName, String name, String objectNameAssoc) {
	// Sample: (Team under Project) Unassign All: All under ABC Dorm
	return "(" + objectNameAssoc + " under " + objectName + ") "
		+ action.label() + ": All under " + name;
    }
}
