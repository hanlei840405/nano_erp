package com.nano.domain.system;

import java.util.ArrayList;
import java.util.List;

import com.nano.domain.AbstractEntity;

public class Element extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;

	private List<Privilege> privileges = new ArrayList<Privilege>(0);

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

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}

}
