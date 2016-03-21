package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Shop;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface ShopMapper {

	Shop findOne(String id) throws NanoDBSelectRuntimeException;

	List<Shop> findMany(Shop shop) throws NanoDBSelectRuntimeException;

	List<Shop> findMany(Shop shop,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Shop shop) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Shop> shops) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Shop shop) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Shop shop) throws NanoDBDeleteRuntimeException;
}
