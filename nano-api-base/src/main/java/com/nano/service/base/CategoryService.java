package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Category;

public interface CategoryService {

	Category save(Category category);

	List<Category> save(List<Category> categories);

	void delete(String id);

	void delete(List<String> ids);

	Category findOne(String id);

	void update(Category category);

	void update(List<Category> categories);
	
	PageInfo<Category> find(Category category,int pageNo,int pageSize);
	
	List<Category> find(Category category);

	List<Category> findRoots();
}
