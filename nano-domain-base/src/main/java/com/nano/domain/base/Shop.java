package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺
 * 
 * @author HANLEI
 *
 */
public class Shop extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String code;
	
	private String status;
	
	private String platformId;
	
	private String memo;

	private List<ShopProperty> shopProperties = new ArrayList<>();

	private List<ShopCategory> shopCategories = new ArrayList<>();

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<ShopProperty> getShopProperties() {
		return shopProperties;
	}

	public void setShopProperties(List<ShopProperty> shopProperties) {
		this.shopProperties = shopProperties;
	}

	public List<ShopCategory> getShopCategories() {
		return shopCategories;
	}

	public void setShopCategories(List<ShopCategory> shopCategories) {
		this.shopCategories = shopCategories;
	}
}
