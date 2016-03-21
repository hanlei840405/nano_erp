package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.ShopCategory;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface ShopCategoryMapper {

	List<ShopCategory> findMany(ShopCategory shopCategory) throws NanoDBSelectRuntimeException;

	List<ShopCategory> findMany(ShopCategory shopCategory,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(ShopCategory shopCategory) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<ShopCategory> shopCategories) throws NanoDBInsertRuntimeException;
	
	Long delete(ShopCategory shopCategory) throws NanoDBDeleteRuntimeException;
}
