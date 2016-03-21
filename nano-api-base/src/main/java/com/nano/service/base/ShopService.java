package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Shop;

public interface ShopService {

	Shop save(Shop shop);

	List<Shop> save(List<Shop> shops);

	void delete(String id);

	void delete(List<String> ids);

	Shop findOne(String id);

	void update(Shop shop);

	void update(List<Shop> shops);

	PageInfo<Shop> find(Shop shop, int pageNo, int pageSize);

	List<Shop> find(Shop shop);
}
