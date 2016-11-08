package com.snail.feedback.bug.mail;

import java.io.File;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.snail.feedback.bug.bean.BugDesc;
import com.snail.feedback.bug.bean.BugFeedback;
import com.snail.feedback.bug.bean.Observer;
import com.snail.feedback.bug.bean.Solver;
import com.snail.util.LogUtil;
import com.snail.util.StringUtils;

public class BugFeedBackMailMessageBuilder extends AbstractMailMessageBuilder {

	private static final Logger log = Logger
			.getLogger(BugFeedBackMailMessageBuilder.class);

	private BugFeedback bugFeedBack;

	public BugFeedBackMailMessageBuilder(BugFeedback bugFeedBack) {
		this.setBugFeedBack(bugFeedBack);
	}

	public Message build() {
		Message mailMessage = new MimeMessage(getSession());

		try {
			// 设置发送人
			mailMessage.setFrom(getFromAddress());
			String presenterEmail = getBugFeedBack().getPresenter().getEmail();
			if (!StringUtils.isBlank(presenterEmail)) {
				mailMessage
						.setReplyTo(new InternetAddress[] { new InternetAddress(
								presenterEmail) });
			}

			// 设置接收人
			List<Solver> solvers = getBugFeedBack().getSolvers();
			InternetAddress[] toAddress = new InternetAddress[solvers.size()];
			int index = 0;
			for (Solver solver : solvers) {
				toAddress[index++] = new InternetAddress(solver.getEmail());
			}
			mailMessage.addRecipients(Message.RecipientType.TO, toAddress);

			// 设置cc的人员
			List<Observer> observers = getBugFeedBack().getObservers();
			if(observers !=null && !observers.isEmpty()){
				InternetAddress[] ccAddress = new InternetAddress[observers.size()];
				index = 0;
				for (Observer observer : observers) {
					ccAddress[index++] = new InternetAddress(observer.getEmail());
				}
				mailMessage.addRecipients(Message.RecipientType.CC, ccAddress);
			}
			// 设置邮件主题
			String subject = getBugFeedBack().getBugName();
			if (StringUtils.isBlank(subject)) {
				subject = getBugFeedBack().getBugNo();
			}
			if (StringUtils.isBlank(subject)) {
				subject = "系统";
			}
			subject += " BUG 反馈!";
			mailMessage.setSubject(subject);

			// 设置邮件内容
			mailMessage.setContent(buildMailContent());

			// 保存修改
			mailMessage.saveChanges();
		} catch (Exception e) {
			LogUtil.errorLog(log, e.getMessage(), e);
		}

		return mailMessage;
	}

	/**
	 * 构建邮件内容,包含HTML只是简单的文件描述和附件
	 * 
	 * @return
	 * @throws MessagingException
	 */
	protected Multipart buildMailContent() throws MessagingException {
		BugDesc bugDesc = getBugFeedBack().getBugDesc();

		List<String> bugAttachments = bugDesc.getAttachment();
		Integer bugPriority = bugDesc.getPriority();
		String bugText = bugDesc.getTextDesc();

		Multipart mainPart = new MimeMultipart();

		mainPart.addBodyPart(buildHtmlPart(getBugFeedBack().getPresenter()
				.getName(), bugText, bugPriority, bugAttachments != null
				&& !bugAttachments.isEmpty()));
		buildAttachmentParts(mainPart, bugAttachments);
		return mainPart;
	}

	/**
	 * 构建邮件的Html内容
	 * 
	 * @param bugText
	 * @param bugPriority
	 * @param hasAttachment
	 * @return
	 * @throws MessagingException
	 */
	private BodyPart buildHtmlPart(String userName, String bugText,
			Integer bugPriority, boolean hasAttachment)
			throws MessagingException {
		BodyPart htmlPart = new MimeBodyPart();
		StringBuffer mailHtmlContent = new StringBuffer();
		mailHtmlContent.append("<!DOCTYPE html>");
		mailHtmlContent.append("<html>");
		mailHtmlContent.append("<head>");
		mailHtmlContent.append("<meta charset='UTF-8'>");
		mailHtmlContent.append("<title>纸鹤软件 BUG 反馈邮件</title>");
		mailHtmlContent.append("<style type='text/css'>");
		mailHtmlContent.append("body{");
		mailHtmlContent.append("font-family:Arial, sans-serif;");
		mailHtmlContent.append("font-size:11pt !important;");
		mailHtmlContent.append("}");

		mailHtmlContent.append("</style>");
		mailHtmlContent.append("</head>");
		mailHtmlContent.append("<body>");

		mailHtmlContent.append("<table style='width: +'" + "100%"
				+ "'+; ' border='0px'>");
		// 问候行
		mailHtmlContent.append("<tr><td>您好!</td></tr>");

		// XX提了一个bug
		mailHtmlContent
				.append("<tr><td style='margin: 0 0 0 2em'><font color='blue'>");
		mailHtmlContent.append(userName);
		mailHtmlContent.append("</font>已经提交了一个bug,需要您处理或关注!</td></tr>");
		// 问题详述
		mailHtmlContent
				.append("<tr><td style='margin: 0 0 0 2em'><b>问题详述:</b></td></tr>");

		if (StringUtils.isNotBlank(bugText)) {
			mailHtmlContent.append("<tr><td style='margin: 0 0 0 4em'><p>");
			mailHtmlContent.append(bugText);
			mailHtmlContent.append("</p></td></tr>");
		} else {
			mailHtmlContent
					.append("<tr><td style='margin: 0 0 0 4em'>没有文字描述信息</td></tr>");
		}

		if (hasAttachment) {
			mailHtmlContent
					.append("<tr><td style='margin: 0 0 0 2em'><I>详细您还可以参见附件</I></td></tr>");
		}
		mailHtmlContent.append("</table><p><p><p><p>");

		// 签名
		mailHtmlContent.append(getSignatureHtml());
		
		mailHtmlContent.append("</body>");
		mailHtmlContent.append("</html>");

		htmlPart.setContent(mailHtmlContent.toString(),
				"text/html; charset=utf-8");
		return htmlPart;
	}

	/**
	 * 构建附件
	 * 
	 * @param mainPart
	 * @param bugAttachments
	 * @throws MessagingException
	 */
	private void buildAttachmentParts(Multipart mainPart,
			List<String> bugAttachments) throws MessagingException {
		if(mainPart == null || bugAttachments == null || bugAttachments.isEmpty() ){
			return;
		}
		BodyPart attachmentPart = null;
		DataSource ds = null;
		BASE64Encoder enc = new BASE64Encoder();
		for (String fileName : bugAttachments) {
			File file = new File(fileName);
			if (!file.exists()) {
				continue;
			}
			attachmentPart = new MimeBodyPart();
			ds = new FileDataSource(fileName);
			attachmentPart.setDataHandler(new DataHandler(ds));
			// 处理中文附件名称
			attachmentPart.setFileName("=?UTF-8?B?"
					+ enc.encode(file.getName().getBytes()) + "?=");
			mainPart.addBodyPart(attachmentPart);
		}
	}

	public BugFeedback getBugFeedBack() {
		return bugFeedBack;
	}

	public void setBugFeedBack(BugFeedback bugFeedBack) {
		this.bugFeedBack = bugFeedBack;
	}

	private String getSignatureHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<span style='font-size: 9pt;'>顺祝</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;'> </span><span style='font-size: 9pt;'>商祺！</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'> &nbsp;<o:p></o:p></span></div>");
		buf.append("<p class='MsoNormal' style='line-height: 13.5pt; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;'><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'>&nbsp;</span></p>");
		buf.append("<p class='MsoNormal' style='line-height: 13.5pt; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;'><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'>----------------------------------------------------------------------<o:p></o:p></span></p>");
		buf.append("<p class='MsoNormal' style='line-height: 13.5pt; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;'><span style='font-size: 9pt;'>联系方式</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'>:<o:p></o:p></span></p>");
		buf.append("<p class='MsoNormal' style='line-height: 13.5pt; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;'><span style='font-size: 9pt;'>手</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'> &nbsp; &nbsp;</span><span style='font-size: 9pt;'>机：</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'>170 9255 1493&nbsp;(</span><span style='font-size: 9pt;'>南京</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'>) / 13451782717 (</span><span style='font-size: 9pt;'>苏州</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'>)<o:p></o:p></span></p>");
		buf.append("<p class='MsoNormal' style='line-height: 13.5pt; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;'><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'>QQ &nbsp;</span><span style='font-size: 9pt;'>号：</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'>371755616&nbsp;<o:p></o:p></span></p>");
		buf.append("<p class='MsoNormal' style='line-height: 13.5pt; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;'><span style='font-size: 9pt;'>邮</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'> &nbsp; &nbsp;</span><span style='font-size: 9pt;'>箱：</span><span style='font-size: 9pt; font-family: Verdana, sans-serif;' lang='EN-US'><a href='mailto:shui878412@126.com'>shui878412@126.com</a>&nbsp;<o:p></o:p></span>");
		buf.append("<hr />");
		buf.append("<p class='MsoNormal' style='line-height: 13.5pt; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;'><span style='font-size: 10pt;font-weight:bold; font-family: 宋体,Verdana, sans-serif;' lang='EN-US'>纸鹤软件,竭诚为您服务!&nbsp;<o:p></o:p></span></p>");
		return buf.toString();
	}

}
