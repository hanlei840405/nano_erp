package com.nano.persistence.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.PlatformExpress;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface PlatformExpressMapper {

	List<PlatformExpress> findMany(PlatformExpress platformExpress) throws NanoDBSelectRuntimeException;

	List<PlatformExpress> findMany(PlatformExpress platformExpress,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(PlatformExpress platformExpress) throws NanoDBInsertRuntimeException;
	
	void saveMany(Map<String,Object> params) throws NanoDBInsertRuntimeException;
	
	Long delete(PlatformExpress platformExpress) throws NanoDBDeleteRuntimeException;
}
