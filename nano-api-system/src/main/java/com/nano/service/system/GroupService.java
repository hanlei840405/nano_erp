package com.nano.service.system;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Group;

public interface GroupService{

	Group save(Group group);

	List<Group> save(List<Group> groups);

	void delete(String id);

	void delete(List<String> ids);

	Group get(String id);

	void update(Group group);

	void update(List<Group> groups);
	
	PageInfo<Group> queryGroups(String code, int pageNo,int pageSize);

	List<Group> queryGroups(String code);

	List<Group> queryAllGroups();
	
	List<Group> queryGroupsByParent(String parentId);

	List<Group> queryRoots();
}
