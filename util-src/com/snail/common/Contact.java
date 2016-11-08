package com.snail.common;

import com.snail.util.ProUtils;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.common.Contact
 * </p>
 * 
 * <p>
 * Description: 联系方式
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
public class Contact {

	private static Contact instance = null;

	private String name;
	private String phone;
	private String email;
	private String qq;
	private String motto;

	public static Contact getInstance() {
		if (instance == null) {
			instance = new Contact();
		}
		return instance;
	}

	@Override
	public String toString() {
		return this.getName() + "[手机:" + this.getPhone() + ",Email:"
				+ this.getEmail() + ",QQ:" + this.getQq() + "]";
	}

	private Contact() {
		this("罗来枫", "17092551493", "shui878412@126.com", "371755616",
				"您的满意就是我最大的动力!");
		String contactName = ProUtils.getPropertyValue("CONTACT_NAME");
		if (StringUtils.isBlank(contactName)) {
			this.name = contactName;
			this.phone = ProUtils.getPropertyValue("CONTACT_PHONE");
			this.email = ProUtils.getPropertyValue("CONTACT_EMAIL");
			this.qq = ProUtils.getPropertyValue("CONTACT_QQ");
			this.motto = ProUtils.getPropertyValue("CONTACT_MOTTO");
		}
	}

	public Contact(String name, String phone, String email, String qq,
			String motto) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.qq = qq;
		this.motto = motto;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

}
