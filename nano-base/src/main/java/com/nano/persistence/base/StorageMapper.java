package com.nano.persistence.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Storage;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface StorageMapper {

	Storage findOne(String id) throws NanoDBSelectRuntimeException;

	List<Storage> findMany(Map<String,String> params) throws NanoDBSelectRuntimeException;

	List<Storage> findMany(Map<String,String> params,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Storage stockBatch) throws NanoDBInsertRuntimeException;
	
	void saveMany(Map<String,Object> params) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Storage stockBatch) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Storage stockBatch) throws NanoDBDeleteRuntimeException;
}
