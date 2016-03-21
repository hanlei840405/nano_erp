package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 字典项
 * 
 * @author HANLEI
 *
 */
public class ShopProperty extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String shopId;

	private String code;

	private String value;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
