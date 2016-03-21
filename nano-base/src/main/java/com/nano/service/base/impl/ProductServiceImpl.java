package com.nano.service.base.impl;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Product;
import com.nano.domain.base.Sku;
import com.nano.domain.base.Stock;
import com.nano.domain.base.Storage;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.ProductMapper;
import com.nano.service.base.ProductService;
import com.nano.service.base.SkuService;
import com.nano.service.base.StockService;
import com.nano.service.base.StorageService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private SkuService skuService;
	@Autowired
	private StockService stockService;
	@Autowired
	private StorageService storageService;

	@Transactional	
	public Product save(Product product) {
		productMapper.saveOne(product);
		return product;
	}

	@Transactional
	public List<Product> save(List<Product> products) {
		productMapper.saveMany(products);
		return products;
	}

	@Transactional
	public void delete(String id) {
		Product product = findOne(id);
		long resultCnt = productMapper.deleteOne(product);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					product.getId()));
		}
		Sku querySkuParam = new Sku();
		querySkuParam.setProductId(id);
		List<Sku> skus = skuService.find(querySkuParam);
		for(Sku sku : skus){
			skuService.delete(sku.getId());
		}
		Map<String,String> queryStockParam = new HashMap<>();
		queryStockParam.put("productId", id);
		PageInfo<Stock> stockPageInfo = stockService.find(queryStockParam,0,1);
		for(Stock stock : stockPageInfo.getList()){
			stockService.delete(stock.getId());
		}

		Map<String,String> queryStorageParam = new HashMap<>();
		queryStorageParam.put("productId", id);
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

	public Product findOne(String id) {
		return productMapper.findOne(id);
	}

	@Transactional
	public void update(Product product) {
		long resultCnt = productMapper.updateOne(product);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					product.getId()));
		}
	}

	@Transactional
	public void update(List<Product> products) {
		for (Product product : products) {
			update(product);
		}

	}

	public PageInfo<Product> find(Product product,int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Product> products = productMapper.findMany(product,rowBounds);
		PageInfo<Product> pageInfo = new PageInfo<Product>(products);
		return pageInfo;
	}

	public List<Product> find(Product product) {
		return productMapper.findMany(product);
	}

	public Product findOne(Product product) {
		RowBounds rowBounds = new RowBounds(0, 1);
		List<Product> products = productMapper.findMany(product,rowBounds);
		if(products.isEmpty()){
			return null;
		}
		return products.get(0);
	}

}
