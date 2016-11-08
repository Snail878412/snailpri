package com.snail.kettle.scheduler.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobEntryListener;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransListener;
import org.pentaho.di.trans.TransMeta;

import com.snail.common.SPException;
import com.snail.kettle.scheduler.listeners.JobErrorListener;
import com.snail.kettle.scheduler.listeners.TransErrorListener;
import com.snail.util.LogUtil;
import com.snail.util.StringUtils;
import com.snail.util.XmlUtil;

/**
 * 
 * <p>
 * Title: com.snail.kettle.scheduler.beans.KettleJob
 * </p>
 * 
 * <p>
 * Description: kettle任务bean
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
public class KettleJob implements Serializable {

	private static final long serialVersionUID = 8400882755188637482L;

	private static Logger log = Logger.getLogger(KettleJob.class);

	public static final int TYPE_JOB = 1;
	public static final int TYPE_TRANS = 2;

	public static final String KETTLE_FILENAME_REGEX = ".*\\.((xml)|(ktr)|(kjb))$";
	
	private static final String KJB_FILE_REGEX = "^.*\\.kjb$";
	private static final String FILENAME_ENDWITH_KTR_FILE_REGEX = "^.*\\.ktr$";
	private static final String FILENAME_ENDWITH_XML_FILE_REGEX = "^.*\\.xml$";

	private int type = TYPE_JOB;

	private String jobNo;

	private String jobName;

	private Job job;
	private Trans tran;

	private String fileName;

	private boolean builded = false;

	private JobEntryListener jobEntryListener;

	private TransListener transListener;

	public KettleJob() {
	};

	public KettleJob(String fileName, boolean lazy,
			JobEntryListener jobEntryListener) throws SPException {
		this(fileName, lazy);
		this.jobEntryListener = jobEntryListener;
	}

	public KettleJob(String fileName, boolean lazy, TransListener transListener)
			throws SPException {
		this(fileName, lazy);
		this.transListener = transListener;
	}

	public KettleJob(String fileName, boolean lazy) throws SPException {
		this.setFileName(fileName);
		if (lazy) {
			return;
		}
		build();
	}

	public boolean build() throws SPException {
		if (builded) {
			return builded;
		}
		try {
			switch (getTypeFromFileName()) {
			case TYPE_JOB:
				JobMeta jobMeta = new JobMeta(this.getFileName(), null);
				this.job = new Job(null, jobMeta);
				jobMeta.setInternalKettleVariables(job);
				this.type = TYPE_JOB;
				generatorJobNo();
				setJobName(this.job.getName());
				this.builded = true;
				break;
			case TYPE_TRANS:
				TransMeta transMeta = new TransMeta(this.getFileName());
				this.tran = new Trans(transMeta);
				this.type = TYPE_TRANS;
				generatorJobNo();
				setJobName(this.tran.getName());
				this.builded = true;
				break;
			default:
				this.builded = false;
				return false;
			}
			addErrorListener();
		} catch (SPException e){
			LogUtil.errorLog(log, e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LogUtil.errorLog(log, e.getMessage(), e);
			throw new SPException("创建kettle任务失败,请联系系统管理员!");
		}
		return builded;
	}

	private int getTypeFromFileName() throws SPException {
		if (StringUtils.isEmpty(this.getFileName())) {
			throw new SPException("文件名称为空!");
		}
		File kettleFile = new File(this.getFileName());
		if (kettleFile == null || !kettleFile.exists() || !kettleFile.canRead()
				|| !kettleFile.canExecute()) {
			throw new SPException("指定文件不存在或无效,请确认文件存在且可读可执行!");
		}

		if (Pattern.matches(KJB_FILE_REGEX, this.getFileName())) {
			return TYPE_JOB;
		}
		if (Pattern.matches(FILENAME_ENDWITH_KTR_FILE_REGEX, this.getFileName())) {
			return TYPE_TRANS;
		}

		try {
			if (Pattern.matches(FILENAME_ENDWITH_XML_FILE_REGEX, this.getFileName())) {
				InputStream in = new FileInputStream(kettleFile);
				Element root = XmlUtil.getRootElement(in);
				if (root == null) {
					throw new SPException("xml文件错误,没有找到根节点!");
				}

				String rootName = root.getName();
				if ("job".equalsIgnoreCase(rootName)) {
					return TYPE_JOB;
				}
				if ("transformation".equalsIgnoreCase(rootName)) {
					return TYPE_TRANS;
				}
			}
		} catch (Exception e) {
			throw new SPException("xml文件错误,请确认!");
		}

		throw new SPException("未知文件类型");
	}

	private void generatorJobNo() {
		String prefix = "";
		switch (getType()) {
		case TYPE_JOB:
			prefix = "J";
			break;
		case TYPE_TRANS:
			prefix = "T";
			break;
		default:
			prefix = "U";
			break;
		}
		this.jobNo = prefix + StringUtils.getUuid();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) throws SPException {
		throw new SPException("不能手动设置类型");
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) throws SPException {
		throw new SPException("不能手动设置编号");
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) throws SPException {
		throw new SPException("不能手动设置任务");
	}

	public Trans getTran() {
		return tran;
	}

	public void setTran(Trans tran) throws SPException {
		throw new SPException("不能手动设置转换");
	}

	/**
	 * Trans Status :
	 * 
	 * Halting,Finished,Finished (with errors),Paused,Running,Stopped,Preparing
	 * executing,Initializing,Waiting
	 * 
	 * Job Status :
	 * 
	 * Waiting,Halting,Running,Stopped,Finished,Finished (with errors)
	 * 
	 * @return
	 */
	public String getStatus() {
		switch (getType()) {
		case TYPE_JOB:
			return getJob().getStatus();
		case TYPE_TRANS:
			return getTran().getStatus();
		default:
			return null;
		}
	}

	public boolean isBuilded() {
		return builded;
	}

	public void setBuilded(boolean builded) throws SPException {
		throw new SPException("不能手动设置是否构建");
	}

	public boolean start(boolean sync) throws SPException {
		if (!builded) {
			build();
		}
		try {
			switch (getType()) {
			case TYPE_JOB:
				return startJob(sync);
			case TYPE_TRANS:
				return startTrans(sync);
			default:
				return false;
			}
		} catch (KettleException e) {
			LogUtil.errorLog(log, e.getMessage(), e);
			throw new SPException("kettle任务启动失败!");
		}
	}

	public boolean stop() {
		switch (getType()) {
		case TYPE_JOB:
			return stopJob();
		case TYPE_TRANS:
			return stopTran();
		default:
			return false;
		}
	}

	private boolean startJob(boolean sync) throws KettleException {
		getJob().start();
		if (sync) {
			// 同步
			getJob().waitUntilFinished();
		}
		return true;
	}

	private boolean startTrans(boolean sync) throws KettleException {
		getTran().execute(null);
		if (sync) {
			// 同步
			getTran().waitUntilFinished();
		}
		return true;
	}

	private boolean stopJob() {
		getJob().stopAll();
		return getJob().isStopped();
	}

	private boolean stopTran() {
		getTran().stopAll();
		return getTran().isStopped();
	}

	public void addErrorListener() {
		switch (getType()) {
		case TYPE_JOB:
			if (getJobEntryListener() == null) {
				setJobEntryListener(new JobErrorListener());
			}
			getJob().addJobEntryListener(getJobEntryListener());
			return;
		case TYPE_TRANS:
			if (getTransListener() == null) {
				setTransListener(new TransErrorListener());
			}
			getTran().addTransListener(getTransListener());
		default:
			return;
		}
	}

	public JobEntryListener getJobEntryListener() {
		return jobEntryListener;
	}

	public void setJobEntryListener(JobEntryListener jobEntryListener) {
		this.jobEntryListener = jobEntryListener;
	}

	public TransListener getTransListener() {
		return transListener;
	}

	public void setTransListener(TransListener transListener) {
		this.transListener = transListener;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
