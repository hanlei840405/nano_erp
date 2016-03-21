package com.nano.service.base.impl;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Dictionary;
import com.nano.domain.base.DictionaryItem;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.DictionaryMapper;
import com.nano.service.base.DictionaryItemService;
import com.nano.service.base.DictionaryService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

	@Autowired
	private DictionaryMapper dictionaryMapper;
	@Autowired
	private DictionaryItemService dictionaryItemService;

	public Dictionary findOne(String key) {
		return dictionaryMapper.findOne(key);
	}

	public List<Dictionary> find(Dictionary dictionary) {
		return dictionaryMapper.findMany(dictionary);
	}

	public PageInfo<Dictionary> find(Dictionary dictionary, int pageNo,
			int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Dictionary> dictionaries = dictionaryMapper.findMany(dictionary, rowBounds);
		PageInfo<Dictionary> pageInfo = new PageInfo<Dictionary>(dictionaries);
		return pageInfo;
	}

	@Transactional
	public Dictionary save(Dictionary dictionary,List<DictionaryItem> inserts) {
		dictionaryMapper.saveOne(dictionary);
		for(DictionaryItem dictionaryItem : inserts){
			dictionaryItemService.save(dictionaryItem);
		}
		return dictionary;
	}

	@Transactional
	public List<Dictionary> save(List<Dictionary> dictionaries) {
		dictionaryMapper.saveMany(dictionaries);
		return dictionaries;
	}

	@Transactional
	public void delete(List<String> ids) {
		for(String id : ids){
			delete(id);
		}
	}

	@Transactional
	public void delete(String id) {
		Dictionary dictionary = findOne(id);
		dictionaryItemService.deleteByParent(id);
		long resultCnt = dictionaryMapper.deleteOne(dictionary);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					dictionary.getId() + " : " + dictionary.getName()));
		}
	}

}
