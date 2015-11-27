package com.cebedo.pmsys.base;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

public interface IRunnableModeler
	extends Runnable, InitializingBean, ApplicationContextAware, Cloneable {

    public boolean isAlive();

}
