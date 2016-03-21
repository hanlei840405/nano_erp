package com.nano.persistence.system;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.system.Menu;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface MenuMapper {

	Menu findOne(String id) throws NanoDBSelectRuntimeException;

	List<Menu> findMany(Menu menu) throws NanoDBSelectRuntimeException;

	List<Menu> findMany(Menu menu,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	List<Menu> findRoots() throws NanoDBSelectRuntimeException;
	
	List<Menu> findManyByPrivilege(String privilegeId) throws NanoDBSelectRuntimeException;

	void saveOne(Menu menu) throws NanoDBInsertRuntimeException;

	void saveMany(List<Menu> menus) throws NanoDBInsertRuntimeException;

	Long updateOne(Menu menu) throws NanoDBUpdateRuntimeException;

	void updateMany(List<Menu> menus) throws NanoDBUpdateRuntimeException;

	Long deleteOne(Menu menu) throws NanoDBDeleteRuntimeException;

	void deleteMany(List<String> ids) throws NanoDBDeleteRuntimeException;
}
