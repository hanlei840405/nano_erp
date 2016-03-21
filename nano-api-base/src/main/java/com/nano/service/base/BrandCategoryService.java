package com.nano.service.base;

import java.util.List;

import com.nano.domain.base.BrandCategory;

public interface BrandCategoryService {

	List<BrandCategory> find(BrandCategory brandCategory);

	void save(BrandCategory brandCategory);
	
	void save(List<BrandCategory> brandCategories);
	
	void delete(BrandCategory brandCategory);
}
