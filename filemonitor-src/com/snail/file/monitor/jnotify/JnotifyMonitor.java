package com.snail.file.monitor.jnotify;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import org.apache.log4j.Logger;

import com.snail.file.monitor.jnotify.bean.JnotifyMonitorBean;
import com.snail.file.monitor.jnotify.handler.DefaultFileHandler;
import com.snail.file.monitor.jnotify.handler.FileHandler;
import com.snail.file.monitor.jnotify.listeners.DefaultJnotifyListener;
import com.snail.util.FileUtil;

/**
 * 
 * <p>
 * Title: com.snail.file.monitor.jnotify.monitor.JnotifyMonitor
 * </p>
 * 
 * <p>
 * Description: JNotify文件监控器.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月26日
 * 
 * @version 1.0
 * 
 */
public final class JnotifyMonitor {

	private static final Logger log = Logger.getLogger(JnotifyMonitor.class);
	
	private static JnotifyMonitor monitor;

	private boolean runing = false;
	private boolean stoped = true;

	private boolean stopMonitor = false;

	private static Map<Integer, JnotifyMonitorBean> monitorBeanMap = new HashMap<Integer, JnotifyMonitorBean>();

	private JnotifyMonitor() {
		copyLibFile();
	}

	public static synchronized JnotifyMonitor getInstance() {
		if (monitor == null) {
			monitor = new JnotifyMonitor();
		}
		return monitor;
	}

	public boolean removeWatch(int watchId) {
		try {
			return JNotify.removeWatch(watchId);
		} catch (JNotifyException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 添加监视器.
	 * 
	 * @param bean
	 * @return
	 */
	public int addWatch(JnotifyMonitorBean bean) {
		try {
			int watchId = JNotify.addWatch(bean.getDir(), bean.getMask(),
					bean.isWatchSubtree(), bean.getListener());
			monitorBeanMap.put(watchId, bean);
			log.info("添加监听器成功!监听号:" + watchId + " " + bean);
			return watchId;
		} catch (JNotifyException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 使用默认的处理器来监视指定的目录
	 * 
	 * @param dir
	 * @return
	 */
	public int addWatch(String dir) {
		return addWatch(dir, new DefaultFileHandler());
	}

	/**
	 * 监视指定目录下的所有文件.
	 * 
	 * @param dir
	 * @param handler
	 * @return
	 */
	public int addWatch(String dir, FileHandler handler) {
		return addWatch(dir, handler, "");
	}

	/**
	 * 监视指定目录下的指定类型的文件.
	 * 
	 * @param dir
	 * @param handler
	 * @param fileType
	 * @return
	 */
	public int addWatch(String dir, FileHandler handler, String fileType) {
		JnotifyMonitorBean bean = new JnotifyMonitorBean();
		bean.setDir(dir);
		bean.setListener(new DefaultJnotifyListener(handler));
		bean.setFileType(fileType);
		return addWatch(bean);
	}

	/**
	 * 复制DLL文件.
	 */
	private void copyLibFile() {
		String[] javaLibPaths = System.getProperty("java.library.path").split(
				";");
		int i = 0;
		String javaLibPath = javaLibPaths[i++];
		while (javaLibPath.equals(".") || !new File(javaLibPath).canWrite()) {
			javaLibPath = javaLibPaths[i++];
		}
		String osModel = System.getProperty("sun.arch.data.model");
		String fileName = "jnotify.dll";
		if ("64".equals(osModel)) {
			fileName = "jnotify_64bit.dll";
		}
		File file = new File(javaLibPath + "/" + fileName);
		if (file.exists()) {
			return;
		}
		File srcFile = new File(fileName);
		try {
			FileUtil.copyFile(srcFile, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动监控器
	 */
	public synchronized void start() {
		if (isRuning()) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				log.info("Monitor is started!");
				setRuning(true);
				setStoped(false);
				setStopMonitor(false);
				while (true) {
					try {
						Thread.sleep(1 * 1000);
					} catch (InterruptedException e) {
						// do nothing
					}
					if (isStopMonitor()) {
						break;
					}
				}
				setRuning(false);
				setStoped(true);
				log.info("Monitor is stoped!");
			}
		}.start();
	}

	/**
	 * 停止监控器.
	 * 
	 * @return
	 */
	public synchronized void stopMonitor() {
		setStopMonitor(true);
	}

	public boolean isRuning() {
		return runing;
	}

	private void setRuning(boolean runing) {
		this.runing = runing;
	}

	public boolean isStoped() {
		return stoped;
	}

	private void setStoped(boolean stoped) {
		this.stoped = stoped;
	}

	public static Map<Integer, JnotifyMonitorBean> getMonitorBeanMap() {
		return monitorBeanMap;
	}

	public static void setMonitorBeanMap(
			Map<Integer, JnotifyMonitorBean> monitorBeanMap) {
		JnotifyMonitor.monitorBeanMap = monitorBeanMap;
	}

	public boolean isStopMonitor() {
		return stopMonitor;
	}

	public void setStopMonitor(boolean stopMonitor) {
		this.stopMonitor = stopMonitor;
	}
}
