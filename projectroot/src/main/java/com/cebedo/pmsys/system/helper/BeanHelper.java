package com.cebedo.pmsys.system.helper;

import org.springframework.context.ApplicationContext;

public class BeanHelper {
	private ApplicationContext ctx;

	public Object getBean(String beanName) {
		if (ctx == null) {
			ctx = AppContext.getApplicationContext();
		}
		return ctx.getBean(beanName);
	}

}
