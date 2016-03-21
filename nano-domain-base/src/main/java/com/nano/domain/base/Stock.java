package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 库存
 * 
 * @author HANLEI
 *
 */
public class Stock extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;

	private String skuId;

	private String warehouseId;

	private int quantity;
	
	private int safeQuantity;
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getSafeQuantity() {
		return safeQuantity;
	}

	public void setSafeQuantity(int safeQuantity) {
		this.safeQuantity = safeQuantity;
	}
}
