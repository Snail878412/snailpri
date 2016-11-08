package com.snail.file.monitor.jnotify.listeners;

import com.snail.file.monitor.jnotify.handler.FileHandler;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.file.monitor.jnotify.listeners.DefaultJnotifyListener
 * </p>
 * 
 * <p>
 * Description: 文件监视器默认处理器.
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
public class DefaultJnotifyListener implements JnotifyBaseListener {

	private static final long serialVersionUID = -116888342983848543L;

	private String fileType; // 监控文件类型
	private FileHandler handler; // 文件处理器.

	public DefaultJnotifyListener() {
	}

	public DefaultJnotifyListener(FileHandler handler) {
		this.handler = handler;
	}

	public DefaultJnotifyListener(FileHandler handler, String fileType) {
		this.handler = handler;
		this.fileType = fileType;
	}

	@Override
	public void fileCreated(int watchId, String rootPath, String name) {
		if (!isMonitorFileType(name)) { // 不是待监控的文件.
			return;
		}
		handler.handle(rootPath + "/" + name, FileHandler.CREATE);
	}

	@Override
	public void fileDeleted(int watchId, String rootPath, String name) {
		if (!isMonitorFileType(name)) { // 不是待监控的文件.
			return;
		}
		handler.handle(rootPath + "/" + name, FileHandler.DELETE);
	}

	@Override
	public void fileModified(int watchId, String rootPath, String name) {
		if (!isMonitorFileType(name)) { // 不是待监控的文件.
			return;
		}
		handler.handle(rootPath + "/" + name, FileHandler.MODIFY);
	}

	@Override
	public void fileRenamed(int watchId, String rootPath, String name,
			String newName) {
		if (!isMonitorFileType(name)) { // 不是待监控的文件.
			return;
		}
		handler.handle(rootPath + "/" + name + ";" + rootPath + "/" + newName,
				FileHandler.RENAME);
	}

	public boolean isMonitorFileType(String fileName) {
		if (StringUtils.isBlank(fileName) || fileName.indexOf(".") < 0) { // 文件名为空或没有后缀,则认为不是监控的文件.
			return false;
		}
		if (StringUtils.isBlank(getFileType())) { // 没有指定监控问文件类型,则默认监视所有的文件类型.
			return true;
		}
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		return fileType.equals(getFileType());
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public FileHandler getHandler() {
		return handler;
	}

	public void setHandler(FileHandler handler) {
		this.handler = handler;
	}

}
