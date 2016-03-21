package com.nano.persistence.system;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.system.User;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface UserMapper {

	User findOne(String key) throws NanoDBSelectRuntimeException;

	List<User> findMany(User user) throws NanoDBSelectRuntimeException;

	List<User> findMany(User user,RowBounds rowBounds) throws NanoDBSelectRuntimeException;
	
	List<User> findManyByGroup(String groupId) throws NanoDBSelectRuntimeException;

	List<User> findManyByDepartment(List<String> departmentIds) throws NanoDBSelectRuntimeException;

	void saveOne(User user) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<User> users) throws NanoDBInsertRuntimeException;
	
	Long updateOne(User user) throws NanoDBUpdateRuntimeException;
	
	void updateMany(List<User> users) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(User user) throws NanoDBDeleteRuntimeException;
	
	void deleteMany(List<String> ids) throws NanoDBDeleteRuntimeException;
}
