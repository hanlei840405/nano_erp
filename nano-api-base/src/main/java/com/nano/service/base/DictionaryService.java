package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Dictionary;
import com.nano.domain.base.DictionaryItem;

public interface DictionaryService {

	Dictionary findOne(String key);

	List<Dictionary> find(Dictionary dictionary);

	PageInfo<Dictionary> find(Dictionary dictionary,int pageNo,int pageSize);

	Dictionary save(Dictionary dictionary,List<DictionaryItem> insert);
	
	List<Dictionary> save(List<Dictionary> dictionaries);
	
	void delete(String id);

	void delete(List<String> ids);
}
