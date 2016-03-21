package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Sku;

public interface SkuService {

	Sku save(Sku sku);

	List<Sku> save(List<Sku> skus);

	void delete(String id);

	void delete(List<String> ids);

	Sku findOne(String id);

	void update(Sku sku);

	void update(List<Sku> skus);

	PageInfo<Sku> find(Sku sku, int pageNo, int pageSize);

	List<Sku> find(Sku sku);

	Sku findOne(Sku sku);
}
