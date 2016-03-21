package com.nano.service.base.impl;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Shop;
import com.nano.domain.base.ShopCategory;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.ShopMapper;
import com.nano.service.base.ShopCategoryService;
import com.nano.service.base.ShopPropertyService;
import com.nano.service.base.ShopService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopMapper shopMapper;

	@Autowired
	private ShopPropertyService shopPropertyService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Transactional	
	public Shop save(Shop shop) {
		shopMapper.saveOne(shop);
		if(!shop.getShopProperties().isEmpty()){
			shopPropertyService.save(shop.getShopProperties());
		}
		if(!shop.getShopCategories().isEmpty()){
			shopCategoryService.save(shop.getShopCategories());
		}
		return shop;
	}

	@Transactional
	public List<Shop> save(List<Shop> shops) {
		shopMapper.saveMany(shops);
		return shops;
	}

	@Transactional
	public void delete(String id) {
		Shop shop = findOne(id);
		long resultCnt = shopMapper.deleteOne(shop);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					shop.getId()));
		}
		shopPropertyService.deleteByParent(id);
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopId(id);
		shopCategoryService.delete(shopCategory);
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Shop findOne(String id) {
		return shopMapper.findOne(id);
	}

	@Transactional
	public void update(Shop shop) {
		long resultCnt = shopMapper.updateOne(shop);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					shop.getId()));
		}
		shopPropertyService.deleteByParent(shop.getId());
		if(!shop.getShopProperties().isEmpty()){
			shopPropertyService.save(shop.getShopProperties());
		}
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopId(shop.getId());
		shopCategoryService.delete(shopCategory);
		if(!shop.getShopCategories().isEmpty()){
			shopCategoryService.save(shop.getShopCategories());
		}
	}

	@Transactional
	public void update(List<Shop> shops) {
		for (Shop shop : shops) {
			update(shop);
		}

	}

	public PageInfo<Shop> find(Shop shop,int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Shop> shops = shopMapper.findMany(shop,rowBounds);
		PageInfo<Shop> pageInfo = new PageInfo<Shop>(shops);
		return pageInfo;
	}

	public List<Shop> find(Shop shop) {
		return shopMapper.findMany(shop);
	}

}
