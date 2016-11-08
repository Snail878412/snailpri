package com.snail.kettle.scheduler.proxy;

import org.pentaho.di.core.exception.KettleException;

import com.snail.common.SPException;
import com.snail.kettle.scheduler.NativeEngine;

/**
 * 
 * <p>
 * Title: com.snail.kettle.scheduler.proxy.AbstractNativeKettleJobProxy
 * </p>
 * 
 * <p>
 * Description: Kettle任务的抽象代理类.
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
public abstract class AbstractNativeKettleJobProxy implements Modifiable {

	public void start() throws SPException, KettleException {
		NativeEngine.getInstance().start(getKettleJobNo());
	}

	public void stop() throws KettleException {
		NativeEngine.getInstance().get(getKettleJobNo()).stop();
	}

	public String getState() throws KettleException {
		return NativeEngine.getInstance().get(getKettleJobNo()).getStatus();
	}

	public abstract String getKettleJobNo();
}
