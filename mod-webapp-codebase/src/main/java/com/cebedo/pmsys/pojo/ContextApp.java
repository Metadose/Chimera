package com.cebedo.pmsys.pojo;

import org.springframework.context.ApplicationContext;

/**
 * This class provides application-wide access to the Spring ApplicationContext.
 * The ApplicationContext is injected by the class "ContextAppProvider".
 *
 * @author Siegfried Bolz
 */
public class ContextApp {

	private static ApplicationContext ctx;

	/**
	 * Injected from the class "ContextAppProvider" which is
	 * automatically loaded during Spring-Initialization.
	 */
	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		ctx = applicationContext;
	}

	/**
	 * Get access to the Spring ApplicationContext from everywhere in your
	 * Application.
	 *
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
}