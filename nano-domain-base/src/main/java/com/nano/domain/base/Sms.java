package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 短信
 * 
 * @author HANLEI
 *
 */
public class Sms extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String telephone;

	private String content;

	private String templateId;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
