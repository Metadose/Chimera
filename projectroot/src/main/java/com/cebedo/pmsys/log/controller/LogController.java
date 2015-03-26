package com.cebedo.pmsys.log.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.log.service.LogServiceImpl;

@Controller
@RequestMapping(LogController.MAPPING_LOG_CONTROLLER)
public class LogController {

	public static final String ATTR_LIST = "logList";
	public static final String MAPPING_LOG_CONTROLLER = "log";
	public static final String JSP_LIST = "logList";
	public static final String JSP_EDIT = "logEdit";

	// private LogService logService;
	//
	// @Autowired(required = true)
	// @Qualifier(value = "logService")
	// public void setLogService(LogService s) {
	// this.logService = s;
	// }

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_LOG_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_LIST, method = RequestMethod.GET)
	public String getLogList(Model model) {
		String dirList = LogServiceImpl.getLogTree();
		model.addAttribute(ATTR_LIST, dirList);
		return JSP_LIST;
	}

	// @PreAuthorize("hasRole('" + SystemConstants.ROLE_LOG_EDITOR + "')")
	// @RequestMapping(value = "tree", method = RequestMethod.GET)
	// public String getLogTree(Model model) {
	// String logTreeHTML = this.logService.getLogTree();
	// model.addAttribute(ATTR_LIST, logTreeHTML);
	// return JSP_LIST;
	// }
	//
	// @PreAuthorize("hasRole('" + SystemConstants.ROLE_LOG_EDITOR + "')")
	// public String editLog() {
	// return JSP_EDIT;
	// }

}