package com.snail.common;

/**
 * 
 * <p>
 * Title: com.snail.common.SPException
 * </p>
 * 
 * <p>
 * Description: 异常基类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月13日
 * 
 * @version 1.0
 *
 */
public class SPException extends Exception {

	private static final long serialVersionUID = -519492280548376824L;
	
	private int errorCode = 0;

	public SPException() {
		super();
	}

	public SPException(String message, Throwable cause) {
		super(message, cause);
	}

	public SPException(String message) {
		super(message);
	}

	public SPException(Throwable cause) {
		super(cause);
	}

	public SPException(int errorCode) {
		super();
		this.setErrorCode(errorCode);
	}

	public SPException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.setErrorCode(errorCode);
	}

	public SPException(int errorCode, String message) {
		super(message);
		this.setErrorCode(errorCode);
	}

	public SPException(int errorCode, Throwable cause) {
		super(cause);
		this.setErrorCode(errorCode);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
