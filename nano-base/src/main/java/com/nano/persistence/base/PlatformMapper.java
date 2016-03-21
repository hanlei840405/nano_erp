package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Platform;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface PlatformMapper {

	Platform findOne(String id) throws NanoDBSelectRuntimeException;

	List<Platform> findMany(Platform platform) throws NanoDBSelectRuntimeException;

	List<Platform> findMany(Platform platform,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Platform platform) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Platform> platforms) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Platform platform) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Platform platform) throws NanoDBDeleteRuntimeException;
}
