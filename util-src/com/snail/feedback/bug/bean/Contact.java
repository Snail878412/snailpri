package com.snail.feedback.bug.bean;

/**
 * 联系人
 * 
 * @author Snail
 *
 */
public class Contact {
	/**
	 * 编码
	 */
	private String no;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 联系电话
	 */
	private String tel;
	/**
	 * email
	 */
	private String email;
	/**
	 * 备注
	 */
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
}
