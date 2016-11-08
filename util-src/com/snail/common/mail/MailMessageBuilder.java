package com.snail.common.mail;

import javax.mail.Message;
/**
 * 
 * <p>
 * Title: com.snail.common.mail.MailMessageBuilder
 * </p>
 * 
 * <p>
 * Description: email Message 构造器接口
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
public interface MailMessageBuilder {
	
	/**
	 * 构造Message
	 * @return
	 */
	public Message build();
}
