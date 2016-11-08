package com.snail.rmi.impl.kettle;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Component;

import com.snail.common.SPException;
import com.snail.kettle.scheduler.NativeEngine;
import com.snail.kettle.scheduler.beans.KettleJob;
import com.snail.rmi.impl.AbstractRmiService;
import com.snail.rmi.kettle.SchedulerRmi;
import com.snail.util.LogUtil;
import com.snail.util.ReturnUtil;

/**
 * 
 * <p>
 * Title: com.snail.rmi.kettle.impl.SchedulerRmiImpl
 * </p>
 * 
 * <p>
 * Description: Kettle 任务调度引擎Rmi接口实现
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年7月30日
 * 
 * @version 1.0
 * 
 */
@Component("schedulerRmiService")
public class SchedulerRmiImpl extends AbstractRmiService implements
		SchedulerRmi {

	private static final long serialVersionUID = -1681862745848128924L;
	private static final Logger log = Logger.getLogger(SchedulerRmiImpl.class);
	private NativeEngine engine;

	public SchedulerRmiImpl() throws KettleException {
		super();
		setEngine(NativeEngine.getInstance());
//		setServiceName("schedulerRmiService");
	}

	@Override
	public String startKettleJob(String jobNo) {
		try {
			if (getEngine().start(jobNo)) {
				return ReturnUtil.getSuccessReturnStr(null);
			}
		} catch (SPException e) {
			LogUtil.errorLog(log, e.getMessage());
			return ReturnUtil.getFailReturnStr(e.getMessage());
		}
		return ReturnUtil.getFailReturnStr("启动失败,请确认任务编号是否正确!");
	}

	@Override
	public String stopKettleJob(String jobNo) {
		return ReturnUtil.getFailReturnStr("暂时不支持停止任务!");
	}

	@Override
	public String addKettleJob(String fileName) {
		try {
			KettleJob kettleJob = getEngine().add(fileName);
			return ReturnUtil.getSuccessReturnStr(kettleJob);
		} catch (SPException e) {
			LogUtil.errorLog(log, e.getMessage(), e);
			return ReturnUtil.getFailReturnStr(e.getMessage());
		}
	}

	@Override
	public String removeKettleJob(String jobNo) {
		try {
			getEngine().remove(jobNo);
		} catch (SPException e) {
			LogUtil.errorLog(log, e.getMessage());
			return ReturnUtil.getFailReturnStr(e.getMessage());
		}
		return ReturnUtil.getSuccessReturnStr("任务移除成功!");
	}

	@Override
	public String getKettleJobStatus(String jobNo) {
		KettleJob kettleJob = getEngine().get(jobNo);
		if (kettleJob == null) {
			return ReturnUtil.getFailReturnStr("没有找到该job!");
		}
		return ReturnUtil.getSuccessReturnStr(kettleJob.getStatus());
	}

	@Override
	public String getAllKettleJobs() {
		Properties kettlejobs = getEngine().getKettleJobs();
		return ReturnUtil.getSuccessReturnStr(kettlejobs);
	}

	@Override
	public String getRunLog() {
		return null;
	}

	public NativeEngine getEngine() {
		return engine;
	}

	private void setEngine(NativeEngine engine) {
		this.engine = engine;
	}

}
