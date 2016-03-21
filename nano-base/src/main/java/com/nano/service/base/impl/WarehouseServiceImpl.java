package com.nano.service.base.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Warehouse;
import com.nano.domain.base.WarehouseBrand;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.WarehouseMapper;
import com.nano.service.base.WarehouseBrandService;
import com.nano.service.base.WarehouseService;

@Service
public class WarehouseServiceImpl implements WarehouseService {

	@Autowired
	private WarehouseMapper warehouseMapper;

	@Autowired
	private WarehouseBrandService warehouseBrandService;

	@Transactional	
	public Warehouse save(Warehouse warehouse) {
		warehouseMapper.saveOne(warehouse);
		if(!warehouse.getWarehouseBrands().isEmpty()){
			warehouseBrandService.save(warehouse.getWarehouseBrands());
		}
		return warehouse;
	}

	@Transactional
	public List<Warehouse> save(List<Warehouse> warehouses) {
		warehouseMapper.saveMany(warehouses);
		return warehouses;
	}

	@Transactional
	public void delete(String id) {
		Warehouse warehouse = findOne(id);
		long resultCnt = warehouseMapper.deleteOne(warehouse);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					warehouse.getId()));
		}
		WarehouseBrand queryParam = new WarehouseBrand();
		queryParam.setWarehouseId(id);
		warehouseBrandService.delete(queryParam);
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Warehouse findOne(String id) {
		return warehouseMapper.findOne(id);
	}

	@Transactional
	public void update(Warehouse warehouse) {
		long resultCnt = warehouseMapper.updateOne(warehouse);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					warehouse.getId()));
		}
		WarehouseBrand queryParam = new WarehouseBrand();
		queryParam.setWarehouseId(warehouse.getId());
		warehouseBrandService.delete(queryParam);
		if(!warehouse.getWarehouseBrands().isEmpty()){
			warehouseBrandService.save(warehouse.getWarehouseBrands());
		}
	}

	@Transactional
	public void update(List<Warehouse> warehouses) {
		for (Warehouse warehouse : warehouses) {
			update(warehouse);
		}

	}

	public PageInfo<Warehouse> find(Map<String,String> params,int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Warehouse> warehouses = warehouseMapper.findMany(params,rowBounds);
		PageInfo<Warehouse> pageInfo = new PageInfo<Warehouse>(warehouses);
		return pageInfo;
	}

	public List<Warehouse> find(Map<String,String> params) {
		return warehouseMapper.findMany(params);
	}

}
