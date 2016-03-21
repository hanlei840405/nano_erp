package com.nano.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nano.domain.base.WarehouseBrand;
import com.nano.persistence.base.WarehouseBrandMapper;
import com.nano.service.base.WarehouseBrandService;

@Service
public class WarehouseBrandServiceImpl implements WarehouseBrandService {

	@Autowired
	private WarehouseBrandMapper warehouseBrandMapper;
	
	public List<WarehouseBrand> find(WarehouseBrand warehouseBrand) {
		return warehouseBrandMapper.findMany(warehouseBrand);
	}

	@Transactional
	public void save(WarehouseBrand warehouseBrand) {
		warehouseBrandMapper.saveOne(warehouseBrand);
	}

	@Transactional
	public void save(List<WarehouseBrand> warehouseBrands) {
		warehouseBrandMapper.saveMany(warehouseBrands);
	}

	@Transactional
	public void delete(WarehouseBrand warehouseBrand) {
		warehouseBrandMapper.delete(warehouseBrand);
	}

}
