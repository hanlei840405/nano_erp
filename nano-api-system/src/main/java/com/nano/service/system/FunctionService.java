package com.nano.service.system;

import java.util.List;

import com.nano.domain.system.Function;

public interface FunctionService {

	Function save(Function function);

	List<Function> save(List<Function> functions);

	void delete(String id);

	void delete(List<String> ids);

	Function get(String id);

	void update(Function function);

	void update(List<Function> functions);

	List<Function> queryFunctionsByMenu(String menuId);

	void deleteManyByMenu(String menuId);
}
