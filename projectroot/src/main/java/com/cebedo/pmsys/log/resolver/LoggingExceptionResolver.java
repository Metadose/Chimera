package com.cebedo.pmsys.log.resolver;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.cebedo.pmsys.common.SystemConstants;

public class LoggingExceptionResolver extends SimpleMappingExceptionResolver {

	private Logger logger = Logger.getLogger(SystemConstants.LOGGER_ERROR);

	@Override
	protected void logException(Exception ex, HttpServletRequest request) {
		this.logger.warn(buildLogMessage(ex, request), ex);
	}
}