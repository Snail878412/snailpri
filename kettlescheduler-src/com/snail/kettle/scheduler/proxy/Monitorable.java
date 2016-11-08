package com.snail.kettle.scheduler.proxy;

import org.pentaho.di.core.exception.KettleException;

import com.snail.common.SPException;

/**
 * 
 * <p>
 * Title: com.snail.kettle.scheduler.proxy.Monitorable
 * </p>
 * 
 * <p>
 * Description: 可以被监控的.主要包括启动/停止/查看状态等
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年7月28日
 * 
 * @version 1.0
 * 
 */
public interface Monitorable {
	/**
	 * 启动
	 * @throws SPException 
	 * @throws KettleException 
	 */
	public void start() throws SPException, KettleException;

	/**
	 * 停止
	 * @throws SPException 
	 * @throws KettleException 
	 */
	public void stop() throws SPException, KettleException;


	/**
	 * 查询状态
	 * @throws KettleException 
	 */
	public String getState() throws KettleException;
}
