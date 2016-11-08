package com.snail.feedback.bug.bean;

import java.util.List;

/**
 * bug反馈:
 * 
 * bug提出人(presenter) +
 * 
 * bug对象(bug) +
 * 
 * 一组问题处理人(solvers)+
 * 
 * 一组观察者(solvers) +
 * 
 * 处理过程(bugProcess)
 * 
 * @author Snail
 * 
 */
public class BugFeedback {
	/**
	 * 已经取消.由用户取消的
	 */
	public static final String BUG_FEEDBACK_STATUS_CANCEL = "0";
	/**
	 * 创建.用户已经提交了bug
	 */
	public static final String BUG_FEEDBACK_STATUS_CREATED = "1";
	/**
	 * 处理中.已经开始处理,但还没处理完成
	 */
	public static final String BUG_FEEDBACK_STATUS_HANDLING = "2";
	/**
	 * 挂起.bug处理人员已经确认了此bug,但暂时没有时间处理
	 */
	public static final String BUG_FEEDBACK_STATUS_SUSPEND = "3";
	/**
	 * 已经完成
	 */
	public static final String BUG_FEEDBACK_STATUS_END = "9";
	/**
	 * bug编号
	 */
	private String bugNo;
	/**
	 * bug名称
	 */
	private String bugName;
	/**
	 * 主持人.即提交bug的人
	 */
	private Presenter presenter;
	/**
	 * bug描述
	 */
	private BugDesc bugDesc;
	/**
	 * 解决者,bug的解决者
	 */
	private List<Solver> solvers;
	/**
	 * bug观察者
	 */
	private List<Observer> observers;
	/**
	 * bug的处理过程
	 */
	private BugProcess bugProcess;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * bug创建时间
	 */
	private Long createTime;
	/**
	 * bug处理完成时间
	 */
	private Long endTime;

	/**
	 * 主持人.即提交bug的人
	 * 
	 * @return
	 */
	public Presenter getPresenter() {
		return presenter;
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public List<Solver> getSolvers() {
		return solvers;
	}

	public void setSolvers(List<Solver> solvers) {
		this.solvers = solvers;
	}

	public List<Observer> getObservers() {
		return observers;
	}

	public void setObservers(List<Observer> observers) {
		this.observers = observers;
	}

	public BugDesc getBugDesc() {
		return bugDesc;
	}

	public void setBugDesc(BugDesc bugDesc) {
		this.bugDesc = bugDesc;
	}

	public BugProcess getBugProcess() {
		return bugProcess;
	}

	public void setBugProcess(BugProcess bugProcess) {
		this.bugProcess = bugProcess;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBugNo() {
		return bugNo;
	}

	public void setBugNo(String bugNo) {
		this.bugNo = bugNo;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getBugName() {
		return bugName;
	}

	public void setBugName(String bugName) {
		this.bugName = bugName;
	}

}
