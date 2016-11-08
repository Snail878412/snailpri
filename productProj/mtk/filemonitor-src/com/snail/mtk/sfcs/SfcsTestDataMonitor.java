package com.snail.mtk.sfcs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.snail.file.monitor.jnotify.JnotifyMonitor;
import com.snail.file.monitor.jnotify.bean.JnotifyMonitorBean;
import com.snail.file.monitor.jnotify.listeners.DefaultJnotifyListener;
import com.snail.mtk.sfcs.monitor.handler.SfcsTestDataHandler;
import com.snail.rmi.RmiClientContext;
import com.snail.rmi.sfcs.TestDataService;
import com.snail.util.ProUtils;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.mtk.sfcs.SfcsTestDataMonitor
 * </p>
 * 
 * <p>
 * Description: SFCS 测试数据监控处理器.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月27日
 * 
 * @version 1.0
 * 
 */
public class SfcsTestDataMonitor {
	// 防止启动多个.
	private static boolean running = false;
	// 配置文件.
	private static Properties pro;

	private static String fileName = "monitorConfig.properties";

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		if(running){
			System.out.println("监视器已经启动...");
			return ;
		}
		
		System.out.println("开始启动监听器.....");
		// 本地文件监听器.
		JnotifyMonitor monitor = JnotifyMonitor.getInstance();
		// RMI客户端.
		RmiClientContext rmiClient = new RmiClientContext();

		// 初始化配置文件
		System.out.println("正在初始化配置文件...");
		if (!initPro(fileName)) {
			System.out.println("监视器启动失败:配置文件初始化失败!");
			return;
		}
		// 准备RMI服务
		System.out.println("正在准备RMI服务...");
		String rmiServerHostName = pro.getProperty("RMI_HOST");
		String rmiServerPort = pro.getProperty("RMI_PORT");
		if (StringUtils.isNotBlank(rmiServerHostName)) {
			rmiClient.setRmiServerHostName(rmiServerHostName);
		}
		if (StringUtils.isNotBlank(rmiServerPort) && StringUtils.isNumeric(rmiServerPort)) {
			rmiClient.setRegistryPort(Integer.parseInt(rmiServerPort));
		}
		if (!"200".equals(rmiClient.getServerStatus())) {
			System.out.println("监视器启动失败:rmi服务器连接失败,请确认!");
			return;
		}
		TestDataService testDataService = null;
		try {
			testDataService = rmiClient.getRmiService("testDataService", TestDataService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (testDataService == null) {
			System.out
					.println("监视器启动失败:没有找testDataService,或不是TestDataService类.");
			return;
		}

		// 开始加载监听对象
		System.out.println("正在加载监听对象...");
		String monitorBeans = pro.getProperty("monitorBeans");
		JSONArray beanJsonArray = JSONArray.fromObject(monitorBeans);
		if (beanJsonArray == null || beanJsonArray.isEmpty()) {
			System.out.println("监视器启动失败:没有指定监听对象!");
			return;
		}
		List<JnotifyMonitorBean> beanList = new ArrayList<JnotifyMonitorBean>();
		Iterator it = beanJsonArray.iterator();
		while (it.hasNext()) {
			JSONObject jsonObj = (JSONObject) it.next();
			JnotifyMonitorBean bean = new JnotifyMonitorBean();
			if (!jsonObj.containsKey("watchPath")
					|| !jsonObj.containsKey("fileHandler")) {
				continue;
			}
			String fileHandlerClassName = jsonObj.getString("fileHandler");
			Object fileHandlerObj = null;
			try {
				Class<?> clazz = Class.forName(fileHandlerClassName);
				fileHandlerObj = clazz.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (fileHandlerObj == null
					|| !(fileHandlerObj instanceof SfcsTestDataHandler)) {
				continue;
			}
			SfcsTestDataHandler fileHandler = (SfcsTestDataHandler) fileHandlerObj;
			fileHandler.setService(testDataService);
			if (jsonObj.containsKey("fields")) {
				fileHandler.setFields(jsonObj.getString("fields"));
			}
			if (jsonObj.containsKey("encoding")) {
				fileHandler.setEncoding(jsonObj.getString("encoding"));
			}
			DefaultJnotifyListener listener = new DefaultJnotifyListener(
					fileHandler);
			if (jsonObj.containsKey("fileType")) {
				bean.setFileType(jsonObj.getString("fileType"));
				listener.setFileType(jsonObj.getString("fileType"));
			}
			bean.setListener(listener);
			bean.setDir(jsonObj.getString("watchPath"));
			if (jsonObj.containsKey("mask")) {
				bean.setMask(Integer.parseInt(jsonObj.getString("mask")));
			}
			if (jsonObj.containsKey("watchSubtree")) {
				bean.setWatchSubtree(Boolean.parseBoolean(jsonObj
						.getString("watchSubtree")));
			}
			beanList.add(bean);
		}
		if(beanList.isEmpty()){
			System.out.println("监视器启动失败:没有找到有效的监控对象");
			return;
		}
		for(JnotifyMonitorBean bean:beanList){
			monitor.addWatch(bean);
		}
		monitor.start();
		System.out.println("监视器启动成功!");
		
		SfcsTestDataMonitor.running = true;
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
