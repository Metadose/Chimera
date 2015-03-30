package com.cebedo.pmsys.log.resolver;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.cebedo.pmsys.common.AuthHelper;
import com.cebedo.pmsys.common.LogHelper;
import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;

public class LoggingExceptionResolver extends SimpleMappingExceptionResolver {

	private Logger logger = Logger.getLogger(SystemConstants.LOGGER_ERROR);
	private AuthHelper authHelper = new AuthHelper();
	private LogHelper logHelper = new LogHelper();

	@SuppressWarnings("rawtypes")
	@Override
	protected void logException(Exception ex, HttpServletRequest request) {
		AuthenticationToken auth = this.authHelper.getAuth();
		String logStr = buildLogMessage(ex, request) + "<br/><br/>";

		// URL.
		logStr += "<b>URL</b><br/>" + request.getRequestURL() + "<br/><br/>";

		// Headers.
		logStr += "<b>HEADERS</b><br/>";
		Enumeration<?> enums = request.getHeaderNames();
		while (enums.hasMoreElements()) {
			String nextElem = enums.nextElement().toString();
			logStr += "<b>" + nextElem + "</b> = "
					+ request.getHeader(nextElem) + "<br/>";
		}
		logStr += "<br/>";

		// Attributes.
		// enums = request.getAttributeNames();
		// while (enums.hasMoreElements()) {
		// String nextElem = enums.nextElement().toString();
		// System.out.println(nextElem + " = "
		// + request.getAttribute(nextElem));
		// }

		// Parameters.
		logStr += "<b>PARAMETERS</b><br/>";
		Map paramMap = request.getParameterMap();
		for (Object key : paramMap.keySet()) {
			logStr += "<b>" + key + "</b> = "
					+ request.getParameter(String.valueOf(key)) + "<br/>";
		}
		logStr += "<br/>";

		this.logger.error(this.logHelper.generateLogMessage(auth, logStr), ex);
	}
}