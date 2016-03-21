package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌
 * 
 * @author HANLEI
 *
 */
public class Brand extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String code;

	private String status;

	private List<Category> categories = new ArrayList<>();

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
