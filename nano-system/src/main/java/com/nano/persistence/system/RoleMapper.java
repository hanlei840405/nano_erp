package com.nano.persistence.system;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.system.Role;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface RoleMapper {

	Role findOne(String id) throws NanoDBSelectRuntimeException;

	List<Role> findMany(Role role) throws NanoDBSelectRuntimeException;

	List<Role> findMany(Role role,RowBounds rowBounds) throws NanoDBSelectRuntimeException;
	
	List<Role> findManyByUser(String userId) throws NanoDBSelectRuntimeException;
	
	List<Role> findManyByGroup(String groupId) throws NanoDBSelectRuntimeException;

	void saveOne(Role role) throws NanoDBInsertRuntimeException;

	void saveMany(List<Role> roles) throws NanoDBInsertRuntimeException;

	Long updateOne(Role role) throws NanoDBUpdateRuntimeException;

	void updateMany(List<Role> roles) throws NanoDBUpdateRuntimeException;

	Long deleteOne(Role role) throws NanoDBDeleteRuntimeException;

	void deleteMany(List<String> ids) throws NanoDBDeleteRuntimeException;
}
