package com.snail.rmi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 * <p>
 * Title: com.snail.rmi.RmiClientXmlContext
 * </p>
 * 
 * <p>
 * Description: RMI客户端.使用spring的配置文件
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年9月1日
 * 
 * @version 1.0
 * 
 */
public class RmiClientXmlContext {
	private static final String CONFIG_PATH = "config/rmi_client.xml";
	private ApplicationContext context;

	public RmiClientXmlContext() {
		setContext(new FileSystemXmlApplicationContext(CONFIG_PATH));
	}

	public Object findRmiService(String beanName) {
		return getContext().getBean(beanName);
	}

	public ApplicationContext getContext() {
		return context;
	}

	private void setContext(ApplicationContext context) {
		this.context = context;
	}
}
