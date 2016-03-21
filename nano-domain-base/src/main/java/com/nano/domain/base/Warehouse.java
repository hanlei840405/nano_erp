package com.nano.domain.base;

import java.util.ArrayList;
import java.util.List;

import com.nano.domain.AbstractEntity;

/**
 * 仓库
 * 
 * @author HANLEI
 *
 */
public class Warehouse extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String code;
	
	private String status;
	
	private List<WarehouseBrand> warehouseBrands = new ArrayList<>();

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

	public List<WarehouseBrand> getWarehouseBrands() {
		return warehouseBrands;
	}

	public void setWarehouseBrands(List<WarehouseBrand> warehouseBrands) {
		this.warehouseBrands = warehouseBrands;
	}
}
