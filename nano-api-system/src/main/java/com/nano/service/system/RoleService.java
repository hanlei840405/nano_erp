package com.nano.service.system;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Role;

public interface RoleService {

	Role save(Role role);

	List<Role> save(List<Role> roles);

	void delete(String id);

	void delete(List<String> ids);

	Role get(String id);

	void update(Role role);

	void update(List<Role> roles);

	List<Role> queryAllRoles();

	PageInfo<Role> queryRoles(String name, int pageNo, int pageSize);

	List<Role> queryRoles(String userId, String groupId);

}
