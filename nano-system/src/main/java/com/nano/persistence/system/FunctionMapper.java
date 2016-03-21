package com.nano.persistence.system;

import java.util.List;

import com.nano.domain.system.Function;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface FunctionMapper {

	Function findOne(String id) throws NanoDBSelectRuntimeException;

	List<Function> findManyByMenu(String menuId) throws NanoDBSelectRuntimeException;

	List<Function> findManyByPrivilege(String privilegeId) throws NanoDBSelectRuntimeException;

	void saveOne(Function function) throws NanoDBInsertRuntimeException;

	void saveMany(List<Function> functions) throws NanoDBInsertRuntimeException;

	Long updateOne(Function function) throws NanoDBUpdateRuntimeException;

	void updateMany(List<Function> functions) throws NanoDBUpdateRuntimeException;

	Long delete(Function function) throws NanoDBDeleteRuntimeException;

	void deleteMany(List<String> ids) throws NanoDBDeleteRuntimeException;
}
