package com.nano.service.system.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Department;
import com.nano.domain.system.User;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.system.DepartmentMapper;
import com.nano.persistence.system.UserMapper;
import com.nano.service.system.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private UserMapper userMapper;

	@Transactional
	public Department save(Department department) {
		departmentMapper.saveOne(department);
		return department;
	}

	@Transactional
	public void delete(String id) {
		Department department = new Department();
		department.setParentId(id);
		// 查询直接下级
		List<Department> departments = departmentMapper.findMany(department);
		User user = new User();
		user.setDepartmentId(id);
		List<User> users = userMapper.findMany(user);
		department = get(id);
		if(!users.isEmpty()){
			throw new NanoDBDeleteRuntimeException(String.format(ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,department.getId() + " : " + department.getName()));
		}
		if(!departments.isEmpty()){
			throw new NanoDBDeleteRuntimeException(String.format(ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,department.getId() + " : " + department.getName()));
		}
		long resultCnt = departmentMapper.deleteOne(department);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,department.getId() + " : " + department.getName()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		List<User> users = userMapper.findManyByDepartment(ids);
		if(!users.isEmpty()){
			String[] exists = new String[users.size()];
			for(int i = 0;i < users.size();i++){
				exists[i] = users.get(i).getDepartmentId();
			}
			String message = Arrays.toString(exists);
			throw new NanoDBDeleteRuntimeException(String.format(ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,message));
		}
		List<Department> departments = departmentMapper.findManyByParent(ids);
		if(!departments.isEmpty()){
			String[] exists = new String[departments.size()];
			for(int i = 0;i < departments.size();i++){
				exists[i] = departments.get(i).getParentId();
			}
			String message = Arrays.toString(exists);
			throw new NanoDBDeleteRuntimeException(String.format(ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,message));
		}
		departmentMapper.deleteMany(ids);
	}

	public Department get(String id) {
		return departmentMapper.findOne(id);
	}

	public List<Department> queryDepartments(String parentId) {
		Department department = new Department();
		department.setParentId(parentId);
		return departmentMapper.findMany(department);
	}

	public List<Department> queryRootDepartment() {
		return departmentMapper.findRoots();
	}

	public void update(Department department) {
		long resultCnt = departmentMapper.updateOne(department);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException();
		}
	}

	public void update(List<Department> departments) {
		departmentMapper.updateMany(departments);
	}

	public List<Department> save(List<Department> departments) {
		if(!departments.isEmpty()){
			departmentMapper.saveMany(departments);
		}
		return departments;
	}

	public PageInfo<Department> findMany(Department department,
			int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		List<Department> departments = departmentMapper.findMany(department,rowBounds);
		PageInfo<Department> pageInfo = new PageInfo<Department>(departments);
		return pageInfo;
	}

}
