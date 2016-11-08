package com.snail.file.monitor.jnotify.handler;

import java.io.Serializable;

/**
 * 
 * <p>
 * Title: com.snail.file.monitor.jnotify.file.FileHandler
 * </p>
 * 
 * <p>
 * Description: 文件处理器
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
public interface FileHandler extends Serializable{

	public static final Integer CREATE = 1;
	public static final Integer DELETE = 2;
	public static final Integer MODIFY = 3;
	public static final Integer RENAME = 4;
	
	/**
	 * 处理文件.
	 * @param fileName
	 * @param eventCode 事件代码
	 * @return
	 */
	public boolean handle(String fileName,int eventCode);
}
