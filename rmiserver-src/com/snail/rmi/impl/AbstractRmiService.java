package com.snail.rmi.impl;

import org.springframework.remoting.rmi.RmiServiceExporter;

import com.snail.rmi.RmiServerContext;
import com.snail.rmi.RmiService;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.rmi.impl.AbstractRmiService
 * </p>
 * 
 * <p>
 * Description: 抽象RMI服务实现类.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月20日
 * 
 * @version 1.0
 * 
 */
public abstract class AbstractRmiService extends RmiServiceExporter implements
		RmiService {
	private static final long serialVersionUID = 3598988256864882825L;

	private String name;

	public AbstractRmiService() {
		setRegistryPort(RmiServerContext.registryPort);
		setServicePort(RmiServerContext.servicePort);
		init();
	}

	public void init() {
		setServiceInterface(this.getClass().getInterfaces()[0]);
		setService(this);
		setName(getDefaultServiceName());
		if (StringUtils.isNotBlank(getName())) {
			setServiceName(getName());
		}
	}

	private String getDefaultServiceName() {
		String serviceName = this.getClass().getName();
		serviceName = serviceName.substring(serviceName.lastIndexOf(".") + 1);
		serviceName = serviceName.substring(0, 1).toLowerCase()
				+ serviceName.substring(1);
		if (serviceName.endsWith("Impl")) {
			serviceName = serviceName.substring(0, serviceName.length() - 4);
		}
		return serviceName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getStatus() throws Exception {
		System.out.println("有一个客户端进来了!");
		return "200";
	}
}
