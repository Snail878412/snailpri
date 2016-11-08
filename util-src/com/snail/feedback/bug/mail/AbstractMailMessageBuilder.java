package com.snail.feedback.bug.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;

import com.snail.common.mail.MailConfig;
import com.snail.common.mail.MailEngine;
import com.snail.common.mail.MailMessageBuilder;
import com.snail.util.LogUtil;

public abstract class AbstractMailMessageBuilder implements MailMessageBuilder {

	private static Logger log = Logger
			.getLogger(AbstractMailMessageBuilder.class);

	private Session session;

	private InternetAddress fromAddress;

	private MailConfig mailConfig;

	public AbstractMailMessageBuilder() {
		mailConfig = new MailConfig();
		session = MailEngine.getInstance().getSession();
		try {
			setFromAddress(new InternetAddress(MailEngine.getInstance()
					.getDefaultFromAddress(), mailConfig.getFromName()));
		} catch (UnsupportedEncodingException e) {
			LogUtil.errorLog(log, e.getMessage(), e);
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public abstract Message build();

	public InternetAddress getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(InternetAddress fromAddress) {
		this.fromAddress = fromAddress;
	}

	public MailConfig getMailConfig() {
		return mailConfig;
	}

	public void setMailConfig(MailConfig mailConfig) {
		this.mailConfig = mailConfig;
	}

}
