package com.nano.service.base.impl;

import com.nano.domain.base.DictionaryItem;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.DictionaryItemMapper;
import com.nano.service.base.DictionaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictionaryItemServiceImpl implements DictionaryItemService{

	@Autowired
	private DictionaryItemMapper dictionaryItemMapper;
	
	public DictionaryItem findOne(String id) {
		return dictionaryItemMapper.findOne(id);
	}

	public List<DictionaryItem> find(DictionaryItem dictionaryItem) {
		return dictionaryItemMapper.findMany(dictionaryItem);
	}

	@Transactional
	public DictionaryItem save(DictionaryItem dictionaryItem) {
		dictionaryItemMapper.saveOne(dictionaryItem);
		return dictionaryItem;
	}

	@Transactional
	public List<DictionaryItem> save(List<DictionaryItem> dictionaryItems) {
		dictionaryItemMapper.saveMany(dictionaryItems);
		return dictionaryItems;
	}

	@Transactional
	public void update(List<DictionaryItem> inserts,List<DictionaryItem> updates,List<String> deletes) {
		for(DictionaryItem dictionaryItem : inserts){
			save(dictionaryItem);
		}
		for(DictionaryItem dictionaryItem : updates){
			dictionaryItemMapper.updateOne(dictionaryItem);
		}
		for(String id : deletes){
			delete(id);
		}
	}

	@Transactional
	public void delete(String id) {
		DictionaryItem dictionaryItem = findOne(id);
		long resultCnt = dictionaryItemMapper.deleteOne(dictionaryItem);
		if(resultCnt == 0){
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					dictionaryItem.getId() + " : " + dictionaryItem.getName()));
		}
	}

	@Transactional
	public void deleteByParent(String parentId) {
		dictionaryItemMapper.deleteByParentId(parentId);
	}

}
