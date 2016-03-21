package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Region;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface RegionMapper {

	Region findOne(String id) throws NanoDBSelectRuntimeException;

	List<Region> findMany(Region region) throws NanoDBSelectRuntimeException;

	List<Region> findMany(Region region,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	List<Region> findRoot() throws NanoDBSelectRuntimeException;

	void saveOne(Region region) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Region> regions) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Region region) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Region region) throws NanoDBDeleteRuntimeException;
}
