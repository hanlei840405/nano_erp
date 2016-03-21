package com.nano.service.system;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Function;
import com.nano.domain.system.Menu;

public interface MenuService {
	Menu save(Menu menu);

	List<Menu> save(List<Menu> menus);

	void delete(String id);

	void delete(List<String> ids);

	Menu get(String id);

	void update(Menu menu, List<Function> inserts,List<Function> updates,
			List<String> deletes);

	void update(List<Menu> menus);

	void save(Menu menu, List<Function> inserts,
			List<String> deletes);

	List<Menu> queryRootMenus();

	PageInfo<Menu> queryRootMenus(int pageNo, int pageSize);

	List<Menu> queryAllMenus();

	List<Menu> queryMenuByParent(String parentId);

	List<Menu> queryMenuByPrivilege(String privilegeId);

	// List<Menu> queryAllChildren(Menu menu);
}
