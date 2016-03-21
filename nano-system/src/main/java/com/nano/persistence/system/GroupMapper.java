package com.nano.persistence.system;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.system.Group;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface GroupMapper {

	Group findOne(String id) throws NanoDBSelectRuntimeException;

	List<Group> findMany(Group group) throws NanoDBSelectRuntimeException;

	List<Group> findMany(Group group,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	List<Group> findManyByParent(List<String> ids) throws NanoDBSelectRuntimeException;
	
	List<Group> findRoots() throws NanoDBSelectRuntimeException;

	void saveOne(Group group) throws NanoDBInsertRuntimeException;

	void saveMany(List<Group> groups) throws NanoDBInsertRuntimeException;

	Long updateOne(Group group) throws NanoDBUpdateRuntimeException;

	void updateMany(List<Group> groups) throws NanoDBUpdateRuntimeException;

	Long deleteOne(Group group) throws NanoDBDeleteRuntimeException;

	void deleteMany(List<String> ids) throws NanoDBDeleteRuntimeException;
}
