package com.nano.persistence.system;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.system.Department;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface DepartmentMapper {

	Department findOne(String id) throws NanoDBSelectRuntimeException;

	List<Department> findMany(Department department) throws NanoDBSelectRuntimeException;

	List<Department> findMany(Department department,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	List<Department> findManyByParent(List<String> parentIds) throws NanoDBSelectRuntimeException;

	List<Department> findRoots() throws NanoDBSelectRuntimeException;
	
	void saveOne(Department department) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Department> departments) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Department department) throws NanoDBUpdateRuntimeException;
	
	void updateMany(List<Department> departments) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Department department) throws NanoDBDeleteRuntimeException;
	
	void deleteMany(List<String> ids) throws NanoDBDeleteRuntimeException;
}
