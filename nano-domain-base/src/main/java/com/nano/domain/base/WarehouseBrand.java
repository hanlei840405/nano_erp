package com.nano.domain.base;

import java.io.Serializable;

public class WarehouseBrand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String warehouseId;
	
	private String brandId;

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
