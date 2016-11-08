package com.snail.feedback.bug.bean;

import java.util.List;
/**
 * 处理结果
 * 
 * @author Snail
 *
 */
public class HandleResult {
	/**
	 * 处理成功
	 */
	private boolean success = true;
	/**
	 * 处理结果文本描述
	 */
	private String textDesc;
	/**
	 * 处理结果附加描述
	 */
	private List<String> attachment;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

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

}
