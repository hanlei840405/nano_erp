package com.nano.domain.base;

import java.util.ArrayList;
import java.util.List;

import com.nano.domain.AbstractEntity;

public class Contact extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	private String name;

	private List<String> userIds = new ArrayList<String>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
}
