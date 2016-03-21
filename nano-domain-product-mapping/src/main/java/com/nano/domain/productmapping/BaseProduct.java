package com.nano.domain.productmapping;

import java.util.Date;

import com.nano.domain.AbstractEntity;

public class BaseProduct extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 本地系统的商品ID
	 */
	private String productId;
	private Long num;
	private Date listTime;
	private Date deListTime;
	private String title;
	private String productPrice;
	private String approveStatus;
	private String shopId;
	private String productOuterId;
	private String properties;
	private Long quantity;
	private String skuPrice;
	/*
	 * 本地系统的skuId
	 */
	private String skuId;
	private String barcode;
	private String skuOuterId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public Date getListTime() {
		return listTime;
	}

	public void setListTime(Date listTime) {
		this.listTime = listTime;
	}

	public Date getDeListTime() {
		return deListTime;
	}

	public void setDeListTime(Date deListTime) {
		this.deListTime = deListTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}


	public String getProductOuterId() {
		return productOuterId;
	}

	public void setProductOuterId(String productOuterId) {
		this.productOuterId = productOuterId;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSkuOuterId() {
		return skuOuterId;
	}

	public void setSkuOuterId(String skuOuterId) {
		this.skuOuterId = skuOuterId;
	}
}
