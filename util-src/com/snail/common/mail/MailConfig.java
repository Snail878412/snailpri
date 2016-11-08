package com.snail.common.mail;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.snail.util.LogUtil;
import com.snail.util.ProUtils;

/**
 * 
 * <p>
 * Title: com.snail.common.mail.MailConfig
 * </p>
 * 
 * <p>
 * Description: 邮件配置
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月14日
 * 
 * @version 1.0
 * 
 */
public class MailConfig {
	private static Logger log = Logger.getLogger(MailConfig.class);

	private String configFilePath = "/config/mail.properties";

	private String userName;
	private String userPwd;

	private String mailDebug;
	private String transportProtocol;

	private String fromAddress;
	private String fromName;

	private String imapHost;
	private String imapPort;

	private String smtpHost;
	private String smtpPort;

	private String pop3Host;
	private String pop3Port;

	public MailConfig() {
	}

	public static MailConfig createInstance() {
		MailConfig instance = new MailConfig();
		instance.init();
		return instance;
	}

	public void init() {
		Properties mailConf = null;
		try {
			mailConf = ProUtils.createProperties(getConfigFilePath());
		} catch (IOException e) {
			LogUtil.errorLog(log, "mail config file load fail!");
		}
		if (mailConf == null) {
			return;
		}
		// 发件人名称
		if (mailConf.containsKey("mail.default.from.name")) {
			setFromName(mailConf.getProperty("mail.default.from.name"));
		}
		// 用户名
		if (mailConf.containsKey("mail.userName")) {
			setUserName(mailConf.getProperty("mail.userName"));
		}
		// 密码
		if (mailConf.containsKey("mail.password")) {
			setUserPwd(mailConf.getProperty("mail.password"));
		}
		// 发件人邮箱地址
		if (mailConf.containsKey("mail.default.from")) {
			setFromAddress(mailConf.getProperty("mail.default.from"));
		}
		// SMTP HOST
		if (mailConf.containsKey("mail.smtp.host")) {
			setSmtpHost(mailConf.getProperty("mail.smtp.host"));
		}
		// SMTP PORT
		if (mailConf.containsKey("mail.smtp.port")) {
			setSmtpPort(mailConf.getProperty("mail.smtp.port"));
		}
		// IMAP HOST
		if (mailConf.containsKey("mail.imap.host")) {
			setImapHost(mailConf.getProperty("mail.imap.host"));
		}
		// IMAP PORT
		if (mailConf.containsKey("mail.imap.port")) {
			setImapPort(mailConf.getProperty("mail.imap.port"));
		}
		// 传递协议
		if (mailConf.containsKey("mail.transport.protocol")) {
			setTransportProtocol(mailConf
					.getProperty("mail.transport.protocol"));
		}
		// 调整模式
		if (mailConf.containsKey("mail.debug")) {
			setMailDebug(mailConf.getProperty("mail.debug"));
		}

		// 传递协议
		if (mailConf.containsKey("mail.pop3.host")) {
			setPop3Host(mailConf
					.getProperty("mail.pop3.host"));
		}
		// 调整模式
		if (mailConf.containsKey("mail.pop3.port")) {
			setPop3Port(mailConf.getProperty("mail.pop3.port"));
		}
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getMailDebug() {
		return mailDebug;
	}

	public void setMailDebug(String mailDebug) {
		this.mailDebug = mailDebug;
	}

	public String getTransportProtocol() {
		return transportProtocol;
	}

	public void setTransportProtocol(String transportProtocol) {
		this.transportProtocol = transportProtocol;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getImapHost() {
		return imapHost;
	}

	public void setImapHost(String imapHost) {
		this.imapHost = imapHost;
	}

	public String getImapPort() {
		return imapPort;
	}

	public void setImapPort(String imapPort) {
		this.imapPort = imapPort;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getPop3Host() {
		return pop3Host;
	}

	public void setPop3Host(String pop3Host) {
		this.pop3Host = pop3Host;
	}

	public String getPop3Port() {
		return pop3Port;
	}

	public void setPop3Port(String pop3Port) {
		this.pop3Port = pop3Port;
	}
}
