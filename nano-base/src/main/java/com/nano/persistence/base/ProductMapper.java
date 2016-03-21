package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Product;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface ProductMapper {

	Product findOne(String id) throws NanoDBSelectRuntimeException;

	List<Product> findMany(Product product) throws NanoDBSelectRuntimeException;

	List<Product> findMany(Product product,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Product product) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Product> products) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Product product) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Product product) throws NanoDBDeleteRuntimeException;
}
