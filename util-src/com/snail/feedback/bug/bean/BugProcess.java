package com.snail.feedback.bug.bean;

/**
 * Bug的处理过程
 * 
 * @author Snail
 * 
 */
public class BugProcess {
	/**
	 * 取消
	 */
	public static final String BUG_PROCESS_TYPE_CANCEL = "0";
	/**
	 * 创建
	 */
	public static final String BUG_PROCESS_TYPE_CREATE = "1";
	/**
	 * 处理中
	 */
	public static final String BUG_PROCESS_TYPE_HANDLING = "2";
	/**
	 * 结束
	 */
	public static final String BUG_PROCESS_TYPE_END = "2";

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Long processTime) {
		this.processTime = processTime;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public HandleResult getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(HandleResult handleResult) {
		this.handleResult = handleResult;
	}

	public BugProcess getLastProcess() {
		return lastProcess;
	}

	public void setLastProcess(BugProcess lastProcess) {
		this.lastProcess = lastProcess;
	}

	/**
	 * 处理人
	 */
	private Contact contact;

	/**
	 * 处理时间
	 */
	private Long processTime;

	/**
	 * 处理类型
	 */
	private String processType;

	/**
	 * 处理结果
	 */
	private HandleResult handleResult;

	/**
	 * 历史处理过程
	 */
	private BugProcess lastProcess;
}
