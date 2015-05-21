package com.cebedo.pmsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.service.SystemConfigurationService;

@Controller
@RequestMapping(LogController.MAPPING_LOG_CONTROLLER)
public class LogController {

    public static final String ATTR_LIST = "logList";
    public static final String ATTR_CONTENT = "logContent";
    public static final String ATTR_PATH = "logPath";
    public static final String ATTR_IS_ERROR = "isError";
    public static final String ATTR_IS_PERFORMANCE = "isPerformance";
    public static final String PARAM_INPUT_LOG = "input_log_address";

    public static final String MAPPING_LOG_CONTROLLER = "log";
    public static final String JSP_LIST = MAPPING_LOG_CONTROLLER + "/logList";
    public static final String JSP_EDIT = MAPPING_LOG_CONTROLLER + "/logEdit";

    private LogHelper logHelper = new LogHelper();
    private SystemConfigurationService configService;
    private String sysHome;

    @Autowired(required = true)
    @Qualifier(value = "systemConfigurationService")
    public void setFieldService(SystemConfigurationService ps) {
	this.configService = ps;
    }

    public String getSysHome() {
	if (sysHome == null) {
	    sysHome = this.configService
		    .getValueByName(SystemConstants.CONFIG_SYS_HOME);
	}
	return sysHome;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_LOG_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_LIST, method = RequestMethod.GET)
    public String getLogList(Model model) {
	String rootPath = getSysHome();
	String dirList = this.logHelper.getLogTree(rootPath);
	model.addAttribute(ATTR_LIST, dirList);
	return JSP_LIST;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_LOG_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_EDIT, method = RequestMethod.POST)
    public String editLog(@RequestParam(PARAM_INPUT_LOG) String logPath,
	    Model model) {
	String content = this.logHelper.getLogContents(logPath);

	// If user is opening an error log.
	if (this.logHelper.isSpecialView(logPath, getSysHome(),
		SystemConstants.LOGGER_ERROR)) {
	    model.addAttribute(ATTR_IS_ERROR, true);
	}
	// If user is opening a performance log.
	else if (this.logHelper.isSpecialView(logPath, getSysHome(),
		SystemConstants.LOGGER_PERFORMANCE)) {
	    model.addAttribute(ATTR_IS_PERFORMANCE, true);
	}
	model.addAttribute(ATTR_CONTENT, content);
	model.addAttribute(ATTR_PATH, logPath);
	return JSP_EDIT;
    }
}