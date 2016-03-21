package com.nano.service.base;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Storage;

public interface StorageService {

	Storage save(Storage storage);

	List<Storage> save(List<Storage> storages);

	void delete(String id);

	void delete(List<String> ids);

	Storage findOne(String id);

	void update(Storage storage);

	void storage(Storage storage);

	void storage(List<Storage> storages);

	void update(List<Storage> storages);

	PageInfo<Storage> find(Map<String,String> params, int pageNo, int pageSize);

	List<Storage> find(Map<String,String> params);
}
