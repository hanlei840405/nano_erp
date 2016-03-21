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
import com.nano.domain.system.Privilege;
import com.nano.domain.system.Role;
import com.nano.domain.system.User;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.system.PrivilegeMapper;
import com.nano.persistence.system.RoleMapper;
import com.nano.persistence.system.RolePrivilegeMapper;
import com.nano.persistence.system.UserMapper;
import com.nano.persistence.system.UserRoleMapper;
import com.nano.service.system.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private RolePrivilegeMapper rolePrivilegeMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private PrivilegeMapper privilegeMapper;

	@Autowired
	private UserMapper userMapper;

	@Transactional
	public Role save(Role role) {
		roleMapper.saveOne(role);
		List<Map<String, String>> rolePrivileges = new ArrayList<Map<String, String>>();
		for (Privilege privilege : role.getPrivileges()) {
			Map<String, String> rolePrivilege = new HashMap<String, String>();
			rolePrivilege.put("roleId", role.getId());
			rolePrivilege.put("privilegeId", privilege.getId());
			rolePrivileges.add(rolePrivilege);
		}
		if (!rolePrivileges.isEmpty()) {
			rolePrivilegeMapper.saveMany(rolePrivileges);
		}
		List<Map<String, String>> userRoles = new ArrayList<Map<String, String>>();
		for (User user : role.getUsers()) {
			Map<String, String> userRole = new HashMap<String, String>();
			userRole.put("roleId", role.getId());
			userRole.put("userId", user.getId());
			userRoles.add(userRole);
		}
		if (!userRoles.isEmpty()) {
			userRoleMapper.saveMany(userRoles);
		}
		return role;
	}

	@Transactional
	public List<Role> save(List<Role> roles) {
		roleMapper.saveMany(roles);
		for (Role role : roles) {
			List<Map<String, String>> rolePrivileges = new ArrayList<Map<String, String>>();
			for (Privilege privilege : role.getPrivileges()) {
				Map<String, String> rolePrivilege = new HashMap<String, String>();
				rolePrivilege.put("roleId", role.getId());
				rolePrivilege.put("privilegeId", privilege.getId());
				rolePrivileges.add(rolePrivilege);
			}
			if (!rolePrivileges.isEmpty()) {
				rolePrivilegeMapper.saveMany(rolePrivileges);
			}
			List<Map<String, String>> userRoles = new ArrayList<Map<String, String>>();
			for (User user : role.getUsers()) {
				Map<String, String> userRole = new HashMap<String, String>();
				userRole.put("roleId", role.getId());
				userRole.put("userId", user.getId());
				userRoles.add(userRole);
			}
			if (!userRoles.isEmpty()) {
				userRoleMapper.saveMany(userRoles);
			}
		}
		return roles;
	}

	@Transactional
	public void delete(String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("roleId", id);
		userRoleMapper.deleteMany(params);
		rolePrivilegeMapper.deleteMany(params);
		Role role = get(id);
		long resultCnt = roleMapper.deleteOne(role);
		if (resultCnt == 0) {
			throw new NanoDBDeleteRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					role.getId() + " : " + role.getName()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		Map<String, String> params = new HashMap<String, String>();
		for (String id : ids) {
			params.put("roleId", id);
			userRoleMapper.deleteMany(params);
			rolePrivilegeMapper.deleteMany(params);
		}
		roleMapper.deleteMany(ids);
	}

	public Role get(String id) {
		Role role = roleMapper.findOne(id);
		Map<String, String> params = new HashMap<String, String>();
		params.put("roleId", role.getId());
		List<Map<String, String>> rolePrivileges = rolePrivilegeMapper
				.findMany(params);
		for (Map<String, String> rolePrivilege : rolePrivileges) {
			role.getPrivileges().add(
					privilegeMapper.findOne(rolePrivilege.get("privilege_id")));
		}
		List<Map<String, String>> userRoles = userRoleMapper.findMany(params);
		for (Map<String, String> userRole : userRoles) {
			role.getUsers().add(userMapper.findOne(userRole.get("user_id")));
		}
		return role;
	}

	@Transactional
	public void update(Role role) {
		long updateCnt = roleMapper.updateOne(role);
		if (updateCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					role.getId() + " : " + role.getName()));
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("roleId", role.getId());
		rolePrivilegeMapper.deleteMany(params);
		userRoleMapper.deleteMany(params);
		if (!role.getPrivileges().isEmpty()) {
			List<Map<String, String>> rolePrivileges = new ArrayList<Map<String, String>>();
			for (Privilege privilege : role.getPrivileges()) {
				Map<String, String> rolePrivilege = new HashMap<String, String>();
				rolePrivilege.put("roleId", role.getId());
				rolePrivilege.put("privilegeId", privilege.getId());
				rolePrivileges.add(rolePrivilege);
			}
			rolePrivilegeMapper.saveMany(rolePrivileges);
		}
		if (!role.getUsers().isEmpty()) {
			List<Map<String, String>> userRoles = new ArrayList<Map<String, String>>();
			for (User user : role.getUsers()) {
				Map<String, String> userRole = new HashMap<String, String>();
				userRole.put("roleId", role.getId());
				userRole.put("userId", user.getId());
				userRoles.add(userRole);
			}
			userRoleMapper.saveMany(userRoles);
		}
	}

	@Transactional
	public void update(List<Role> roles) {
		roleMapper.updateMany(roles);
	}

	public List<Role> queryAllRoles() {
		return roleMapper.findMany(new Role());
	}

	public PageInfo<Role> queryRoles(String name, int pageNo, int pageSize) {
		Role role = new Role();
		role.setName(name);
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		List<Role> roles = roleMapper.findMany(role, rowBounds);
		PageInfo<Role> pageInfo = new PageInfo<Role>(roles);
		return pageInfo;
	}

	public List<Role> queryRoles(String userId, String groupId) {
		List<Role> roles = new ArrayList<Role>();
		roles.addAll(roleMapper.findManyByUser(userId));
		roles.addAll(roleMapper.findManyByGroup(groupId));
		return roles;
	}

}
