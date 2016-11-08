package com.snail.kettle.scheduler.handlers;

import org.apache.log4j.Logger;

import com.snail.common.mail.MailEngine;
import com.snail.feedback.bug.bean.BugFeedback;
import com.snail.feedback.bug.mail.BugFeedBackMailMessageBuilder;
import com.snail.util.LogUtil;

/**
 * 
 * <p>
 * Title: com.snail.kettle.scheduler.handlers.DefaultErrorHandler
 * </p>
 * 
 * <p>
 * Description: 流程错误默认处理.默认发送提示邮件到管理员,并记录日志
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
public class DefaultErrorHandler implements KettleJobHandler {

	private static Logger log = Logger.getLogger(DefaultErrorHandler.class);

	private BugFeedback jobRunErrorFeedBack;

	public DefaultErrorHandler() {
	}

	public DefaultErrorHandler(BugFeedback jobRunErrorFeedBack) {
		this.setJobRunErrorFeedBack(jobRunErrorFeedBack);
	}

	@Override
	public void handle() {
		if (getJobRunErrorFeedBack() == null) {
			LogUtil.errorLog(log, "kettle任务执行异常反馈信息未准备");
			return;
		}

		BugFeedBackMailMessageBuilder builder = new BugFeedBackMailMessageBuilder(
				getJobRunErrorFeedBack());
		MailEngine.getInstance().sendMail(builder.build());

		LogUtil.errorLog(log, getJobRunErrorFeedBack().getBugDesc()
				.getTextDesc());
	}

	public BugFeedback getJobRunErrorFeedBack() {
		return jobRunErrorFeedBack;
	}

	public void setJobRunErrorFeedBack(BugFeedback jobRunErrorFeedBack) {
		this.jobRunErrorFeedBack = jobRunErrorFeedBack;
	}

}
