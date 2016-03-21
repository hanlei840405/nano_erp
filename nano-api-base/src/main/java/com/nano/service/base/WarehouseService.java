package com.nano.service.base;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Warehouse;

public interface WarehouseService {

	Warehouse save(Warehouse warehouse);

	List<Warehouse> save(List<Warehouse> warehouses);

	void delete(String id);

	void delete(List<String> ids);

	Warehouse findOne(String id);

	void update(Warehouse warehouse);

	void update(List<Warehouse> warehouses);

	PageInfo<Warehouse> find(Map<String,String> params, int pageNo, int pageSize);

	List<Warehouse> find(Map<String,String> params);
}
