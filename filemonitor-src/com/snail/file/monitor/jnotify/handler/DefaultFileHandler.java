package com.snail.file.monitor.jnotify.handler;

/**
 * 
 * <p>
 * Title: com.snail.file.monitor.jnotify.file.DefaultFileHandler
 * </p>
 * 
 * <p>
 * Description: 默认的文件处理器.
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
public class DefaultFileHandler implements FileHandler {

	private static final long serialVersionUID = -6930221387744393505L;

	@Override
	public boolean handle(String fileName, int eventCode) {
		String opType = "";
		switch (eventCode) {
		case 1:
			opType = "新增";
			break;
		case 2:
			opType = "删除";
			break;
		case 3:
			opType = "修改";
			break;
		case 4:
			opType = "重命名";
			break;
		default:
			opType = "未知";
			break;
		}
		System.out.println(opType + "[" + fileName + "]");
		return false;
	}

}
