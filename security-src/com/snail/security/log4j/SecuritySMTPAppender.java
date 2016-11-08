package com.snail.security.log4j;

import org.apache.log4j.net.SMTPAppender;

import com.snail.security.asymmetric.RsaProcessor;

/**
 * 
 * <p>
 * Title: com.snail.common.log4j.SecuritySMTPAppender
 * </p>
 * 
 * <p>
 * Description 安全的SMTPAppender
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014-9-9
 * 
 * @version 1.0
 * 
 */
public class SecuritySMTPAppender extends SMTPAppender {

	@Override
	public void setSMTPPassword(String password) {
		super.setSMTPPassword(RsaProcessor.getInstance().decrypt(password));
	}
}
