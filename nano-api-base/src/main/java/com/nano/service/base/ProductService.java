package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Product;

public interface ProductService {

	Product save(Product product);

	List<Product> save(List<Product> products);

	void delete(String id);

	void delete(List<String> ids);

	Product findOne(String id);

	void update(Product product);

	void update(List<Product> products);

	PageInfo<Product> find(Product product, int pageNo, int pageSize);

	List<Product> find(Product product);
	
	Product findOne(Product product);
}
