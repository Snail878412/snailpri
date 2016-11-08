package com.snail.kettle.scheduler.listeners;

import java.util.ArrayList;
import java.util.List;

import com.snail.feedback.bug.bean.BugDesc;
import com.snail.feedback.bug.bean.BugFeedback;
import com.snail.feedback.bug.bean.Presenter;
import com.snail.feedback.bug.bean.Solver;
import com.snail.kettle.scheduler.handlers.DefaultErrorHandler;
import com.snail.kettle.scheduler.handlers.KettleJobHandler;

/**
 * 
 * <p>
 * Title: com.snail.kettle.scheduler.listeners.AbstractErrorListener
 * </p>
 * 
 * <p>
 * Description: KETTL任务错误监控器抽象类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年9月1日
 * 
 * @version 1.0
 * 
 */
public abstract class AbstractErrorListener {
	private BugFeedback kettleJobErrorFeedBack = new BugFeedback();

	private KettleJobHandler errorHandler;

	public AbstractErrorListener() {
		initBugFeedback();
	};

	private void initBugFeedback() {
		getKettleJobErrorFeedBack().setBugName("Kettle任务执行异常");
		Presenter presenter = new Presenter();
		presenter.setName("Native Kettle Job Scheduler Engine");
		presenter.setEmail("shui878412@126.com");
		getKettleJobErrorFeedBack().setPresenter(presenter);

		List<Solver> solvers = new ArrayList<Solver>();
		Solver solver = new Solver();
		solver.setEmail("shui878412@126.com");
		solvers.add(solver);
		getKettleJobErrorFeedBack().setSolvers(solvers);
	}

	protected void runErrorWarring(String errorInfo) {
		BugDesc bugDesc = new BugDesc();
		bugDesc.setPriority(BugDesc.BUG_PRIORITY_SERIOUS);
		bugDesc.setTextDesc(errorInfo);
		BugFeedback bugFeedback = getKettleJobErrorFeedBack();
		bugFeedback.setBugDesc(bugDesc);

		if (getErrorHandler() == null) {
			setErrorHandler(new DefaultErrorHandler(bugFeedback));
		}
		getErrorHandler().handle();
	}

	public BugFeedback getKettleJobErrorFeedBack() {
		return kettleJobErrorFeedBack;
	}

	public void setKettleJobErrorFeedBack(BugFeedback kettleJobErrorFeedBack) {
		this.kettleJobErrorFeedBack = kettleJobErrorFeedBack;
	}

	public KettleJobHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(KettleJobHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
}
