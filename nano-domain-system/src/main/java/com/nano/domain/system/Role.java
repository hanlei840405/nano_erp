package com.nano.domain.system;

import java.util.ArrayList;
import java.util.List;

import com.nano.domain.AbstractEntity;

public class Role extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private List<User> users = new ArrayList<User>(0);
	private List<Privilege> privileges = new ArrayList<Privilege>(0);
	private List<Group> groups = new ArrayList<Group>(0);

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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
}
