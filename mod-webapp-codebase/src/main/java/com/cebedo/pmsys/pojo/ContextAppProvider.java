package com.cebedo.pmsys.pojo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This class provides an application-wide access to the Spring
 * ApplicationContext! The ApplicationContext is injected in a static method of
 * the class "ContextApp".
 *
 * Use ContextApp.getApplicationContext() to get access to all Spring Beans.
 *
 * @author Siegfried Bolz
 */
public class ContextAppProvider implements ApplicationContextAware {

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		// Wiring the ApplicationContext into a static method
		ContextApp.setApplicationContext(ctx);
	}
}