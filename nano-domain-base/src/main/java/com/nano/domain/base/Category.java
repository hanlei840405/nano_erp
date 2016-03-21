package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类目
 * 
 * @author HANLEI
 *
 */
public class Category extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String code;

	private String parentId;

	private List<Brand> brands = new ArrayList<>();

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

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}
}
