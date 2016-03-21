package com.nano.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nano.domain.base.ShopCategory;
import com.nano.persistence.base.ShopCategoryMapper;
import com.nano.service.base.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

	@Autowired
	private ShopCategoryMapper shopCategoryMapper;
	
	public List<ShopCategory> find(ShopCategory shopCategory) {
		return shopCategoryMapper.findMany(shopCategory);
	}

	@Transactional
	public void save(ShopCategory shopCategory) {
		shopCategoryMapper.saveOne(shopCategory);
	}

	@Transactional
	public void save(List<ShopCategory> shopCategories) {
		shopCategoryMapper.saveMany(shopCategories);
	}

	@Transactional
	public void delete(ShopCategory shopCategory) {
		shopCategoryMapper.delete(shopCategory);
	}

}
