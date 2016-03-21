package com.nano.persistence.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.RegionExpress;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface RegionExpressMapper {

	List<RegionExpress> findMany(RegionExpress regionExpress) throws NanoDBSelectRuntimeException;

	List<RegionExpress> findMany(RegionExpress regionExpress,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(RegionExpress regionExpress) throws NanoDBInsertRuntimeException;
	
	void saveMany(Map<String,Object> params) throws NanoDBInsertRuntimeException;
	
	Long delete(RegionExpress regionExpress) throws NanoDBDeleteRuntimeException;
}
