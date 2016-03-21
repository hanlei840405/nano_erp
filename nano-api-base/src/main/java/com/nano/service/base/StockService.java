package com.nano.service.base;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Stock;

public interface StockService {

	Stock save(Stock stock);

	List<Stock> save(List<Stock> stocks);

	void delete(String id);

	void delete(List<String> ids);

	Stock findOne(String id);

	void update(Stock stock);

	void update(List<Stock> stocks);

	PageInfo<Stock> find(Map<String,String> params, int pageNo, int pageSize);

	List<Stock> find(Map<String,String> params);
}
