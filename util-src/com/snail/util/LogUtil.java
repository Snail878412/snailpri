package com.snail.util;

import org.apache.log4j.Logger;

/**
 * 
 * <p>
 * Title: com.snail.util.LogUtil
 * </p>
 * 
 * <p>
 * Description: 日志处理工具类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月12日
 * 
 * @version 1.0
 * 
 */
public class LogUtil {

	public static Logger getLogger(Object obj) {
		if (obj == null) {
			return Logger.getLogger(LogUtil.class);
		}
		return Logger.getLogger(obj.getClass());
	}

	public static Logger getLogger(Class<?> claz) {
		return Logger.getLogger(claz);
	}

	public static Logger getLogger() {
		try {
			StackTraceElement[] temp = Thread.currentThread().getStackTrace();
			StackTraceElement a = (StackTraceElement) temp[temp.length-1];
			return Logger.getLogger(Class.forName(a.getClassName()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return getLogger(null);
		}
	}
	
	/**
	 * 当为debug模式时,则记录debug日志,否则记error日志
	 * 
	 * @param log
	 * @param errorMsg
	 * @param t
	 */
	public static void errorLog(Logger log, String errorMsg, Throwable t) {
		if (log == null) {
			return;
		}
		if (log.isDebugEnabled()) {
			if (t == null) {
				log.debug(errorMsg);
			} else {
				log.debug(errorMsg, t);
			}
		} else {
			if (t == null) {
				log.error(errorMsg);
			} else {
				log.error(errorMsg, t);
			}
		}
	}

	/**
	 * 当为debug模式时,则记录debug日志,否则记error日志
	 * 
	 * @param log
	 * @param errorMsg
	 */
	public static void errorLog(Logger log, String errorMsg) {
		errorLog(log, errorMsg, null);
	}

	/**
	 * 当为debug模式时,则记录debug日志,否则不记日志
	 * 
	 * @param log
	 * @param msg
	 * @param t
	 */
	public static void debugLog(Logger log, String msg, Throwable t) {
		if (log == null) {
			return;
		}
		if (log.isDebugEnabled()) {
			if (t == null) {
				log.debug(msg);
			} else {
				log.debug(msg, t);
			}
		}
	}

	/**
	 * 当为debug模式时,则记录debug日志,否则不记日志
	 * 
	 * @param log
	 * @param msg
	 * @param t
	 */
	public static void debugLog(Logger log, String msg) {
		debugLog(log, msg, null);
	}

}
