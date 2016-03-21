package com.nano.service.system;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Department;

public interface DepartmentService{

	Department save(Department department);

	List<Department> save(List<Department> departments);

	void delete(String id);

	void delete(List<String> ids);

	Department get(String id);

	void update(Department department);

	void update(List<Department> departments);
	
	List<Department> queryDepartments(String parentId);

	List<Department> queryRootDepartment();
	
	PageInfo<Department> findMany(Department department,int pageNo,int pageSize);
}
