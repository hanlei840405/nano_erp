package com.nano.persistence.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Stock;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface StockMapper {

	Stock findOne(String id) throws NanoDBSelectRuntimeException;

	List<Stock> findMany(Map<String,String> params) throws NanoDBSelectRuntimeException;

	List<Stock> findMany(Map<String,String> params,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Stock stock) throws NanoDBInsertRuntimeException;
	
	void saveMany(Map<String,Object> params) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Stock stock) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Stock stock) throws NanoDBDeleteRuntimeException;
}
