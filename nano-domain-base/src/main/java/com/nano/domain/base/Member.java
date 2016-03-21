package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

public class Member extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String realName;

	private String nick;

	private String telephone;

	private String mobile;

	private String qq;

	private String mail;

	private String msn;

	private String account;

	private String address;

	/**
	 * 总花费
	 */
	private Double outlay;

	/**
	 * 积分
	 */
	private Long vantages;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getOutlay() {
		return outlay;
	}

	public void setOutlay(Double outlay) {
		this.outlay = outlay;
	}

	public Long getVantages() {
		return vantages;
	}

	public void setVantages(Long vantages) {
		this.vantages = vantages;
	}
}
