package com.nano.domain.base;

import java.io.Serializable;

public class ShopCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String shopId;

	private String categoryId;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}
