package com.nano.service.system.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.system.Function;
import com.nano.domain.system.Menu;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.system.FunctionMapper;
import com.nano.persistence.system.MenuMapper;
import com.nano.persistence.system.MenuPrivilegeMapper;
import com.nano.service.system.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private FunctionMapper functionMapper;

	@Autowired
	private MenuPrivilegeMapper menuPrivilegeMapper;

	@Transactional
	public Menu save(Menu menu) {
		menuMapper.saveOne(menu);
		if (!menu.getFunctions().isEmpty()) {
			functionMapper.saveMany(menu.getFunctions());
		}
		return menu;
	}

	@Transactional
	public List<Menu> save(List<Menu> menus) {
		if(!menus.isEmpty()){
			menuMapper.saveMany(menus);
		}
		return menus;
	}

	@Transactional
	public void delete(String id) {
		Menu menu = get(id);
		List<Function> functions = functionMapper.findManyByMenu(id);
		if (!functions.isEmpty()) {
			throw new NanoDBDeleteRuntimeException(String.format(
					ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,
					menu.getId() + " : " + menu.getName()));
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("menuId", id);
		menuPrivilegeMapper.deleteMany(params);
		long resultCnt = menuMapper.deleteOne(menu);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					menu.getId() + " : " + menu.getName()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		Map<String, String> params = new HashMap<String, String>();
		for (String id : ids) {
			List<Function> functions = functionMapper.findManyByMenu(id);
			if (!functions.isEmpty()) {
				String[] exists = new String[functions.size()];
				for (int i = 0; i < functions.size(); i++) {
					exists[i] = functions.get(i).getMenuId();
				}
				String message = Arrays.toString(exists);
				throw new NanoDBDeleteRuntimeException(String.format(
						ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,
						message));
			}
			params.put("menuId", id);
			menuPrivilegeMapper.deleteMany(params);
		}
		menuMapper.deleteMany(ids);
	}

	public Menu get(String id) {
		return menuMapper.findOne(id);
	}

	@Transactional
	public void update(Menu menu, List<Function> inserts,List<Function> updates,
			List<String> deletes) {
		long resultCnt = menuMapper.updateOne(menu);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					menu.getId() + " : " + menu.getName()));
		}
		if(!inserts.isEmpty()){
			functionMapper.saveMany(inserts);
		}
		
		if(!deletes.isEmpty()){
			functionMapper.deleteMany(deletes);
		}
		if(!updates.isEmpty()){
			functionMapper.updateMany(updates);
		}
	}

	@Transactional
	public void update(List<Menu> menus) {
		menuMapper.updateMany(menus);
	}

	@Transactional
	public void save(Menu menu, List<Function> inserts,
			List<String> deletes) {
		menuMapper.saveOne(menu);
		if (!inserts.isEmpty()) {
			functionMapper.saveMany(inserts);
		}
		if (!deletes.isEmpty()) {
			functionMapper.deleteMany(deletes);
		}
	}

	public List<Menu> queryRootMenus() {
		return menuMapper.findRoots();
	}

	public PageInfo<Menu> queryRootMenus(int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		Menu menu = new Menu();
		List<Menu> menus = menuMapper.findMany(menu, rowBounds);
		PageInfo<Menu> pageInfo = new PageInfo<Menu>(menus);
		return pageInfo;
	}

	public List<Menu> queryAllMenus() {
		Menu menu = new Menu();
		return menuMapper.findMany(menu);
	}

	public List<Menu> queryMenuByParent(String parentId) {
		Menu menu = new Menu();
		menu.setParentId(parentId);
		return menuMapper.findMany(menu);
	}

	public List<Menu> queryMenuByPrivilege(String privilegeId) {
		return menuMapper.findManyByPrivilege(privilegeId);
	}

}
