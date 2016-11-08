package com.snail.common.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import org.apache.log4j.Logger;

import com.snail.common.SPException;
import com.snail.util.LogUtil;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.common.mail.MailEngine
 * </p>
 * 
 * <p>
 * Description: 邮件引擎
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
public class MailEngine {
	
	/**
	 * 126邮箱的文件夹名称
	 */
	public static final String MAIL_FOLDERS_126 = "INBOX,草稿箱,已发送,已删除,垃圾邮件,广告邮件,订阅邮件";
	
	private static Logger log = Logger.getLogger(MailEngine.class);

	private static MailEngine mailEngine = null;

	private String configFilePath = "config/mail.properties";

	private Session session;

	private Properties mailSessionConf;

	private String defaultFromAddress = "projfeedback@126.com";

	private MailEngine() throws Exception {
		build();
	}

	/**
	 * 构建邮件收发器.
	 * 
	 * @throws Exception
	 */
	public void build() throws Exception {
		InputStream inputStream = MailEngine.class.getClassLoader()
				.getResourceAsStream(getConfigFilePath());
		mailSessionConf = new Properties();
		try {
			mailSessionConf.load(inputStream);
		} catch (IOException e) {
			LogUtil.errorLog(log, "mail config file load fail!", e);
			throw new SPException(
					"邮件配置文件加载失败,请确认是否有配置classpath:config/mail.properties!", e);
		}
		setSession(Session.getInstance(mailSessionConf, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailSessionConf
						.getProperty("mail.userName"), mailSessionConf
						.getProperty("mail.password"));
			}
		}));
		if (mailSessionConf.contains("mail.default.from")) {
			setDefaultFromAddress(mailSessionConf
					.getProperty("mail.default.from"));
		}
		if (StringUtils.isBlank(getDefaultFromAddress())) {
			setDefaultFromAddress("projfeedback@126.com");
		}
	}

	public synchronized static MailEngine getInstance() {
		try {
			if (mailEngine == null) {
				mailEngine = new MailEngine();
			}
		} catch (Exception e) {
			LogUtil.errorLog(log, e.getMessage());
		}
		return mailEngine;
	}

	/**
	 * 发送邮件.
	 * 
	 * @param mailMessage
	 */
	public void sendMail(Message mailMessage) {
		try {
			Transport.send(mailMessage);
		} catch (Exception e) {
			LogUtil.errorLog(log, "邮件发送失败!", e);
		}
	}

	/**
	 * 收邮件.从收件夹中接收邮件,只读
	 * 
	 * @return
	 * @throws Exception
	 */
	public Message[] reciveMail(String reciveType) throws Exception {
		if ("IMAP".equalsIgnoreCase(reciveType)) {
			return reciveMailByImap();
		} else {
			return reciveMailByPop3();
		}
	}

	/**
	 * 使用IMAP方式收邮件.
	 * 
	 * @return
	 * @throws Exception
	 */
	private Message[] reciveMailByImap() throws Exception {
		Folder folder = getFolderByImap("INBOX");
		folder.open(Folder.READ_ONLY);
		return folder.getMessages();
	}
	

	private Folder getFolderByImap(String folderName) throws Exception{
		MailConfig mailConfig = MailConfig.createInstance();
		Properties props = System.getProperties();
		props.setProperty("mail.imap.port", mailConfig.getImapPort());
		props.setProperty("mail.debug", mailConfig.getMailDebug());
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imap");
		store.connect(mailConfig.getImapHost(), mailConfig.getUserName(),
				mailConfig.getUserPwd());
		return store.getFolder(folderName);
	} 

	/**
	 * 使用POP3方式收邮件.
	 * 
	 * @return
	 * @throws Exception
	 */
	private Message[] reciveMailByPop3() throws Exception {
		Folder folder = getFolderByPop3("INBOX");
		folder.open(Folder.READ_ONLY);
		return folder.getMessages();
	}
	
	private Folder getFolderByPop3(String folderName) throws Exception{
		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("pop3");
		MailConfig mailConfig = MailConfig.createInstance();
		store.connect(mailConfig.getPop3Host(), mailConfig.getUserName(),
				mailConfig.getUserPwd());
		return store.getFolder(folderName);
	}
	/**
	 * 获得邮件服务器上的文件夹.
	 * @param type 获取方式.(imap or pop3)
	 * @param folderName 文件夹名称.
	 * @return
	 * @throws Exception
	 */
	public Folder getFolder(String type,String folderName) throws Exception{
		if ("IMAP".equalsIgnoreCase(type)) {
			return getFolderByImap(folderName);
		} else {
			return getFolderByPop3(folderName);
		}
	}

	public static void main(String[] args) throws Exception {
		MailConfig mailConfig = MailConfig.createInstance();
		Properties props = System.getProperties();
		props.setProperty("mail.imap.port", mailConfig.getImapPort());
		props.setProperty("mail.debug", mailConfig.getMailDebug());
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imap");
		store.connect(mailConfig.getImapHost(), mailConfig.getUserName(),
				mailConfig.getUserPwd());
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);
		List<Message>  notViewedMessages = new ArrayList<Message>();
		Message[] messages = folder.getMessages();
		for(Message message : messages){
			if(message.getFlags().contains(Flag.SEEN)){
				continue;
			}
			notViewedMessages.add(message);
			try {
				message.setFlag(Flags.Flag.SEEN, true);
				message.saveChanges();
			} catch (Exception e) {
				System.out.println("出错了~!");
			}
		}
		System.out.println(notViewedMessages.size()+"/"+messages.length);
	}
	
	/**
	 * 默认发件人
	 * 
	 * @return
	 */
	public String getDefaultFromAddress() {
		return defaultFromAddress;
	}

	public Session getSession() {
		return session;
	}

	private void setSession(Session session) {
		this.session = session;
	}

	public Properties getMailSessionConf() {
		return mailSessionConf;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setDefaultFromAddress(String defaultFromAddress) {
		this.defaultFromAddress = defaultFromAddress;
	}
}
