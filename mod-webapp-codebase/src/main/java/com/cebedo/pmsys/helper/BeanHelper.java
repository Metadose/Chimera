package com.cebedo.pmsys.helper;

import org.springframework.context.ApplicationContext;

import com.cebedo.pmsys.pojo.ContextApp;

public class BeanHelper {
	private ApplicationContext ctx;

	public Object getBean(String beanName) {
		if (ctx == null) {
			ctx = ContextApp.getApplicationContext();
		}
		return ctx.getBean(beanName);
	}

}
