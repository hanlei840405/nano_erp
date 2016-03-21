package com.nano.service.base.impl;

import com.nano.domain.base.BrandCategory;
import com.nano.persistence.base.BrandCategoryMapper;
import com.nano.service.base.BrandCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandCategoryServiceImpl implements BrandCategoryService {

	@Autowired
	private BrandCategoryMapper brandCategoryMapper;
	
	public List<BrandCategory> find(BrandCategory brandCategory) {
		return brandCategoryMapper.findMany(brandCategory);
	}

	@Transactional
	public void save(BrandCategory brandCategory) {
		brandCategoryMapper.saveOne(brandCategory);
	}

	@Transactional
	public void save(List<BrandCategory> brandCategories) {
		brandCategoryMapper.saveMany(brandCategories);
	}

	@Transactional
	public void delete(BrandCategory brandCategory) {
		brandCategoryMapper.delete(brandCategory);
	}

}
