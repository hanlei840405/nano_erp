package com.nano.persistence.base;

import java.util.List;

import com.nano.domain.base.ShopProperty;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface ShopPropertyMapper {

	ShopProperty findOne(String id) throws NanoDBSelectRuntimeException;

	List<ShopProperty> findMany(ShopProperty shopProperty) throws NanoDBSelectRuntimeException;

	void saveOne(ShopProperty shopProperty) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<ShopProperty> shopPropertys) throws NanoDBInsertRuntimeException;

	void updateOne(ShopProperty shopProperty) throws NanoDBInsertRuntimeException;
	
	Long deleteOne(ShopProperty shopProperty) throws NanoDBDeleteRuntimeException;
	
	Long deleteByParent(String parentId) throws NanoDBDeleteRuntimeException;
}
