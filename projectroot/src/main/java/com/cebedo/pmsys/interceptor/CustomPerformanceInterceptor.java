package com.cebedo.pmsys.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.util.StopWatch;

public class CustomPerformanceInterceptor extends AbstractMonitoringInterceptor {

    private static final long serialVersionUID = -2223777220091898111L;
    private static final int THRESHOLD_MIN = 500;

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
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger)
	    throws Throwable {
	String name = createInvocationTraceName(invocation);
	StopWatch stopWatch = new StopWatch(name);
	stopWatch.start(name);
	try {
	    return invocation.proceed();
	} finally {
	    stopWatch.stop();
	    long timeMillis = stopWatch.getTotalTimeMillis();
	    String logStr = "<td>" + name + "</td><td>" + timeMillis + "</td>";

	    // Log only requests that take more than the minimum threshold.
	    if (timeMillis > THRESHOLD_MIN) {
		logger.trace(logStr);
	    }
	}
    }
}
