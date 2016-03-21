package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 平台
 * 
 * @author HANLEI
 *
 */
public class Platform extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String code;
	
	private String status;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
