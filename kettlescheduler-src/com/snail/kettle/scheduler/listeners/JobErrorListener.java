package com.snail.kettle.scheduler.listeners;

import java.util.Date;
import java.util.List;

import org.pentaho.di.core.Result;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobEntryListener;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.entries.special.JobEntrySpecial;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.job.entry.JobEntryInterface;

import com.snail.util.DateUtil;

/**
 * 
 * <p>
 * Title: com.snail.kettle.scheduler.listeners.JobErrorListener
 * </p>
 * 
 * <p>
 * Description: job错误处理监听器
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
public class JobErrorListener extends AbstractErrorListener implements JobEntryListener {

	@Override
	public void afterExecution(Job job, JobEntryCopy jobEntryCopy,
			JobEntryInterface jobEntryInterface, Result result) {
		if (result.getNrErrors() < 1) {
			return;
		}

		// 若流程中已经有错误处理分支或无条件分支,且不是特殊步骤(如空步骤),则不需要停止job
		List<JobHopMeta> hops = job.getJobMeta().getJobhops();
		boolean stop = true;
		for (JobHopMeta hop : hops) {
			if (hop.getFromEntry().equals(jobEntryCopy)
					&& (!hop.getEvaluation() || hop.isUnconditional())
					&&!(hop.getToEntry().getEntry() instanceof JobEntrySpecial)) {
				stop = false;
				break;
			}
		}
		if (stop) {
			StringBuffer errorInfo = new StringBuffer();
			errorInfo.append("<p>Kettle任务执行过程中有一个步骤出错了,且该步骤没有设置错误处理分支,请及时处理!</p>");
			errorInfo.append("<li>任务ID:"+job.getId()+"</li>");
			errorInfo.append("<li>任务名称:"+job.getJobname()+"</li>");
			errorInfo.append("<li>错误步骤名称:"+jobEntryCopy.getName()+"</li>");
			errorInfo.append("<li>执行时间:"+DateUtil.getFormatDateTime(new Date())+"</li>");
			errorInfo.append("<p>更多详情请参见日志文件</p>");
			runErrorWarring(errorInfo.toString());
			job.stopAll();
		}
	}

	@Override
	public void beforeExecution(Job arg0, JobEntryCopy arg1,
			JobEntryInterface arg2) {
	}

}
