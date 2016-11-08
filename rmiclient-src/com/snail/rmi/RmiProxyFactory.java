package com.snail.rmi;

import java.util.Properties;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.snail.util.StringUtils;

/**
 * <p>
 * Title: com.snail.rmi.RmiProxyFactory
 * </p>
 * 
 * <p>
 * Description: RMI 代理的工厂类
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
public class RmiProxyFactory {

	private static RmiProxyFactory instance;

	private Properties rmiServices = new Properties();

	private RmiProxyFactory() {
	}

	public synchronized static RmiProxyFactory getInstance() {
		if (instance == null) {
			instance = new RmiProxyFactory();
		}
		return instance;
	}

	/**
	 * 依据服务名称 获取RMI服务类.
	 * @param context
	 * @param serviceName. serviceName 和 beanName一样.
	 * @param serviceInterface
	 * @return
	 */
	public <T> T getRmiService(RmiClientContext context, String serviceName,
			Class<T> serviceInterface) {
		return getRmiService(context, serviceName, null, serviceInterface);
	}

	/**
	 * 依据服务名称和bean名称获取RMI服务类.
	 * 先从缓存中查找,若找的则返回,没有找的则从远程服务器中拿下来创建,并使用beanName缓存起来.
	 * @param context
	 * @param serviceName
	 * @param beanName 缓存中的key值
	 * @param serviceInterface
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getRmiService(RmiClientContext context, String serviceName,
			String beanName, Class<T> serviceInterface) {
		if (context == null || StringUtils.isBlank(serviceName)
				|| serviceInterface == null) {
			return null;
		}
		if (StringUtils.isBlank(beanName)) {
			beanName = serviceName;
		}
		String key = getKey(context);
		Object contextServicesObj = rmiServices.get(key);
		Properties contextServices = null;
		if (contextServicesObj != null
				&& (contextServicesObj instanceof Properties)) {
			contextServices = (Properties) contextServicesObj;
		}
		RmiProxyFactoryBean proxyBean = null;
		if (contextServices == null) {
			contextServices = new Properties();
			proxyBean = createRmiServicePorxy(context, serviceName,
					serviceInterface);
			contextServices.put(beanName, proxyBean);
			rmiServices.put(key, contextServices);
			return (T) proxyBean.getObject();
		}

		Object serviceObject = contextServices.get(beanName);
		if (serviceObject != null
				&& (serviceObject instanceof RmiProxyFactoryBean)) {
			proxyBean = (RmiProxyFactoryBean) serviceObject;
			proxyBean.afterPropertiesSet();
			contextServices.put(beanName, proxyBean);
			return (T) proxyBean.getObject();
		}
		proxyBean = createRmiServicePorxy(context, serviceName,
				serviceInterface);
		contextServices.put(beanName, proxyBean);
		return (T) proxyBean.getObject();
	}

	/**
	 * 使用rmiServerHostName:registryPort作为键值
	 * @param context
	 * @return
	 */
	private String getKey(RmiClientContext context) {
		String key = context.getRmiServerHostName() + ":"
				+ context.getRegistryPort();
		return key;
	}

	/**
	 * 从RMI服务器上查找服务,并创建代理类.
	 * @param context
	 * @param serviceName
	 * @param serviceInterface
	 * @return
	 */
	private RmiProxyFactoryBean createRmiServicePorxy(RmiClientContext context,
			String serviceName, Class<?> serviceInterface) {
		String serviceUrl = "rmi://" + context.getRmiServerHostName() + ":"
				+ context.getRegistryPort() + "/" + serviceName;
		RmiProxyFactoryBean proxyBean = new RmiProxyFactoryBean();
		proxyBean.setServiceUrl(serviceUrl);
		proxyBean.setServiceInterface(serviceInterface);
		proxyBean.afterPropertiesSet();
		return proxyBean;
	}

	public Properties getRmiServices() {
		return this.rmiServices;
	}

	/**
	 * 从缓存中查找,若没有找到,则返回null.
	 * @param context
	 * @param beanName
	 * @return
	 */
	public Object findRmiService(RmiClientContext context, String beanName) {
		if (context == null) {
			return null;
		}
		String key = getKey(context);
		Object obj = getRmiServices().get(key);
		if (obj == null || !(obj instanceof Properties)) {
			return null;
		}
		return ((Properties) obj).get(beanName);
	}
}
