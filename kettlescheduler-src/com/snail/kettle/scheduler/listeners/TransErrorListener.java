package com.snail.kettle.scheduler.listeners;

import java.util.Date;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransListener;

import com.snail.util.DateUtil;

/**
 * 
 * <p>
 * Title: com.snail.kettle.scheduler.listeners.TransErrorListener
 * </p>
 * 
 * <p>
 * Description: 转换出错监听器
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年7月29日
 * 
 * @version 1.0
 * 
 */
public class TransErrorListener extends AbstractErrorListener implements
		TransListener {

	@Override
	public void transFinished(Trans trans) throws KettleException {
		if (trans.getErrors() > 0) {
			StringBuffer errorInfo = new StringBuffer();
			errorInfo.append("<p>Kettle转换执行过程中至少有一个步骤出错了,请及时处理!</p>");
			errorInfo.append("<li>转换名称:" + trans.getName() + "</li>");
			errorInfo.append("<li>执行时间:"
					+ DateUtil.getFormatDateTime(new Date()) + "</li>");
			errorInfo.append("<p>更多详情请参见日志文件</p>");
			runErrorWarring(errorInfo.toString());
		}
	}

	@Override
	public void transActive(Trans arg0) {
		// do nothing
	}

	@Override
	public void transStarted(Trans arg0) throws KettleException {
		// do nothing
	}
}
