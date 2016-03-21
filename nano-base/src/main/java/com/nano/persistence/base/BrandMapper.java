package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Brand;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface BrandMapper {

	Brand findOne(String id) throws NanoDBSelectRuntimeException;

	List<Brand> findMany(Brand brand) throws NanoDBSelectRuntimeException;

	List<Brand> findMany(Brand brand,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Brand brand) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Brand> brands) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Brand brand) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Brand brand) throws NanoDBDeleteRuntimeException;
}
