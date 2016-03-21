package com.nano.web.vo.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/12.
 */
public class ContactVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String code;
	private String name;
	private List<String> userIds = new ArrayList<>();
	private List<String> userNames = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}
}
