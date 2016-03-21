package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 短信模板
 * 
 * @author HANLEI
 *
 */
public class SmsTemplate extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String code;

	private String template;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}
