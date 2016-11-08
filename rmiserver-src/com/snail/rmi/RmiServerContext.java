package com.snail.rmi;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.snail.util.LogUtil;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.rmi.RmiServerContext
 * </p>
 * 
 * <p>
 * Description: RMI服务器
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年7月30日
 * 
 * @version 1.0
 * 
 */
public class RmiServerContext {

	private Logger log = Logger.getLogger(RmiServerContext.class);

	public static int registryPort = 9808;
	public static int servicePort = 9809;

	private boolean runing = false;

	public static ApplicationContext rmiContext;

	private String configPath = "config/rmi_server.xml";

	private static RmiServerContext instance;

	private RmiServerContext() {
	};

	public static synchronized RmiServerContext getInstance() {
		if (instance == null) {
			instance = new RmiServerContext();
		}
		return instance;
	}

	public static void main(String[] args) {
		RmiServerContext context = RmiServerContext.getInstance();
		context.startRmiServer();
	}

	public void startRmiServer() {
		if (isRuning()) {
			return;
		}
		if (StringUtils.isBlank(getConfigPath())) {
			return;
		}
		try {
			log.info("开始启动RMI服务器...");
			setRmiContext(new ClassPathXmlApplicationContext(getConfigPath()));
			log.info("RMI服务器启动成功!");
			setRuning(true);
		} catch (BeansException e) {
			e.printStackTrace();
			System.out.println("服务器启动失败:" + e.getMessage());
			log.info("RMI服务器启动失败!");
			LogUtil.errorLog(log, "RMI服务器启动失败!", e);
		}
	}

	public void reStartRmiServer() {
		if (isRuning()) {
			stopRmiServer();
		}
		startRmiServer();
	}

	private void stopRmiServer() {
		if (!isRuning()) {
			return;
		}
		log.info("正在关闭RMI服务器...");
		Map<String, RmiServiceExporter> rmiServiceExporter = rmiContext
				.getBeansOfType(RmiServiceExporter.class);

		try {
			for (RmiServiceExporter remote : rmiServiceExporter.values()) {
				remote.destroy();
			}
			rmiContext = null;
			setRuning(false);
			log.info("关闭RMI服务器成功!");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public ApplicationContext getRmiContext() {
		return RmiServerContext.rmiContext;
	}

	public void setRmiContext(ApplicationContext rmiContext) {
		RmiServerContext.rmiContext = rmiContext;
	}

	public boolean isRuning() {
		return runing;
	}

	private void setRuning(boolean runing) {
		this.runing = runing;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

}
