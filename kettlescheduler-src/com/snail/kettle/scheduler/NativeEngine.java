package com.snail.kettle.scheduler;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.elasticsearch.common.io.PatternFilenameFilter;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.snail.common.SPException;
import com.snail.kettle.scheduler.beans.KettleJob;
import com.snail.rmi.RmiServerContext;
import com.snail.util.LogUtil;
import com.snail.util.ProUtils;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.kettle.scheduler.NativeEngine
 * </p>
 * 
 * <p>
 * Description:本地kettle任务调度引擎.
 * <li>使用add/addAll方法向调度引擎中添加kettle任务(job/Trans)</li>
 * <li>使用start/startAll启动调度引擎中的kettle任务(job/Trans)</li>
 * <li>使用getKettleJobs查询调度引擎中的kettle任务(job/Trans)</li>
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
@Component("nativeKettleJobEngine")
@Scope("singleton")
public class NativeEngine {

	private static final PatternFilenameFilter KETTLE_FILENAME_FILTER = new PatternFilenameFilter(
			KettleJob.KETTLE_FILENAME_REGEX);

	private static Logger log = Logger.getLogger(NativeEngine.class);

	private static RmiServerContext rmiServer;

	private static NativeEngine engine;

	private String jobHome = "";

	private boolean autoLoadJob = false;

	private static Properties kettleJobs = new Properties();

	private NativeEngine() throws KettleException {
		init();
	};

	private void init() throws KettleException {
		LogUtil.debugLog(log, "开始启动KETLLTE任务调度引擎...");
		String jobHome = ProUtils.getPropertyValue("JOB_HOME");
		if (StringUtils.isNotBlank(jobHome)) {
			File homeDir = new File(jobHome);
			if(!homeDir.exists()){
				homeDir.mkdirs();
			}
			this.jobHome = jobHome;
		}
		if(StringUtils.isBlank(jobHome)){
			File homeDir = new File("kettleJobs");
			if(!homeDir.exists()){
				homeDir.mkdir();
			}
			jobHome = homeDir.getAbsolutePath();
		}
		LogUtil.debugLog(log, "KETTLE任务根目录:" + this.jobHome);
		KettleEnvironment.init();
		String autoLoadJob = ProUtils.getPropertyValue("AUTO_LOAD_JOB");
		if (StringUtils.isNotBlank(autoLoadJob)
				&& "true".equalsIgnoreCase(autoLoadJob)) {
			this.autoLoadJob = true;
		}
		LogUtil.debugLog(log, "开始加载KETTLE任务...");
		autoLoadJob();
		LogUtil.debugLog(log, "KETTLE任务加载完毕!");
	}

	/**
	 * 自动加载目录中的kettle文件.<br/>
	 * <li>1.根目录下的所有任务文件(包括xml,ktr,kjb文件);</li><br/>
	 * <li>2.根目录下一级子目录中的所有的任务文件(包括xml,ktr,kjb文件).</li>
	 */
	private void autoLoadJob() {
		if (StringUtils.isBlank(this.jobHome) || !this.autoLoadJob) {
			// 没有根路径或没有显示设置需要自动加载,则直接返回.
			LogUtil.debugLog(log, "取消自动加载:没有根路径或不需要自动加载.");
			return;
		}
		File kettleJobRootDir = new File(this.jobHome);
		if (!kettleJobRootDir.exists() || !kettleJobRootDir.isDirectory()) {
			// 根目录不存在或不是目录,则直接退出.
			LogUtil.debugLog(log, "取消自动加载:指定的根目录不存在或不是目录.");
			return;
		}
		File[] kettleJobFiles = kettleJobRootDir
				.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File parentFile, String fileName) {
						File file = new File(parentFile.getAbsoluteFile()
								+ File.separator + fileName);
						if (file.isDirectory()) {
							return file.list(KETTLE_FILENAME_FILTER).length > 0;
						}
						return Pattern.matches(KettleJob.KETTLE_FILENAME_REGEX,
								fileName);
					}
				});
		if (kettleJobFiles.length < 1) {
			// 目录下没有KETTLE任务文件,直接退出
			LogUtil.debugLog(log, "取消自动加载:没有有效的KETTLE任务文件.");
			return;
		}
		for (File kettleJobFile : kettleJobFiles) {
			add(kettleJobFile);
		}
	}

	private void add(File kettleJobFile) {
		String fileName = "";
		if (kettleJobFile == null || !kettleJobFile.exists()) {
			return;
		}
		if (kettleJobFile.isFile() && !kettleJobFile.canRead()) {
			LogUtil.debugLog(log, "文件不可读:" + kettleJobFile.getAbsolutePath());
			return;
		}
		try {
			if (kettleJobFile.isFile()) {
				add(new KettleJob(kettleJobFile.getAbsolutePath(), false));
				return;
			}
			File[] subFiles = kettleJobFile.listFiles();
			for (File subFile : subFiles) {
				if (subFile == null || !subFile.exists()
						|| subFile.isDirectory()) {
					continue;
				}
				if (!subFile.canRead()) {
					LogUtil.debugLog(log, "文件不可读:" + subFile.getAbsolutePath());
					continue;
				}
				add(new KettleJob(subFile.getAbsolutePath(), false));
			}

		} catch (SPException e) {
			LogUtil.debugLog(log,
					"加载KETTLE任务失败[" + fileName + "]:" + e.getMessage());
		}
	}

	/**
	 * 在动加载KETTLE任务逻辑:<br/>
	 * 若AUTO_LOAD_JOB设置为true,则自动从JOB_HOME(默认目录为:当前目录/kettleJobs)目录中查找KETTELE任务文件并加载(懒加载).<br/>
	 * <li>1.根目录下的所有任务文件(包括xml,ktr,kjb文件);</li> <br/>
	 * <li>2.根目录下一级子目录中的所有的任务文件(包括xml,ktr,kjb文件).</li>
	 * 
	 * @return
	 * @throws KettleException
	 */
	public static synchronized NativeEngine getInstance()
			throws KettleException {
		if (engine == null) {
			engine = new NativeEngine();
		}
		if (rmiServer == null) {
			rmiServer = RmiServerContext.getInstance();
			rmiServer.startRmiServer();
		}

		return engine;
	}

	/**
	 * 向容器中添加kettle Job
	 * 
	 * @param kttleJob
	 */
	public void add(KettleJob kttleJob) {
		getKettleJobs().put(kttleJob.getJobNo(), kttleJob);
	}

	/**
	 * 向容器中添加 kettle Job
	 * 
	 * @param fileName
	 *            文件的相对路径
	 * @return
	 * @throws SPException
	 */
	public KettleJob add(String fileName) throws SPException {
		KettleJob kttleJob = new KettleJob(getReallyPath(fileName), false);
		add(kttleJob);
		return kttleJob;
	}

	/**
	 * 从容器中移除任务.
	 * 
	 * @param kettleJobNo
	 * @return
	 * @throws SPException
	 */
	public boolean remove(String kettleJobNo) throws SPException {
		getKettleJobs().remove(kettleJobNo);
		return true;
	}

	/**
	 * 获取kettle文件的绝对路径
	 * 
	 * @param fileName
	 * @return
	 */
	private String getReallyPath(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return "";
		}
		return getJobHome() + fileName;
	}

	/**
	 * 启动指定的任务
	 * 
	 * @param jobNo
	 * @return
	 * @throws SPException
	 */
	public boolean start(String jobNo) throws SPException {
		if (!kettleJobs.containsKey(jobNo)) {
			return false;
		}
		Object obj = kettleJobs.get(jobNo);
		if (obj == null || !(obj instanceof KettleJob)) {
			return false;
		}
		((KettleJob) obj).start(false);
		return true;
	}

	/**
	 * 启动所有的任务
	 * 
	 * @return
	 * @throws SPException
	 */
	public boolean startAll() throws SPException {
		if (kettleJobs == null || kettleJobs.isEmpty()) {
			return false;
		}
		for (Object key : kettleJobs.keySet()) {
			start(key.toString());
		}
		return true;
	}

	/**
	 * 将Properties中的都添加到容器中.
	 * 
	 * @param kettleJobs
	 */
	public void addAll(Properties kettleJobs) {
		if (kettleJobs == null || kettleJobs.isEmpty()) {
			return;
		}
		for (Object key : kettleJobs.keySet()) {
			getKettleJobs().put(key, kettleJobs.get(key));
		}
	}

	/**
	 * 将map中的都添加到容器中
	 * 
	 * @param kettleJobs
	 */
	public void addAll(Map<String, KettleJob> kettleJobs) {
		if (kettleJobs == null || kettleJobs.isEmpty()) {
			return;
		}
		for (String key : kettleJobs.keySet()) {
			getKettleJobs().put(key, kettleJobs.get(key));
		}
	}

	/**
	 * 返回指定的任务
	 * 
	 * @param kettleJobNo
	 * @return
	 */
	public KettleJob get(String kettleJobNo) {
		return (KettleJob) kettleJobs.get(kettleJobNo);
	}

	public Properties getKettleJobs() {
		return kettleJobs;
	}

	public void setKettleJobs(Properties kettleJobs) {
		NativeEngine.kettleJobs = kettleJobs;
	}

	public String getJobHome() {
		return jobHome;
	}

	public void setJobHome(String jobHome) {
		this.jobHome = jobHome;
	}

}
