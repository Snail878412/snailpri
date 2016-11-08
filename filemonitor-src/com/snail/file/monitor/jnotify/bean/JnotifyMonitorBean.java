package com.snail.file.monitor.jnotify.bean;

import net.contentobjects.jnotify.JNotify;

import com.snail.file.monitor.jnotify.listeners.JnotifyBaseListener;

/**
 * 
 * <p>
 * Title: com.snail.file.monitor.jnotify.bean.JnotifyMonitorBean
 * </p>
 * 
 * <p>
 * Description: 被监控对象
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
public class JnotifyMonitorBean {
	private String dir; // 监控的目录
	private String fileType; // 监控的文件类型.
	private JnotifyBaseListener listener; // 监控处理器.
	private Integer mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED
			| JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;;
	private boolean watchSubtree = false;

	@Override
	public String toString() {
		return "JnotifyMonitorBean [dir=" + dir + ", fileType=" + fileType
				+ ", listener=" + listener.getClass().getName() + ", mask="
				+ mask + ", watchSubtree=" + watchSubtree + "]";
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public boolean isWatchSubtree() {
		return watchSubtree;
	}

	public void setWatchSubtree(boolean watchSubtree) {
		this.watchSubtree = watchSubtree;
	}

	public JnotifyBaseListener getListener() {
		return listener;
	}

	public void setListener(JnotifyBaseListener listener) {
		this.listener = listener;
	}

}
