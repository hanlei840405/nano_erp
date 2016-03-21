package com.nano.service.base.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nano.domain.base.Stock;
import com.nano.service.base.StockService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Storage;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.StorageMapper;
import com.nano.service.base.StorageService;

@Service
public class StorageServiceImpl implements StorageService {
	private static final Logger logger = LoggerFactory
			.getLogger(StorageServiceImpl.class);
	@Autowired
	private StorageMapper storageMapper;
	@Autowired
	private StockService stockService;
	@Transactional
	public Storage save(Storage storage) {
		storageMapper.saveOne(storage);
		return null;
	}

	@Transactional
	public List<Storage> save(List<Storage> storages) {
		Map<String,Object> params = new HashMap<>();
		params.put("storages", storages);
		storageMapper.saveMany(params);
		return storages;
	}

	@Transactional
	public void delete(String id) {
		Storage storage = findOne(id);
		long resultCnt = storageMapper.deleteOne(storage);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					storage.getId()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		for(String id : ids){
			delete(id);
		}
	}

	public Storage findOne(String id) {
		return storageMapper.findOne(id);
	}

	@Transactional
	public void update(Storage storage) {
		long resultCnt = storageMapper.updateOne(storage);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					storage.getId()));
		}
	}

	/**
	 * 入库
	 * @param storage
	 */
	@Transactional
	public void storage(Storage storage) {
		update(storage);
		Map<String,String> queryParam = new HashMap<>();
		queryParam.put("skuId",storage.getSkuId());
		queryParam.put("warehouseId",storage.getWarehouseId());
		PageInfo<Stock> stocks = stockService.find(queryParam, 0, 1);
		Stock stock;
		if(stocks.getList().isEmpty()){
			stock = new Stock();
			stock.setCreator(storage.getCreator());
			stock.setProductId(storage.getProductId());
			stock.setSkuId(storage.getSkuId());
			stock.setQuantity(storage.getQuantity());
			stock.setWarehouseId(storage.getWarehouseId());
			stockService.save(stock);
		}else{
			stock = stocks.getList().get(0);
			stock.setQuantity(stock.getQuantity() + storage.getQuantity());
			stock.setModifier(storage.getModifier());
			while(true){
				try{
					stockService.update(stock);
				}catch(NanoDBConsistencyRuntimeException e){
					logger.error(Marker.ANY_MARKER,e.getMessage());
					continue;
				}
				break;
			}
		}
	}

	/**
	 * 批量入库
	 * @param storages
	 */
	@Transactional
	public void storage(List<Storage> storages) {
		for(Storage storage : storages){
			storage(storage);
		}
	}

	@Transactional
	public void update(List<Storage> storages) {
		for(Storage storage : storages){
			update(storage);
		}
	}

	public PageInfo<Storage> find(Map<String, String> params,
			int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo,pageSize);
		List<Storage> storages = storageMapper.findMany(params, rowBounds);
		PageInfo<Storage> pageInfo = new PageInfo<Storage>(storages);
		return pageInfo;
	}

	public List<Storage> find(Map<String, String> params) {
		return storageMapper.findMany(params);
	}

}
