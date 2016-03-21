package com.nano.web.vo.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/9/12.
 */
public class BrandVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String code;
	private String name;
	private String status;
	private Date created;
	private List<String> categoryIds = new ArrayList<>();
	private List<String> categoryNames = new ArrayList<>();

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public List<String> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<String> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<String> getCategoryNames() {
		return categoryNames;
	}

	public void setCategoryNames(List<String> categoryNames) {
		this.categoryNames = categoryNames;
	}
}
