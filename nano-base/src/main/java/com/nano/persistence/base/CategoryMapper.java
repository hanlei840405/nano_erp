package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Category;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface CategoryMapper {

	Category findOne(String id) throws NanoDBSelectRuntimeException;

	List<Category> findMany(Category category) throws NanoDBSelectRuntimeException;

	List<Category> findMany(Category category,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	List<Category> findRoots() throws NanoDBSelectRuntimeException;

	void saveOne(Category category) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Category> categorys) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Category category) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Category category) throws NanoDBDeleteRuntimeException;
}
