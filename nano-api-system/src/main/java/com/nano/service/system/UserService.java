package com.nano.service.system;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.User;

public interface UserService{

	User save(User user);

	List<User> save(List<User> users);

	void delete(String id);

	void delete(List<String> ids);

	User get(String key);

	void update(User user);

	void update(List<User> users);
	
	PageInfo<User> queryUsers(String code, String departmentId, int pageNo,int pageSize);

	/**
	 * 查找组下面的所有用户信息
	 * @param groupId
	 * @return
	 */
	List<User> queryUsersByGroup(String groupId);

	List<User> queryUsers(User user);
}
