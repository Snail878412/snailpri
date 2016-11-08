package com.snail.common;

/**
 * 
 * <p>
 * Title: com.snail.common.SPNoRollbackException
 * </p>
 * 
 * <p>
 * Description: 事务不回滚异常
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
public class SPNoRollbackException extends SPException {
	private static final long serialVersionUID = 4843315099185553424L;

	public SPNoRollbackException() {
		super();
	}

	public SPNoRollbackException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

	public SPNoRollbackException(int errorCode, String message) {
		super(errorCode, message);
	}

	public SPNoRollbackException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public SPNoRollbackException(int errorCode) {
		super(errorCode);
	}

	public SPNoRollbackException(String message, Throwable cause) {
		super(message, cause);
	}

	public SPNoRollbackException(String message) {
		super(message);
	}

	public SPNoRollbackException(Throwable cause) {
		super(cause);
	}

}
