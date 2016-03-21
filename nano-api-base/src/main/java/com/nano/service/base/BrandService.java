package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Brand;

public interface BrandService {

	Brand save(Brand brand);

	List<Brand> save(List<Brand> brands);

	void delete(String id);

	void delete(List<String> ids);

	Brand findOne(String id);

	void update(Brand brand);

	void update(List<Brand> brands);
	
	PageInfo<Brand> find(Brand brand,int pageNo,int pageSize);
	
	List<Brand> find(Brand brand);
}
