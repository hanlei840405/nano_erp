package com.nano.web.vo.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nano.constant.Constant;

public class DepartmentVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String parentId;
	private Long position;
	private String code;
	private String text;
	private String state = Constant.OPEN;
	private List<DepartmentVO> children = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<DepartmentVO> getChildren() {
		return children;
	}

	public void setChildren(List<DepartmentVO> children) {
		this.children = children;
	}
}
