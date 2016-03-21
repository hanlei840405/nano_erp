package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Express;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface ExpressMapper {

	Express findOne(String id) throws NanoDBSelectRuntimeException;

	List<Express> findMany(Express express) throws NanoDBSelectRuntimeException;

	List<Express> findMany(Express express,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Express express) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Express> expresses) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Express express) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Express express) throws NanoDBDeleteRuntimeException;
}
