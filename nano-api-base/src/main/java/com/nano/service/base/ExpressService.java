package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Express;

public interface ExpressService {

	Express save(Express express);

	List<Express> save(List<Express> expresses);

	void delete(String id);

	void delete(List<String> ids);

	Express findOne(String id);

	void startStop(Express express);

	void startStop(List<Express> expresses);

	void update(Express express);

	void update(List<Express> expresses);

	PageInfo<Express> find(Express express, int pageNo, int pageSize);

	List<Express> find(Express express);
}
