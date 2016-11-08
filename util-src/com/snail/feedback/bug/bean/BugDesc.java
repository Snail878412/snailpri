package com.snail.feedback.bug.bean;

import java.util.List;

/**
 * bug描述
 * 
 * @author Snail
 * 
 */
public class BugDesc {

	/**
	 * 毁灭性的
	 */
	public static final Integer BUG_PRIORITY_FATAL = 0;
	/**
	 * 非常严重的
	 */
	public static final Integer BUG_PRIORITY_VERY_SERIOUS = 10;
	/**
	 * 严重的
	 */
	public static final Integer BUG_PRIORITY_SERIOUS = 20;
	/**
	 * 一般性的
	 */
	public static final Integer BUG_PRIORITY_GENERIC = 30;
	/**
	 * 可延期的
	 */
	public static final Integer BUG_PRIORITY_DEFERABLE = 40;
	/**
	 * 建议性的
	 */
	public static final Integer BUG_PRIORITY_SUGGESTIVE = 50;
	/**
	 * bug文本描述
	 */
	private String textDesc;
	/**
	 * bug辅助描述附件
	 */
	private List<String> attachment;
	/**
	 * 优先级.即重要性/严重度.
	 */
	private Integer priority;

	public String getTextDesc() {
		return textDesc;
	}

	public void setTextDesc(String textDesc) {
		this.textDesc = textDesc;
	}

	public List<String> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<String> attachment) {
		this.attachment = attachment;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
