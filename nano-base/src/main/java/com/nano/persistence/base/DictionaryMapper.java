package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Dictionary;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface DictionaryMapper {

	Dictionary findOne(String key) throws NanoDBSelectRuntimeException;

	List<Dictionary> findMany(Dictionary dictionary) throws NanoDBSelectRuntimeException;

	List<Dictionary> findMany(Dictionary dictionary,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Dictionary dictionary) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Dictionary> dictionaries) throws NanoDBInsertRuntimeException;
	
	Long deleteOne(Dictionary dictionary) throws NanoDBDeleteRuntimeException;
}
