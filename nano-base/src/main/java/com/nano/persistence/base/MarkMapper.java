package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Mark;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface MarkMapper {

	Mark findOne(String id) throws NanoDBSelectRuntimeException;

	List<Mark> findMany() throws NanoDBSelectRuntimeException;

	List<Mark> findMany(RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Mark mark) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Mark> marks) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Mark mark) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Mark mark) throws NanoDBDeleteRuntimeException;
}
