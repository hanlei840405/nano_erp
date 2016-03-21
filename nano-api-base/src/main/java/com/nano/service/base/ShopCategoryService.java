package com.nano.service.base;

import java.util.List;

import com.nano.domain.base.ShopCategory;

public interface ShopCategoryService {

	List<ShopCategory> find(ShopCategory shopCategory);

	void save(ShopCategory shopCategory);
	
	void save(List<ShopCategory> shopCategories);
	
	void delete(ShopCategory shopCategory);
}
