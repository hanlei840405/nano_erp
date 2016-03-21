package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 区域
 * 
 * @author HANLEI
 *
 */
public class Region extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String code;
	
	private String parentId;

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
