package com.nano.service.base.impl;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.*;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.BrandMapper;
import com.nano.service.base.BrandCategoryService;
import com.nano.service.base.BrandService;
import com.nano.service.base.ProductService;
import com.nano.service.base.WarehouseBrandService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandMapper brandMapper;
	@Autowired
	private BrandCategoryService brandCategoryService;
	@Autowired
	private WarehouseBrandService warehouseBrandService;
	@Autowired
	private ProductService productService;

	@Transactional
	public Brand save(Brand brand) {
		brandMapper.saveOne(brand);
		if(!brand.getCategories().isEmpty()){
			List<BrandCategory> brandCategories = new ArrayList<BrandCategory>();
			for(Category category : brand.getCategories()){
				BrandCategory brandCategory = new BrandCategory();
				brandCategory.setBrandId(brand.getId());
				brandCategory.setCategoryId(category.getId());
				brandCategories.add(brandCategory);
			}
			brandCategoryService.save(brandCategories);
		}
		return brand;
	}

	@Transactional
	public List<Brand> save(List<Brand> brands) {
		brandMapper.saveMany(brands);
		for(Brand brand : brands){
			if(!brand.getCategories().isEmpty()){
				List<BrandCategory> brandCategories = new ArrayList<BrandCategory>();
				for(Category category : brand.getCategories()){
					BrandCategory brandCategory = new BrandCategory();
					brandCategory.setBrandId(brand.getId());
					brandCategory.setCategoryId(category.getId());
					brandCategories.add(brandCategory);
				}
				brandCategoryService.save(brandCategories);
			}
		}
		return brands;
	}

	@Transactional
	public void delete(String id) {
		Brand brand = findOne(id);
		Product product = new Product();
		product.setBrandId(id);
		List<Product> products = productService.find(product);
		if(!products.isEmpty()){
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,
					brand.getId() + " : " + brand.getName()));
		}
		long resultCnt = brandMapper.deleteOne(brand);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					brand.getId() + " : " + brand.getName()));
		}
		BrandCategory brandCategory = new BrandCategory();
		brandCategory.setBrandId(id);
		brandCategoryService.delete(brandCategory);
		WarehouseBrand warehouseBrand = new WarehouseBrand();
		warehouseBrand.setBrandId(id);
		warehouseBrandService.delete(warehouseBrand);
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Brand findOne(String id) {
		return brandMapper.findOne(id);
	}

	@Transactional
	public void update(Brand brand) {
		long resultCnt = brandMapper.updateOne(brand);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					brand.getId() + " : " + brand.getName()));
		}
		BrandCategory brandCategory = new BrandCategory();
		brandCategory.setBrandId(brand.getId());
		brandCategoryService.delete(brandCategory);
		if(!brand.getCategories().isEmpty()){
			List<BrandCategory> brandCategories = new ArrayList<BrandCategory>();
			for(Category category : brand.getCategories()){
				BrandCategory bc = new BrandCategory();
				bc.setBrandId(brand.getId());
				bc.setCategoryId(category.getId());
				brandCategories.add(bc);
			}
			brandCategoryService.save(brandCategories);
		}
	}

	@Transactional
	public void update(List<Brand> brands) {
		for(Brand brand : brands){
			update(brand);
		}
	}

	public PageInfo<Brand> find(Brand brand, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Brand> brands = brandMapper.findMany(brand, rowBounds);
		PageInfo<Brand> pageInfo = new PageInfo<Brand>(brands);
		return pageInfo;
	}

	public List<Brand> find(Brand brand) {
		return brandMapper.findMany(brand);
	}

}
