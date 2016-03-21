package com.nano.persistence.base;

import java.util.List;

import com.nano.domain.base.DictionaryItem;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface DictionaryItemMapper {

	DictionaryItem findOne(String id) throws NanoDBSelectRuntimeException;

	List<DictionaryItem> findMany(DictionaryItem dictionaryItem) throws NanoDBSelectRuntimeException;

	void saveOne(DictionaryItem dictionaryItem) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<DictionaryItem> dictionaryItems) throws NanoDBInsertRuntimeException;

	void updateOne(DictionaryItem dictionaryItem) throws NanoDBInsertRuntimeException;
	
	Long deleteOne(DictionaryItem dictionaryItem) throws NanoDBDeleteRuntimeException;
	
	Long deleteByParentId(String parentId) throws NanoDBDeleteRuntimeException;
}
