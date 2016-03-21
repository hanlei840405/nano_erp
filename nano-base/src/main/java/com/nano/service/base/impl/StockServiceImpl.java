package com.nano.service.base.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Stock;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.StockMapper;
import com.nano.service.base.StockService;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private StockMapper stockMapper;

	@Transactional	
	public Stock save(Stock stock) {
		stockMapper.saveOne(stock);
		return stock;
	}

	@Transactional
	public List<Stock> save(List<Stock> stocks) {
		Map<String, Object> params = new HashMap<>();
		params.put("stocks", stocks);
		stockMapper.saveMany(params);
		return stocks;
	}

	@Transactional
	public void delete(String id) {
		Stock stock = findOne(id);
		long resultCnt = stockMapper.deleteOne(stock);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					stock.getId()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Stock findOne(String id) {
		return stockMapper.findOne(id);
	}

	@Transactional
	public void update(Stock stock) {
		long resultCnt = stockMapper.updateOne(stock);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					stock.getId()));
		}
	}

	@Transactional
	public void update(List<Stock> stocks) {
		for (Stock stock : stocks) {
			update(stock);
		}

	}

	public PageInfo<Stock> find(Map<String,String> params,int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Stock> stocks = stockMapper.findMany(params,rowBounds);
		PageInfo<Stock> pageInfo = new PageInfo<Stock>(stocks);
		return pageInfo;
	}

	public List<Stock> find(Map<String,String> params) {
		return stockMapper.findMany(params);
	}

}
