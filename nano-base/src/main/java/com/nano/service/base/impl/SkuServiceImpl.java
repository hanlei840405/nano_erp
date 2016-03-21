package com.nano.service.base.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nano.domain.base.Stock;
import com.nano.domain.base.Storage;
import com.nano.service.base.StockService;
import com.nano.service.base.StorageService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Sku;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.SkuMapper;
import com.nano.service.base.SkuService;

@Service
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuMapper skuMapper;
	@Autowired
	private StockService stockService;
	@Autowired
	private StorageService storageService;

	@Transactional	
	public Sku save(Sku sku) {
		skuMapper.saveOne(sku);
		return sku;
	}

	@Transactional
	public List<Sku> save(List<Sku> skus) {
		skuMapper.saveMany(skus);
		return skus;
	}

	@Transactional
	public void delete(String id) {
		Sku sku = findOne(id);
		long resultCnt = skuMapper.deleteOne(sku);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					sku.getId()));
		}
		Map<String,String> queryStockParam = new HashMap<>();
		queryStockParam.put("skuId", id);
		PageInfo<Stock> stockPageInfo = stockService.find(queryStockParam,0,1);
		for(Stock stock : stockPageInfo.getList()){
			stockService.delete(stock.getId());
		}

		Map<String,String> queryStorageParam = new HashMap<>();
		queryStorageParam.put("skuId", id);
		PageInfo<Storage> storagePageInfo = storageService.find(queryStorageParam,0,1);
		for(Storage storage : storagePageInfo.getList()){
			storageService.delete(storage.getId());
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Sku findOne(String id) {
		return skuMapper.findOne(id);
	}

	@Transactional
	public void update(Sku sku) {
		long resultCnt = skuMapper.updateOne(sku);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					sku.getId()));
		}
	}

	@Transactional
	public void update(List<Sku> skus) {
		for (Sku sku : skus) {
			update(sku);
		}

	}

	public PageInfo<Sku> find(Sku sku,int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Sku> skus = skuMapper.findMany(sku,rowBounds);
		PageInfo<Sku> pageInfo = new PageInfo<Sku>(skus);
		return pageInfo;
	}

	public List<Sku> find(Sku sku) {
		return skuMapper.findMany(sku);
	}

	@Override
	public Sku findOne(Sku sku) {
		RowBounds rowBounds = new RowBounds(0, 1);
		List<Sku> skus = skuMapper.findMany(sku,rowBounds);
		if(skus.isEmpty()){
			return null;
		}
		return skus.get(0);
	}

}
