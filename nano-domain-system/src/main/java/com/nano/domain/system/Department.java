package com.nano.domain.system;

import java.util.ArrayList;
import java.util.List;

import com.nano.domain.AbstractEntity;

public class Department extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String parentId;
	private Long position;
	private String code;
	private List<User> users = new ArrayList<User>(0);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
