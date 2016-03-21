package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 字典表
 * 
 * @author HANLEI
 *
 */
public class Dictionary extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String code;

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
}
