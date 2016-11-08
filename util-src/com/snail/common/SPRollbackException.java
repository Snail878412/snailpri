package com.snail.common;

/**
 * 
 * <p>
 * Title: com.snail.common.SPRollbackException
 * </p>
 * 
 * <p>
 * Description: 事务回滚异常
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
public class SPRollbackException extends SPException {

	private static final long serialVersionUID = 1077073749731918874L;

	public SPRollbackException() {
		super();
	}

	public SPRollbackException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

	public SPRollbackException(int errorCode, String message) {
		super(errorCode, message);
	}

	public SPRollbackException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public SPRollbackException(int errorCode) {
		super(errorCode);
	}

	public SPRollbackException(String message, Throwable cause) {
		super(message, cause);
	}

	public SPRollbackException(String message) {
		super(message);
	}

	public SPRollbackException(Throwable cause) {
		super(cause);
	}

}
