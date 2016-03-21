package com.nano.service.base.impl;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.BrandCategory;
import com.nano.domain.base.Category;
import com.nano.domain.base.Product;
import com.nano.domain.base.ShopCategory;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.CategoryMapper;
import com.nano.service.base.BrandCategoryService;
import com.nano.service.base.CategoryService;
import com.nano.service.base.ProductService;
import com.nano.service.base.ShopCategoryService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private BrandCategoryService brandCategoryService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ProductService productService;
	
	@Transactional
	public Category save(Category category) {
		categoryMapper.saveOne(category);
		return category;
	}

	@Transactional
	public List<Category> save(List<Category> categories) {
		categoryMapper.saveMany(categories);
		return categories;
	}

	@Transactional
	public void delete(String id) {
		Category category = findOne(id);
		Category queryParam = new Category();
		queryParam.setParentId(id);
		List<Category> categories = categoryMapper.findMany(queryParam);
		if(!categories.isEmpty()){
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,
					category.getId() + " : " + category.getName()));
		}
		Product product = new Product();
		product.setCategoryId(id);
		List<Product> products = productService.find(product);
		if(!products.isEmpty()){
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,
					category.getId() + " : " + category.getName()));
		}
		long resultCnt = categoryMapper.deleteOne(category);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					category.getId() + " : " + category.getName()));
		}
		BrandCategory brandCategory = new BrandCategory();
		brandCategory.setCategoryId(id);
		brandCategoryService.delete(brandCategory);
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setCategoryId(id);
		shopCategoryService.delete(shopCategory);
	}

	@Transactional
	public void delete(List<String> ids) {
		for(String id : ids){
			delete(id);
		}
	}

	public Category findOne(String id) {
		return categoryMapper.findOne(id);
	}

	@Transactional
	public void update(Category category) {
		long resultCnt = categoryMapper.updateOne(category);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					category.getId() + " : " + category.getName()));
		}
	}

	@Transactional
	public void update(List<Category> categories) {
		for(Category categoty : categories){
			update(categoty);
		}
	}

	public PageInfo<Category> find(Category category, int pageNo,
			int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Category> categories = categoryMapper.findMany(category, rowBounds);
		PageInfo<Category> pageInfo = new PageInfo<Category>(categories);
		return pageInfo;
	}

	public List<Category> find(Category category) {
		return categoryMapper.findMany(category);
	}

	public List<Category> findRoots() {
		return categoryMapper.findRoots();
	}
}
