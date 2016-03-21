package com.nano.service.base;

import java.util.List;

import com.nano.domain.base.WarehouseBrand;

public interface WarehouseBrandService {

	List<WarehouseBrand> find(WarehouseBrand warehouseBrand);

	void save(WarehouseBrand warehouseBrand);
	
	void save(List<WarehouseBrand> warehouseBrands);
	
	void delete(WarehouseBrand warehouseBrand);
}
