package com.cebedo.pmsys.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.util.StopWatch;

public class CustomPerformanceInterceptor extends AbstractMonitoringInterceptor {

    private static final long serialVersionUID = -2223777220091898111L;
    private static final int THRESHOLD_MIN = 0; // Log all.
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    /**
     * Create a new PerformanceMonitorInterceptor with a static logger.
     */
    public CustomPerformanceInterceptor() {
	;
    }

    /**
     * Create a new PerformanceMonitorInterceptor with a dynamic or static
     * logger, according to the given flag.
     * 
     * @param useDynamicLogger
     *            whether to use a dynamic logger or a static logger
     * @see #setUseDynamicLogger
     */
    public CustomPerformanceInterceptor(boolean useDynamicLogger) {
	setUseDynamicLogger(useDynamicLogger);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
	Date start = new Date(System.currentTimeMillis());
	String name = createInvocationTraceName(invocation);
	StopWatch stopWatch = new StopWatch(name);
	stopWatch.start(name);
	try {
	    return invocation.proceed();
	} finally {
	    stopWatch.stop();
	    long timeMillis = stopWatch.getTotalTimeMillis();

	    String logString = "TSTAMP:\"%s\" CLASS_NAME:\"%s\" DURATION:\"%s\"";

	    // Log only requests that take more than the minimum threshold.
	    if (timeMillis > THRESHOLD_MIN) {
		logger.trace(String.format(logString, this.formatter.format(start), name, timeMillis));
	    }
	}
    }
}
