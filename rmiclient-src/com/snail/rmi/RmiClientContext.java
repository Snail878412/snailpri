package com.snail.rmi;

import java.util.Properties;

import com.snail.util.ProUtils;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.rmi.RmiClientContext
 * </p>
 * 
 * <p>
 * Description: RMI客户端.
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
public class RmiClientContext {

	private String rmiServerHostName = "127.0.0.1";
	private int registryPort = 9808;
	
	public RmiClientContext() {
		String hostName = ProUtils.getPropertyValue("RMI_HOST");
		String port = ProUtils.getPropertyValue("RMI_PORT");
		if (StringUtils.isNotEmpty(hostName)) {
			setRmiServerHostName(hostName);
		}
		if (StringUtils.isNotEmpty(port) && StringUtils.isNumeric(port)) {
			setRegistryPort(Integer.parseInt(port));
		}
	}

	public RmiClientContext(String rmiServerHostName, int registryPort) {
		this.setRmiServerHostName(rmiServerHostName);
		this.setRegistryPort(registryPort);
	}

	public <T> T getRmiService(String serviceName, Class<T> serviceInterface) {
		return RmiProxyFactory.getInstance().getRmiService(this, serviceName,
				serviceInterface);
	}

	public Properties getRmiServices() {
		String key = getRmiServerHostName() + ":" + getRegistryPort();
		return (Properties) RmiProxyFactory.getInstance().getRmiServices()
				.get(key);
	}

	public Object findRmiService(String beanName) {
		return RmiProxyFactory.getInstance().findRmiService(this, beanName);
	}

	public String getServerStatus() {
		try {
			ServerInfoService serverInfoService = getRmiService("serverInfo",
					ServerInfoService.class);
			return serverInfoService.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "120";
	}

	public String getRmiServerHostName() {
		return rmiServerHostName;
	}

	public void setRmiServerHostName(String rmiServerHostName) {
		this.rmiServerHostName = rmiServerHostName;
	}

	public int getRegistryPort() {
		return registryPort;
	}

	public void setRegistryPort(int registryPort) {
		this.registryPort = registryPort;
	}
}
