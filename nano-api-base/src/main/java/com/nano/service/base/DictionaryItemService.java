package com.nano.service.base;

import java.util.List;

import com.nano.domain.base.DictionaryItem;

public interface DictionaryItemService {

	DictionaryItem findOne(String id);

	List<DictionaryItem> find(DictionaryItem dictionaryItem);

	DictionaryItem save(DictionaryItem dictionaryItem);
	
	List<DictionaryItem> save(List<DictionaryItem> dictionaryItems);

	void update(List<DictionaryItem> inserts,List<DictionaryItem> updates,List<String> deletes);
	
	void delete(String id);
	
	void deleteByParent(String parentId);
}
