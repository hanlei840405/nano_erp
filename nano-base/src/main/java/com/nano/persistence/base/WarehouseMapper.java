package com.nano.persistence.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Warehouse;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface WarehouseMapper {

	Warehouse findOne(String id) throws NanoDBSelectRuntimeException;

	List<Warehouse> findMany(Map<String,String> params) throws NanoDBSelectRuntimeException;

	List<Warehouse> findMany(Map<String,String> params,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Warehouse warehouse) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Warehouse> warehouses) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Warehouse warehouse) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Warehouse warehouse) throws NanoDBDeleteRuntimeException;
}
