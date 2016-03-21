package com.nano.persistence.base;

import java.util.List;

import com.nano.domain.base.BrandCategory;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface BrandCategoryMapper {

	List<BrandCategory> findMany(BrandCategory brandCategory) throws NanoDBSelectRuntimeException;

	void saveOne(BrandCategory brandCategory) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<BrandCategory> brandCategorys) throws NanoDBInsertRuntimeException;
	
	Long delete(BrandCategory brandCategory) throws NanoDBDeleteRuntimeException;
}
