package com.cebedo.pmsys.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cebedo.pmsys.constants.RegistryLogger;

public class ExecuteTimeInterceptorImpl extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(RegistryLogger.LOGGER_PERFORMANCE);
    private static final String ATTR_START = "START_TIME";
    private static final int THRESHOLD_MIN = 100; // 100 milliseconds.

    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private final String logString = "TSTAMP:\"%s\" CLASS_NAME:\"%s\" DURATION:\"%s\"";

    /**
     * Before the actual handler will be executed.
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	    throws Exception {
	long startTime = System.currentTimeMillis();
	request.setAttribute(ATTR_START, startTime);
	return true;
    }

    /**
     * After the handler is executed.
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	    ModelAndView modelAndView) throws Exception {

	String handlerStr = handler.toString();

	// Log only requests that are ours.
	if (handlerStr.contains("pmsys")) {

	    long startTime = (Long) request.getAttribute(ATTR_START);
	    long endTime = System.currentTimeMillis();
	    long executeTime = endTime - startTime;

	    // If we don't reach the threshold, don't log.
	    if (executeTime > THRESHOLD_MIN) {
		Date start = new Date(startTime);

		logger.trace(String.format(this.logString, this.formatter.format(start), handlerStr,
			executeTime));
	    }
	}
    }
}