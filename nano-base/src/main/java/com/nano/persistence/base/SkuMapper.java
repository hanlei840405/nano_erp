package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Sku;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface SkuMapper {

	Sku findOne(String id) throws NanoDBSelectRuntimeException;

	List<Sku> findMany(Sku sku) throws NanoDBSelectRuntimeException;

	List<Sku> findMany(Sku sku,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Sku sku) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Sku> skus) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Sku sku) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Sku sku) throws NanoDBDeleteRuntimeException;
}
