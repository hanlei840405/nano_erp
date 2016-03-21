package com.nano.web.vo.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nano.constant.Constant;

/**
 * Created by Administrator on 2015/9/13.
 */
public class RegionVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String code;

	private String parentId;

	private String text;

	private String state = Constant.OPEN;

	private List<RegionVO> children = new ArrayList<>();

	private List<String> expressIds = new ArrayList<>();

	private List<String> expressNames = new ArrayList<>();

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

	public List<RegionVO> getChildren() {
		return children;
	}

	public void setChildren(List<RegionVO> children) {
		this.children = children;
	}

	public List<String> getExpressIds() {
		return expressIds;
	}

	public void setExpressIds(List<String> expressIds) {
		this.expressIds = expressIds;
	}

	public List<String> getExpressNames() {
		return expressNames;
	}

	public void setExpressNames(List<String> expressNames) {
		this.expressNames = expressNames;
	}
}
