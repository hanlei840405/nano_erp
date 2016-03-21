package com.nano.domain.base;

import java.io.Serializable;

public class BrandCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String brandId;

	private String categoryId;

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
