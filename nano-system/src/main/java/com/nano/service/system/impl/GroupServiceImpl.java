package com.nano.service.system.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Group;
import com.nano.domain.system.Role;
import com.nano.domain.system.User;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.system.GroupMapper;
import com.nano.persistence.system.RoleGroupMapper;
import com.nano.persistence.system.UserGroupMapper;
import com.nano.service.system.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupMapper groupMapper;

	@Autowired
	private RoleGroupMapper roleGroupMapper;

	@Autowired
	private UserGroupMapper userGroupMapper;

	@Transactional
	public Group save(Group group) {
		groupMapper.saveOne(group);
		List<Map<String, String>> roleGroups = new ArrayList<Map<String, String>>();
		for (Role role : group.getRoles()) {
			Map<String, String> roleGroup = new HashMap<String, String>();
			roleGroup.put("roleId", role.getId());
			roleGroup.put("groupId", group.getId());
			roleGroups.add(roleGroup);
		}
		if (!roleGroups.isEmpty()) {
			roleGroupMapper.saveMany(roleGroups);
		}

		List<Map<String, String>> userGroups = new ArrayList<Map<String, String>>();
		for (User user : group.getUsers()) {
			Map<String, String> userGroup = new HashMap<String, String>();
			userGroup.put("userId", user.getId());
			userGroup.put("groupId", group.getId());
			userGroups.add(userGroup);
		}
		if (!userGroups.isEmpty()) {
			userGroupMapper.saveMany(userGroups);
		}
		return group;
	}

	@Transactional
	public List<Group> save(List<Group> groups) {
		if (!groups.isEmpty()) {
			groupMapper.saveMany(groups);
		}
		for (Group group : groups) {
			List<Map<String, String>> roleGroups = new ArrayList<Map<String, String>>();
			for (Role role : group.getRoles()) {
				Map<String, String> roleGroup = new HashMap<String, String>();
				roleGroup.put("roleId", role.getId());
				roleGroup.put("groupId", group.getId());
				roleGroups.add(roleGroup);
			}
			if (!roleGroups.isEmpty()) {
				roleGroupMapper.saveMany(roleGroups);
			}

			List<Map<String, String>> userGroups = new ArrayList<Map<String, String>>();
			for (User user : group.getUsers()) {
				Map<String, String> userGroup = new HashMap<String, String>();
				userGroup.put("userId", user.getId());
				userGroup.put("groupId", group.getId());
				userGroups.add(userGroup);
			}
			if (!userGroups.isEmpty()) {
				userGroupMapper.saveMany(userGroups);
			}
		}
		return groups;
	}

	@Transactional
	public void delete(String id) {
		Group group = new Group();
		group.setParentId(id);
		// 查询直接下级
		List<Group> groups = groupMapper.findMany(group);
		group = get(id);
		if (!groups.isEmpty()) {
			throw new NanoDBDeleteRuntimeException(String.format(
					ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,
					group.getId() + " : " + group.getName()));
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("groupId", id);
		roleGroupMapper.deleteMany(params);
		userGroupMapper.deleteMany(params);

		long resultCnt = groupMapper.deleteOne(group);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					group.getId() + " : " + group.getName()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		List<Group> groups = groupMapper.findManyByParent(ids);
		if (!groups.isEmpty()) {
			String[] exists = new String[groups.size()];
			for (int i = 0; i < groups.size(); i++) {
				exists[i] = groups.get(i).getParentId();
			}
			String message = Arrays.toString(exists);
			throw new NanoDBDeleteRuntimeException(String.format(
					ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION, message));
		}
		Map<String, String> params = new HashMap<String, String>();
		for (String id : ids) {
			params.put("groupId", id);
			roleGroupMapper.deleteMany(params);
			userGroupMapper.deleteMany(params);
		}
		groupMapper.deleteMany(ids);
	}

	public Group get(String id) {
		return groupMapper.findOne(id);
	}

	@Transactional
	public void update(Group group) {
		long resultCnt = groupMapper.updateOne(group);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					group.getId() + " : " + group.getName()));
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("groupId", group.getId());
		roleGroupMapper.deleteMany(params);
		userGroupMapper.deleteMany(params);
		if (!group.getRoles().isEmpty()) {
			List<Map<String, String>> roleGroups = new ArrayList<Map<String, String>>();
			for (Role role : group.getRoles()) {
				Map<String, String> roleGroup = new HashMap<String, String>();
				roleGroup.put("roleId", role.getId());
				roleGroup.put("groupId", group.getId());
				roleGroups.add(roleGroup);
			}
			roleGroupMapper.saveMany(roleGroups);
		}
		if (!group.getUsers().isEmpty()) {
			List<Map<String, String>> userGroups = new ArrayList<Map<String, String>>();
			for (User user : group.getUsers()) {
				Map<String, String> userGroup = new HashMap<String, String>();
				userGroup.put("userId", user.getId());
				userGroup.put("groupId", group.getId());
				userGroups.add(userGroup);
			}
			userGroupMapper.saveMany(userGroups);
		}
	}

	@Transactional
	public void update(List<Group> groups) {
		groupMapper.updateMany(groups);
	}

	public PageInfo<Group> queryGroups(String code, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		Group group = new Group();
		group.setCode(code);
		List<Group> groups = groupMapper.findMany(group, rowBounds);
		PageInfo<Group> pageInfo = new PageInfo<Group>(groups);
		return pageInfo;
	}

	public List<Group> queryGroups(String code) {
		Group group = new Group();
		group.setCode(code);
		return groupMapper.findMany(group);
	}

	public List<Group> queryAllGroups() {
		Group group = new Group();
		return groupMapper.findMany(group);
	}

	public List<Group> queryGroupsByParent(String parentId) {
		Group group = new Group();
		group.setParentId(parentId);
		return groupMapper.findMany(group);
	}

	public List<Group> queryRoots() {
		return groupMapper.findRoots();
	}

}
