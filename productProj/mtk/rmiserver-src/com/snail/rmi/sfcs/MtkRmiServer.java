package com.snail.rmi.sfcs;

import java.io.IOException;
import java.util.Properties;

import com.snail.rmi.RmiServerContext;
import com.snail.util.ProUtils;
import com.snail.util.StringUtils;

public class MtkRmiServer {

	// 防止启动多个.
	private static boolean running = false;
	// 配置文件.
	private static Properties pro;

	private static String fileName = "monitorConfig.properties";

	public static void main(String[] args) {
		if (running) {
			System.out.println("RMI服务器已经启动...");
			return;
		}
		System.out.println("开始启动RMI服务器.....");
		RmiServerContext rmiServer = RmiServerContext.getInstance();
		System.out.println("正在初始化配置文件...");
		if (!initPro(fileName)) {
			System.out.println("监视器启动失败:配置文件初始化失败!");
			return;
		}
		String registryPort = pro.getProperty("registryPort");
		String servicePort = pro.getProperty("servicePort");
		if (StringUtils.isNotBlank(registryPort)) {
			RmiServerContext.registryPort = Integer.parseInt(registryPort);
		}
		if (StringUtils.isNotBlank(registryPort)) {
			RmiServerContext.servicePort = Integer.parseInt(servicePort);
		}
		rmiServer.startRmiServer();
		System.out.println("RMI服务器启动成功!");
	}

	private static boolean initPro(String fileName) {
		try {
			pro = ProUtils.createProperties(fileName, false);
			return true;
		} catch (IOException e) {
			System.out.println("============配置文件初始化失败[" + fileName + "]");
			e.printStackTrace();
		}
		return false;
	};
}
