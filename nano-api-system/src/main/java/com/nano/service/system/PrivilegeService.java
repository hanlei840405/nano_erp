package com.nano.service.system;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Privilege;

public interface PrivilegeService{

	Privilege save(Privilege privilege);

	List<Privilege> save(List<Privilege> privileges);

	void delete(String id);

	void delete(List<String> ids);

	Privilege get(String id);

	void update(Privilege privilege);

	void update(List<Privilege> privileges);
	
	List<Privilege> queryAllPrivileges();

	PageInfo<Privilege> queryPrivileges(String menuCode, String functionCode,
			String elementCode, String category, int pageNo,int pageSize);

	List<Privilege> queryPrivileges(String roleId);
}
