package com.nano.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Role;
import com.nano.domain.system.User;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.system.GroupMapper;
import com.nano.persistence.system.PrivilegeMapper;
import com.nano.persistence.system.RoleMapper;
import com.nano.persistence.system.UserGroupMapper;
import com.nano.persistence.system.UserMapper;
import com.nano.persistence.system.UserRoleMapper;
import com.nano.service.system.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private UserGroupMapper userGroupMapper;

	@Autowired
	private GroupMapper groupMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private PrivilegeMapper privilegeMapper;

	public User get(String key) {
		User user = userMapper.findOne(key);
		if(user == null){
			return user;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", user.getId());
		List<Map<String,String>> userGroups = userGroupMapper.findMany(params);
		for(Map<String,String> userGroup : userGroups){
			user.getGroups().add(groupMapper.findOne(userGroup.get("group_id")));
		}
		List<Map<String,String>> userRoles = userRoleMapper.findMany(params);
		params.clear();
		for(Map<String,String> userRole : userRoles){
			Role role = roleMapper.findOne(userRole.get("role_id"));
			user.getRoles().add(role);
		}
		return user;
	}

	@Transactional
	public User save(User user) {
		userMapper.saveOne(user);
		List<Map<String, String>> userRoles = new ArrayList<Map<String, String>>();
		for (Role role : user.getRoles()) {
			Map<String, String> userRole = new HashMap<String, String>();
			userRole.put("userId", user.getId());
			userRole.put("roleId", role.getId());
			userRoles.add(userRole);
		}
		if (!userRoles.isEmpty()) {
			userRoleMapper.saveMany(userRoles);
		}
		return user;
	}

	@Transactional
	public void delete(String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", id);
		userRoleMapper.deleteMany(params);
		userGroupMapper.deleteMany(params);
		User user = get(id);
		long resultCnt = userMapper.deleteOne(user);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,user.getId() + " : " + user.getName()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		Map<String, String> params = new HashMap<String, String>();
		for (String id : ids) {
			params.put("userId", id);
			userRoleMapper.deleteMany(params);
			userGroupMapper.deleteMany(params);
		}
		userMapper.deleteMany(ids);
	}

	public PageInfo<User> queryUsers(String code, String departmentId,
			int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		User user = new User();
		user.setCode(code);
		user.setDepartmentId(departmentId);
		List<User> users = userMapper.findMany(user, rowBounds);
		PageInfo<User> pageInfo = new PageInfo<User>(users);
		return pageInfo;
	}

	public List<User> queryUsersByGroup(String groupId) {
		return userMapper.findManyByGroup(groupId);
	}

	public List<User> queryUsers(User user){
		return userMapper.findMany(user);
	}

	@Transactional
	public List<User> save(List<User> users) {
		if(!users.isEmpty()){
			userMapper.saveMany(users);
		}
		for(User user : users){
			List<Map<String, String>> userRoles = new ArrayList<Map<String, String>>();
			for (Role role : user.getRoles()) {
				Map<String, String> userRole = new HashMap<String, String>();
				userRole.put("userId", user.getId());
				userRole.put("roleId", role.getId());
				userRoles.add(userRole);
			}
			if (!userRoles.isEmpty()) {
				userRoleMapper.saveMany(userRoles);
			}
		}
		return users;
	}

	@Transactional
	public void update(User user) {
		long updateCnt = userMapper.updateOne(user);
		if (updateCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,user.getId() + " : " + user.getName()));
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", user.getId());
		userRoleMapper.deleteMany(params);
		List<Map<String, String>> userRoles = new ArrayList<Map<String, String>>();
		for (Role role : user.getRoles()) {
			Map<String, String> userRole = new HashMap<String, String>();
			userRole.put("userId", user.getId());
			userRole.put("roleId", role.getId());
			userRoles.add(userRole);
		}
		if (!userRoles.isEmpty()) {
			userRoleMapper.saveMany(userRoles);
		}
	}

	@Transactional
	public void update(List<User> users) {
		userMapper.updateMany(users);
	}

}
