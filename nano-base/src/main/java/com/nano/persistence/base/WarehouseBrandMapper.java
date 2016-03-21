package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.WarehouseBrand;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface WarehouseBrandMapper {

	List<WarehouseBrand> findMany(WarehouseBrand warehouseBrand) throws NanoDBSelectRuntimeException;

	List<WarehouseBrand> findMany(WarehouseBrand warehouseBrand,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(WarehouseBrand warehouseBrand) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<WarehouseBrand> warehouseBrand) throws NanoDBInsertRuntimeException;
	
	Long delete(WarehouseBrand warehouseBrand) throws NanoDBDeleteRuntimeException;
}
